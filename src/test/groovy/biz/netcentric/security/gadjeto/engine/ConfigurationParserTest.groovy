package biz.netcentric.security.gadjeto.engine

import biz.netcentric.security.gadjeto.configuration.ConfigurationParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

class ConfigurationParserTest {

    static final String RESOURCE_FOLDER = "src/test/resources/configparserTest-phases"

    ConfigurationParser parser

    @BeforeEach
    void beforeEach() {
        this.parser = new ConfigurationParser()
    }

    @Test
    void loadAllFromConfig_singleFile() {
        File file = loadFile("${RESOURCE_FOLDER}/info-phase.yaml")

        List<ConcretePhase> phases = parser.loadAllFromConfig([file])

        assertEquals 1, phases.size()

        ConcretePhase phaseUnderTest = phases.get(0)
        assertEquals "info", phaseUnderTest.getPhaseId()
        assertEquals "Information Phase", phaseUnderTest.getName()
        assertEquals 3, phaseUnderTest.getAll().size()
    }

    File loadFile(String path){
        def location = getClass().getResource(path).toString();
        new File(path)
    }
}
