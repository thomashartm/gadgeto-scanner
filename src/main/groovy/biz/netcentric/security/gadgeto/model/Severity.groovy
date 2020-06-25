package biz.netcentric.security.gadgeto.model

enum Severity {

    NONE("n", "none", -1),
    INFORMATIONAL("i", "informational", 0),
    LOW("l", "low", 1),
    MEDIUM("m", " medium", 2),
    HIGH("h", "high", 3),
    CRITICAL("c", "critical", 4);

    String name

    String token

    int rank

    Severity(String name, String token, int rank) {
        this.name = name
        this.token = token
        this.rank = rank
    }
}
