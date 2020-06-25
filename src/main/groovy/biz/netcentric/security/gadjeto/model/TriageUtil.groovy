package biz.netcentric.security.gadjeto.model

class TriageUtil {

    static Severity getSeverityByRank(int rank){
        Severity.values().each {severity ->
            if(severity.getRank() == rank){
                return severity
            }
        }

        Severity.NONE
    }
}
