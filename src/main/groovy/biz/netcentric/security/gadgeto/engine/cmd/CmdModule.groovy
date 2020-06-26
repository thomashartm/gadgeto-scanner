package biz.netcentric.security.gadgeto.engine.cmd

import biz.netcentric.security.gadgeto.engine.Module
import biz.netcentric.security.gadgeto.model.ModuleDefinition
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils


@Slf4j
class CmdModule implements Module{

    ModuleDefinition moduleDefinition

    boolean debugEnabled

    @Override
    def run(String target) {
        def failPatterns = getFailPattern()
        def positivePatterns = getPositiveResponsePatterns()

        def processHandler = { Process proc ->
            StringBuilder responseBuilder = new StringBuilder()
            StringBuilder errorBuilder = new StringBuilder()

            proc.in.eachLine { line ->
                if(debugEnabled){
                    println line
                }

                boolean failed = failPatterns.stream()
                        .anyMatch(pattern -> isMatch(pattern, line))

                if(failed){
                    errorBuilder.append "[STATUS] Module failed: ${line}"
                }else{
                    // check for success now
                    boolean success = positivePatterns.stream()
                            .anyMatch(pattern -> isMatch(pattern, line))

                    if(success){
                        responseBuilder.append "[STATUS] Positive match ::: ${line}"
                    }
                }
            }

            return new CmdToolResponse(strError: errorBuilder, strOut: responseBuilder)
        }

        CmdToolResponse response = execute(target, processHandler)

        if(response.hasErrors()){
            CmdSupport.printMessage CmdLayout.WARNING, response.getStrError().toString()
        }else if(response.hasOutput()){
            CmdSupport.printMessage CmdLayout.OKGREEN, response.getStrOut().toString()
        }else{
            CmdSupport.printMessage CmdLayout.OKBLUE,  "[STATUS] OK"
        }

        CmdSupport.printMessage moduleDefinition.getExecutable(), "finished"

        // add empty line
        CmdSupport.emptyLine()
    }

    def execute(String target, Closure process) {
        def exec = moduleDefinition.getExecutable()
        def command = "${exec} ${moduleDefinition.getArgs()} ${target}"

        CmdSupport.printMessage exec, command

        def proc = null
        try {
            proc = command.execute()
            CmdToolResponse result = process(proc)
            return result
        } catch (Exception ex) {
            CmdSupport.handleError(true, exec, command, ex)
        }finally{
            if(proc != null && proc.out != null && proc.out instanceof OutputStream){
                log.debug "Closing proc"
                proc.out.close()
            }
        }

        // replace upper statement with
        //CommandlineProcessRunner runner = new CommandlineProcessRunner()
        //CmdToolResponse result = runner.runProcess(exec, command, process)

        // empty default response
        return new CmdToolResponse()
    }



    boolean isMatch(String pattern, String value){
        boolean result = false
        // check if the pattern is a regex if not me match using contains
        if (StringUtils.isNotBlank(pattern)) {
            if (pattern.startsWith("RE:") && pattern.size() > 2) {
                String regex = pattern.substring(3)
                result = (value =~ regex).find()
            } else {
                result = value.contains(pattern)
            }
        }

        result
    }

    List getFailPattern(){
        List<String> responsePattern = this.moduleDefinition.getFailedResponse()
        responsePattern != null ? responsePattern : []
    }

    List getPositiveResponsePatterns(){
        List<String> responsePattern = this.moduleDefinition.getPositiveResponse()
        responsePattern != null ? responsePattern : []
    }
}