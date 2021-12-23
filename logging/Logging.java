package by.epamtc.stanislavmelnikov.logging;

import java.util.logging.Logger;

public class Logging {
    private static final Logger logger =
            Logger.getLogger(Logging.class.getName());

    public static Logger getLogger() {
        return logger;
    }

}
