package biz.netcentric.security.gadgeto.model


import org.junit.jupiter.api.Test

import static org.junit.Assert.assertEquals

class ModuleDefinitionTest {

    @Test
    void testSeverityLevel() {
        ModuleDefinition definition = new ModuleDefinition(name: "nmap scan", severity: Severity.CRITICAL.getRank())
        assertEquals Severity.CRITICAL, definition.getSeverityLevel()

    }
}