package biz.netcentric.security.gadgeto.engine.cmd

import groovy.util.logging.Slf4j

import static biz.netcentric.security.gadgeto.engine.cmd.CmdSupport.handleError

@Slf4j
class CommandlineProcessRunner {

    CmdToolResponse runProcess(String exec, String command, Closure responseHandler){
        def proc = null
        try {
            proc = executeInShell(command)
            return responseHandler(proc)
        } catch (Exception ex) {
            handleError(true, exec, command, ex)
        }finally{
            if(proc != null && proc.out != null && proc.out instanceof OutputStream){
                log.debug "Closing proc"
                proc.out.close()
            }
        }

        return new CmdToolResponse()
    }

    boolean runBooleanResultProcess(String exec, String command, Closure responseHandler){
        def proc = null
        try {
            proc = executeInShell(command)
            return responseHandler(proc)
        } catch (Exception ex) {
            handleError(true, exec, command, ex)
        }finally{
            if(proc != null && proc.out != null && proc.out instanceof OutputStream){
                log.debug "Closing proc"
                proc.out.close()
            }
        }

        return false
    }

    Process executeInShell(String fullStatement){
        Process proc
        String[] cmd = ["/bin/sh", "-c", fullStatement ]
        proc = Runtime.getRuntime().exec(cmd)
        proc.waitFor()

        return proc
    }
}
