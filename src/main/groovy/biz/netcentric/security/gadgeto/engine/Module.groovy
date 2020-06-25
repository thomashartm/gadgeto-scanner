package biz.netcentric.security.gadgeto.engine

trait Module {

    def abstract run(String target)

    abstract List getFailPattern()

    abstract List getPositiveResponsePatterns()
}