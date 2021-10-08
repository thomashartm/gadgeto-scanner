package biz.netcentric.security.gadgeto.engine

import biz.netcentric.security.gadgeto.engine.cmd.CmdLayout
import biz.netcentric.security.gadgeto.engine.cmd.CmdModule
import biz.netcentric.security.gadgeto.engine.cmd.CmdSupport
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
        List<String> ignored = []
        List<String> installed = []
        phaseDefinition.getModules().each {definition ->
            String executable = definition.executable
            List<String> dependencies = definition.dependencies != null ? definition.dependencies : []

            if(!installed.contains(executable) && !ignored.contains(executable)){
                boolean installStatus = CmdSupport.verifyIfInstalled(executable)
                if(dependencies.size() > 0 && !CmdSupport.verifyDependenciesAreInstalled(dependencies)) {
                    CmdSupport.printMessage CmdLayout.WARNING, "[PRE-CHECK] Skipping module ${definition.name}. Dependencies not found."
                    installStatus = false
                }

                if(installStatus){
                    installed.add executable
                }else{
                    ignored.add executable
                }
            }

            if(installed.contains(executable)) {
                modules.add new CmdModule(moduleDefinition: definition)
            }else{
                CmdSupport.printMessage CmdLayout.WARNING, "[PRE-CHECK] Skipping module ${definition.name}. Required executable ${executable} or dependencies ${dependencies.join(",")} are not installed."
            }
        }

        return modules
    }
}
