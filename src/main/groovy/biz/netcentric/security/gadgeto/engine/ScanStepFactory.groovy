package biz.netcentric.security.gadgeto.engine

import biz.netcentric.security.gadgeto.configuration.ConfigurationParser
import biz.netcentric.security.gadgeto.engine.cmd.CmdModule

trait ScanStepFactory {

    ConfigurationParser moduleParser = new ConfigurationParser()

    List<Module> create(List<String> paths) {
        List<Module> modules = []
        moduleParser.loadModules(paths).each { moduleDefinition ->
            modules.add(new CmdModule(moduleDefinition: moduleDefinition))
        }

        modules
    }

    List<Module> getAll() {
        List<String> allPaths = paths()
        create(allPaths)
    }
}
