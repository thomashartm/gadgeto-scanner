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


            if(!installed.contains(executable) && !ignored.contains(executable)){
                boolean installStatus = CmdSupport.verifyIfInstalled(executable)
                if(installStatus){
                    installed.add executable
                }else{
                    ignored.add executable
                }
            }

            if(installed.contains(executable)) {
                modules.add new CmdModule(moduleDefinition: definition)
            }else{
                CmdSupport.printMessage CmdLayout.WARNING, "[PRE-CHECK] Skipping module ${definition.name}. Required executable ${executable} is not installed."
            }
        }

        return modules
    }
}
