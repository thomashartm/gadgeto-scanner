import argparse
import json
import re
import socket
from urllib.parse import urljoin

import dns.resolver
import httpx
import requests
import tldextract
from bs4 import BeautifulSoup


def gather_dns_info(domain):
    result = {"A": [], "AAAA": [], "MX": [], "NS": [], "TXT": [], "CNAME": []}
    try:
        for rtype in result.keys():
            try:
                answers = dns.resolver.resolve(domain, rtype)
                result[rtype] = [str(a) for a in answers]
            except Exception:
                pass
    except Exception:
        pass
    try:
        result["ip"] = socket.gethostbyname(domain)
    except Exception:
        result["ip"] = None
    return result


def enumerate_subdomains(domain):
    subdomains = set()
    try:
        url = f"https://crt.sh/?q=%25.{domain}&output=json"
        r = requests.get(url, timeout=10)
        if r.status_code == 200:
            for entry in r.json():
                value = entry.get("name_value", "")
                for line in value.splitlines():
                    if line.endswith(domain) and line != domain:
                        subdomains.add(line.strip())
    except Exception:
        pass
    return sorted(subdomains)


def fetch_page(url):
    try:
        resp = httpx.get(url, timeout=10, follow_redirects=True)
        return resp
    except Exception:
        return None


def parse_html_for_apis(base_url, html):
    apis = set()
    soup = BeautifulSoup(html, "html.parser")
    script_contents = []
    for tag in soup.find_all("script"):
        if tag.get("src"):
            src_url = urljoin(base_url, tag["src"])
            resp = fetch_page(src_url)
            if resp:
                script_contents.append(resp.text)
        else:
            if tag.string:
                script_contents.append(tag.string)
    pattern_urls = re.compile(r"https?://[\w./%-]+", re.I)
    pattern_fetch = re.compile(r"(?:fetch|axios\.\w+|XMLHttpRequest)\s*\(\s*[\'\"]([^\'\"]+)")
    for content in script_contents:
        for url in pattern_urls.findall(content):
            apis.add(url)
        for url in pattern_fetch.findall(content):
            if url.startswith("/"):
                url = urljoin(base_url, url)
            apis.add(url)
    return sorted(apis)


def check_robots(domain):
    url = f"https://{domain}/robots.txt"
    resp = fetch_page(url)
    if resp and resp.status_code == 200:
        return resp.text
    return None


def check_well_known(domain):
    well_known_paths = [
        "security.txt",
        "change-password",
    ]
    data = {}
    for path in well_known_paths:
        url = f"https://{domain}/.well-known/{path}"
        resp = fetch_page(url)
        if resp and resp.status_code == 200:
            data[path] = resp.text
    return data


def gather_info(target_url):
    parts = tldextract.extract(target_url)
    domain = ".".join(part for part in [parts.domain, parts.suffix] if part)
    result = {"domain": domain}
    result["dns"] = gather_dns_info(domain)
    result["subdomains"] = enumerate_subdomains(domain)
    page = fetch_page(target_url)
    if page:
        result["http_headers"] = dict(page.headers)
        result["api_endpoints"] = parse_html_for_apis(target_url, page.text)
    else:
        result["http_headers"] = {}
        result["api_endpoints"] = []
    result["robots"] = check_robots(domain)
    result["well_known"] = check_well_known(domain)
    return result


def main():
    parser = argparse.ArgumentParser(description="Simple reconnaissance agent")
    parser.add_argument("url", help="Target URL, e.g. https://example.com")
    parser.add_argument("-o", "--output", help="Write JSON report to file")
    args = parser.parse_args()

    info = gather_info(args.url)
    json_output = json.dumps(info, indent=2)
    if args.output:
        with open(args.output, "w") as f:
            f.write(json_output)
    print(json_output)


if __name__ == "__main__":
    main()
