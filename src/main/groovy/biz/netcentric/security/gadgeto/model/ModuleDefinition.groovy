package biz.netcentric.security.gadgeto.model

class ModuleDefinition {

    String name

    String executable

    String args

    String description

    String remediation

    int severity

    String mode

    List<String> dependencies

    List<String> positiveResponse

    List<String> failedResponse

    Severity getSeverityLevel(){
        TriageUtil.getSeverityByRank(this.severity)
    }
}
