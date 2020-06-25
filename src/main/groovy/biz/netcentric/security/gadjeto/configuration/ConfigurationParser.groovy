package biz.netcentric.security.gadjeto.configuration

import biz.netcentric.security.gadjeto.engine.ConcretePhase
import biz.netcentric.security.gadjeto.model.PhaseDefinition
import groovy.util.logging.Slf4j
import groovy.yaml.YamlSlurper

@Slf4j
class ConfigurationParser {

    List<ConcretePhase> loadAllFromConfig(List<File> configLocations) {
        List<ConcretePhase> phases = []
        configLocations.each { file ->
            log.info "Loading configuration from: ${file}"
            if (file != null && file.exists()) {
                if (file.isDirectory()) {
                    // load contents
                    file.eachFileMatch(~/.*.yaml/) { yamlFile ->
                        phases.add yamlFile
                    }
                } else {
                    // load file
                    ConcretePhase phase = loadPhase(file)
                    phases.add phase
                }
            } else {
                log.error "Unable to find file."
            }
        }
        phases
    }

    ConcretePhase loadPhase(File file) {
        String fileContent = file.text
        PhaseDefinition phaseDefinition = parseToPhaseDefinition(fileContent)

        new ConcretePhase(phaseDefinition: phaseDefinition)
    }

    PhaseDefinition parseToPhaseDefinition(String source) {
        def yaml = new YamlSlurper()
        PhaseDefinition definition = yaml.parseText(source)
        definition
    }

    def parse(String configurationSource) {
        def yaml = new YamlSlurper()
        List moduleDefinitions = yaml.parseText(configurationSource)

        moduleDefinitions
    }

}