package biz.netcentric.security.gadgeto.engine.cmd


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class CommandlineProcessRunnerTest {

    CommandlineProcessRunner runner

    @BeforeEach
    void beforeEach() {
        this.runner = new CommandlineProcessRunner()
    }

    @Test
    void testRunPipedGrepOnMultilineStatement() {
        //String command = "nmap -F --open -Pn netcentric.biz | grep tcp"
        String command = 'echo "found the following ports \ntcp port 22 \ntcp port 43 \n \nudp port 2222" | grep 22'


        List<String> results = []
        runner.runProcess("echo", command, { Process process ->
            process.in.eachLine { String line ->
                results.add line
            }

            // just a dummy
            return new CmdToolResponse(strError: new StringBuilder(), strOut: new StringBuilder())
        })

        Assertions.assertEquals "tcp port 22 ", results.get(0)
        Assertions.assertEquals "udp port 2222", results.get(1)
    }
}
