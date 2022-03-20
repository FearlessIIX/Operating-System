package Structure;

import java.time.ZonedDateTime;

public class Logger {
    public static void logMessage(String message) {
        ZonedDateTime dateTime = ZonedDateTime.now();
        String time = dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond() + "::" + dateTime.getNano();
        toDebugStream(message, time);
    }
    private static void toDebugStream(String message, String time) { System.out.println(time + "  " + message); }
}
