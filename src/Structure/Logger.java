package Structure;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");

    public static void logMessage(String message) {
        String time = ZonedDateTime.now().format(formatter);
        toDebugStream(message, time);
    }
    private static void toDebugStream(String message, String time) { System.out.println(time + "  " + message); }
}
