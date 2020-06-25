package biz.netcentric.security.gadjeto.model

class TriageUtil {

    static Severity getSeverityByRank(int rank){
        Severity severity = Severity.NONE
        Severity.values().each {sev ->
            if(sev.getRank() == rank){
                severity = sev
                return
            }
        }

        severity
    }
}
