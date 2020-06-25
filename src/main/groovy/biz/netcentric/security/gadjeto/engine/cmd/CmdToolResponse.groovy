package biz.netcentric.security.gadjeto.engine.cmd

class CmdToolResponse {

    StringBuilder strOut

    StringBuilder strError

    boolean hasErrors(){
        strError != null && strError.size() > 0
    }

    boolean hasOutput(){
        strOut != null && strOut.size() > 0
    }
}
