package biz.netcentric.security.gadjeto.engine

import biz.netcentric.security.gadjeto.engine.cmd.CmdModule
import biz.netcentric.security.gadjeto.model.PhaseDefinition

class ConcretePhase implements Phase {

    PhaseDefinition phaseDefinition

    @Override
    String getPhaseId() {
        return phaseDefinition.getId()
    }

    @Override
    String getName() {
        return phaseDefinition.getName()
    }

    @Override
    List<Module> getAll() {
        List<Module> modules = []
        phaseDefinition.getModules().each {definition ->
            modules.add new CmdModule(moduleDefinition: definition)
        }
        return modules
    }
}
