package biz.netcentric.security.gadgeto.engine

import biz.netcentric.security.gadgeto.engine.cmd.CmdSupport


trait Phase {

    abstract String getPhaseId()

    abstract String getName()

    abstract List<Module> getAll()

    void execute(String target){
        List<Module> modules = getAll()

        CmdSupport.emptyLine()

        modules.each {module ->
            module.run(target)
        }
    }

    void execute(String target, Closure closure){
        List<Module> modules = getAll()

        modules.each {module ->
            module.run(target)
        }
    }
}