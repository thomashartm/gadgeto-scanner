import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.DEBUG

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative [%thread] - %msg%n"
    }
}

def USER_HOME = System.getProperty("user.home");
// add a status message regarding USER_HOME
addInfo("USER_HOME=${USER_HOME}")

appender("FILE", FileAppender) {
    // make use of the USER_HOME variable
    file = "${USER_HOME}/myApp.log"
    encoder(PatternLayoutEncoder) {
        Pattern = "%d %level %thread %mdc %logger - %m%n"
    }
}

//root(DEBUG, ["FILE"])
root(INFO, ["CONSOLE"])