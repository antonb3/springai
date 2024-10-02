package feeltheagi.springai;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
    public static void logException(Exception e) {
        log.info("===Exception begin===");
        log.info(e.getLocalizedMessage());
        for (StackTraceElement s : e.getStackTrace()) {
            if (s.getClassName().startsWith("feeltheagi")) {
                log.info(s.getClassName() + " - " + s.getMethodName() + " - " + s.getLineNumber());
            }
        }
        log.info("===Exception end===");
    }
}
