package biz.netcentric.security.gadgeto.engine

import biz.netcentric.security.gadgeto.engine.cmd.CmdModule
import biz.netcentric.security.gadgeto.model.PhaseDefinition

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
