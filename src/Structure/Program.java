package Structure;

import Control.KeyStream;
import Control.MouseStream;

import javax.swing.*;

/**
 * Base class for all Programs to extend from, contains the logic for responsive apps within the OS
 */
public abstract class Program extends JPanel {
    protected final int width;
    protected final int height;
    protected final String name;

    /**
     * The KeyStream that the Program will listen to
     */
    private final KeyStream _key_stream = new KeyStream();

    /**
     * The MouseStream that the Program will listen to
     */
    private final MouseStream _mouse_stream = new MouseStream();

    /**
     * An internal flag for the _manager Method, Manager should terminate when this is set to true
     */
    protected boolean exit = false;

    public Program(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    /**
     * Terminates the Method _manager, causing it to lose status as a monitor
     */
    public void terminate() { this.exit = true; }

    /**
     * <strong>This is the base controller for all Programs</strong><br>
     * In order for a Program to function it must implement this method and do the following
     * <ol>
     *     <li>Read Events from KeyStream and forward them to the extending Program</li>
     *     <li>Read Events from MouseStream and forward them to the extending Program</li>
     *     <li>Implement a <strong>backup</strong> exit key-bind, my standard is [ctrl + alt + '-']</li>
     *     <li><strong>It is also suggested that _manager can be terminated by setting <code>exit</code> to true</strong></li>
     * </ol>
     */
    protected abstract void _manager();

    /**
     * Returns the Mutual KeyStream for this Program
     * @return The Program's KeyStream
     */
    public synchronized KeyStream getKeyStream() { return _key_stream; }

    /**
     * Returns the Mutual MouseStream for this Program
     * @return The Program's MouseStream
     */
    public synchronized MouseStream getMouseStream() { return _mouse_stream; }

    /**
     * If the _manager Method has been terminated yet
     * @return True or False
     */
    public boolean isTerminated() { return this.exit; }

    /**
     * Returns the name of this program [set by extending object], used for debugging purposes
     * @return The name of the program
     */
    public String getProgramName() { return this.name; }
}