package biz.netcentric.security.gadgeto.engine.cmd

import org.apache.commons.lang3.StringUtils

enum CmdLayout {

    NC("\u001B[35m"),
    HEADLINE("\u001B[37m"),
    HEADLINE_BOLD("\u001B[1;37m"),
    OKBLUE("\u001B[34m"),
    OKGREEN("\u001B[32m"),
    WARNING("\u001B[33m"),
    FAIL("\u001B[31m")

    String cmdColor

    String reset

    CmdLayout(String cmdColor){
        this.cmdColor = cmdColor
        this.reset = "\u001B[0m"
    }

    String format(String value){
        StringUtils.join(cmdColor, value, this.reset)
    }

    String breakMessage(String message){
        StringUtils.join(cmdColor, message, this.reset)
    }
}