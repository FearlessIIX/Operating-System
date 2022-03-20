package Control;

import java.awt.event.KeyEvent;
import java.util.Stack;

/**
 * A Stream Containing a Stack of KeyEvents
 */
public class KeyStream {
    /**
     * The Stack of Events for this Stream, can be accessed by multiple threads at once
     */
    private final Stack<KeyEvent> _stream = new Stack<>();

    /**
     * Removes the latest Event from the Stream and returns it
     * @return The latest Event
     */
    public synchronized KeyEvent getNext() { return _stream.pop(); }

    /**
     * If the Stream contains any unconsumed Events
     * @return True or False
     */
    public synchronized boolean hasNext() { return _stream.size() > 0; }

    /**
     * Stores an Event at the front of queue, making it ready for consumption
     * @param event The Event to queue
     */
    public synchronized void sendEvent(KeyEvent event) { _stream.push(event); }
}
