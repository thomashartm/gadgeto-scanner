package biz.netcentric.security.gadgeto.engine.cmd


import groovy.util.logging.Slf4j

@Slf4j
class CmdSupport {

    static void printMessage(CmdLayout type, String message) {
        println type.format(message)
    }

    static void printMessage(String name, String message) {
        println CmdLayout.HEADLINE.format("[TOOL] ${name} ::: ${message}")
    }

    static void printError(String name, String message) {
        println CmdLayout.FAIL.format("[ERROR] ::: ${message}")
    }

    static void emptyLine(){
        println ""
    }
}