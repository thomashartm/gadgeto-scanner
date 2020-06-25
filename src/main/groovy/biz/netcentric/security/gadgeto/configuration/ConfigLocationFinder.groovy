package biz.netcentric.security.gadgeto.configuration

import groovy.util.logging.Slf4j

@Slf4j
class ConfigLocationFinder {

    List<File> getConfigFiles(final List<String> locations){
        List<File> detectedYamlConfigs = []
        locations.each {location ->
            File file = new File(location)
            if(file != null && file.exists()){

                // going through the directory and collect all yaml files
                if(file.isDirectory()){
                    file.eachFileMatch(~/.*.yaml/) { yamlFile ->
                        detectedYamlConfigs.add yamlFile
                    }
                }else if(file.getName().endsWith(".yaml")){
                    detectedYamlConfigs.add file
                }else{
                    log.error "Config path is neither a cpnfig folder nor a yaml config: ${location}"
                }
            }else{
                log.error "Config path does not exist: ${location}"
            }
        }

        detectedYamlConfigs
    }
}
