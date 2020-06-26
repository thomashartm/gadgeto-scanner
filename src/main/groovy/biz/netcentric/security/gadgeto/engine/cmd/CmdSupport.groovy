package biz.netcentric.security.gadgeto.engine.cmd


import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils

@Slf4j
class CmdSupport {

    static boolean verifyIfInstalled(String executable){
        def shellCommand = "which ${executable}"

        boolean result = false
        try{
            CommandlineProcessRunner runner = new CommandlineProcessRunner()
            result = runner.runProcess("which", shellCommand, {Process proc ->

                String output = proc.text
                log.debug(output)

                return !StringUtils.contains(output, "not found") && StringUtils.contains(output, executable)
            })

        }catch(Exception ex) {
            log.error("Unable to run tool installation check for ${executable} ", ex)
        }

        return result
    }

    static void handleError(boolean debugEnabled, String exec, String command, Exception ex) {
        printError exec, command

        if (debugEnabled) {
            log.error ex.toString(), ex
        } else {
            log.error "[ERROR]: ${ex.toString()}"
        }
    }

    static void printMessage(CmdLayout type, String message) {
        println type.format(message)
    }

    static void printMessage(String name, String message) {
        println CmdLayout.OKBLUE.format("[TOOL] ${name} ::: ${message}")
    }

    static void printError(String name, String message) {
        println CmdLayout.FAIL.format("[ERROR] ::: ${message}")
    }

    static void emptyLine(){
        println ""
    }
}