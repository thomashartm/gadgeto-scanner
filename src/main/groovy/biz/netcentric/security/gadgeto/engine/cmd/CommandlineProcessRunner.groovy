package biz.netcentric.security.gadgeto.engine.cmd

import groovy.util.logging.Slf4j

import static biz.netcentric.security.gadgeto.engine.cmd.CmdSupport.*

@Slf4j
class CommandlineProcessRunner {

    def runProcess(String exec, String command, Closure responseHandler){
        def proc = null
        def result = null
        try {
            proc = command.execute()
            result = responseHandler(proc)
        } catch (Exception ex) {
            handleError(true, exec, command, ex)
        }finally{
            if(proc != null && proc.out != null && proc.out instanceof OutputStream){
                log.debug "Closing proc"
                proc.out.close()
            }
        }
        return result
    }
}
