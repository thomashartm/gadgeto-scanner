package biz.netcentric.security.gadjeto.engine.cmd

import org.apache.commons.lang3.StringUtils

enum CmdLayout {

    HEADLINE("\u001B[34m"),
    OKBLUE("\u001B[34m"),
    OKGREEN("\u001B[32m"),
    WARNING("\u001B[34m"),
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