package biz.netcentric.security.gadjeto.model

import biz.netcentric.security.gadjeto.configuration.ConfigurationParser
import org.junit.jupiter.api.Test

class ModuleParserTest {

    @Test
    void parse() {
        def parser = new ConfigurationParser()
        parser.parse()
    }
}