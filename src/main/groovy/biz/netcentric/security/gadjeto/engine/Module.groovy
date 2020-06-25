package biz.netcentric.security.gadjeto.engine

trait Module {

    def abstract run(String target)

    abstract List getFailPattern()

    abstract List getPositiveResponsePatterns()
}