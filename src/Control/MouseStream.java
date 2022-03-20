package Control;

import java.util.Stack;
import java.awt.event.MouseEvent;

/**
 * A Stream containing a Stack of MouseEvents
 */
public class MouseStream {
    /**
     * The Stack of Events for this Stream, can be accessed by multiple threads at once
     */
    private final Stack<MouseEvent> _stream = new Stack<>();

    /**
     * Removes the latest Event from the Stream and returns it
     * @return The latest Event
     */
    public synchronized MouseEvent getNext() { return _stream.pop(); }

    /**
     * If the Stream contains any unconsumed Events
     * @return True or False
     */
    public synchronized boolean hasNext() { return _stream.size() > 0; }

    /**
     * Stores an Event at the front of queue, making it ready for consumption
     * @param event The Event to queue
     */
    public synchronized void sendEvent(MouseEvent event) { _stream.push(event); }
}
