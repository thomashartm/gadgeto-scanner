package biz.netcentric.security.gadjeto

import biz.netcentric.security.gadjeto.configuration.ConfigLocationFinder
import biz.netcentric.security.gadjeto.configuration.ConfigurationParser
import biz.netcentric.security.gadjeto.engine.ConcretePhase
import biz.netcentric.security.gadjeto.engine.Phase
import groovy.cli.picocli.CliBuilder
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils

@Slf4j
class GadjetoScanner {

    ConfigurationParser configParser

    ConfigLocationFinder configFinder

    Map<String, Phase> phases = [:]

    GadjetoScanner(){
        this.configFinder = new ConfigLocationFinder()
        this.configParser = new ConfigurationParser()
    }

    static void main(String[] args) {

        CliBuilder cli = createCommandLineOptions()
        def options = cli.parse(args)
        assert options // would be null (false) on failure
        // only the URL is mandatory.

        if (!options.getProperty('list')) {
            assert options.getProperty('url')
            options.arguments()
        }

        def gadjetoScanner = new GadjetoScanner()
        gadjetoScanner.run(options)
    }

    private static CliBuilder createCommandLineOptions() {
        def cli = new CliBuilder()
        cli.p(longOpt: 'phase', args: 1, argName: 'phase', 'Run scan process phase')
        cli.c(longOpt: 'config', args: 1, argName: 'config', 'Load configurations from')
        cli.l(longOpt: 'list', args: 0, argName: 'list', 'List all scan phases by name')
        cli._(longOpt: 'url', args: 1, argName: 'URL', 'Target URL')
        cli
    }

    private run(def options) {
        final String configuration = options.getProperty('config')
        List<File> configurations = this.getConfigDirectories(configuration)
        loadPhases(configurations)

        if(options.getProperty('list')){
            phases.each {phase ->
                println phase.getKey()
            }
            return
        }

        String phaseId = options.getProperty('phase')
        assert (phaseId != null)
        assert (this.phases.containsKey(phaseId))

        Phase phaseToExecute = this.phases.get(phaseId)
        log.info "Starting selected phase ${phaseToExecute.getPhaseId()}"
        String targetUrl = options.getProperty('url')
        phaseToExecute.execute(targetUrl)
        log.info "Finished"
    }

    private void loadPhases(List<File> configurations){
        List<Phase> loadedPhaseConfigs = this.configParser.loadAllFromConfig(configurations)
        loadedPhaseConfigs.each {phase ->
            this.phases.put phase.getPhaseId(), phase
        }
    }

    private List<File> getConfigDirectories(String configLocation){

        if(StringUtils.isEmpty(configLocation)){
            return this.configFinder.getConfigFiles([configLocation])
        }else{
            log.info "No config property provided. Falling back to to configuration location: config"
            List<String> files = []
            files.add("config")
            return this.configFinder.getConfigFiles(files)
        }
    }
}