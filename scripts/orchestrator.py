#!/usr/bin/env python3
"""Orchestrate the recon agent and summarize results with an LLM via Ollama."""
import argparse
import json
import subprocess

import ollama


def run_recon(url: str) -> dict:
    """Execute recon_agent.py for the given URL and return the parsed JSON."""
    proc = subprocess.run(
        ["python3", "scripts/recon_agent.py", url],
        capture_output=True, text=True
    )
    if proc.returncode != 0:
        raise RuntimeError(proc.stderr)
    return json.loads(proc.stdout)


def summarize(model: str, data: dict) -> str:
    """Ask the Ollama model to summarize the reconnaissance data."""
    prompt = (
        "Summarize the following reconnaissance JSON data in a concise report:\n\n"
        + json.dumps(data, indent=2)
    )
    response = ollama.chat(model=model, messages=[{"role": "user", "content": prompt}])
    return response["message"]["content"].strip()


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Run the recon agent and summarize results with an Ollama model"
    )
    parser.add_argument("url", help="Target URL for reconnaissance")
    parser.add_argument(
        "--model", default="llama3", help="Ollama model to use for summarization"
    )
    args = parser.parse_args()

    data = run_recon(args.url)
    report = summarize(args.model, data)
    print(report)


if __name__ == "__main__":
    main()
