package Control;

import Structure.Program;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Implementation of KeyListener, hands out KeyStreams to its Monitor
 */
public class Keyboard implements KeyListener {

    /**
     * The program that currently has the Monitor of Keyboard
     */
    private Program _monitor;

    /**
     * Passes in initial value for Keyboard Monitor
     * @param monitor The Initial Monitor
     */
    public Keyboard(Program monitor) { setMonitor(monitor); }

    /**
     * Dereferences old Monitor by setting the new Monitor
     * @param new_monitor The new Monitor to be set
     */
    public void setNewMonitor(Program new_monitor) { setMonitor(new_monitor); }

    private void setMonitor(Program monitor) {
        _monitor = monitor;
        System.out.println(" -- Keyboard Monitor transferred to -> " + monitor.getProgramName());
    }

    @Override
    public void keyTyped(KeyEvent ignored) { }
    @Override
    public void keyPressed(KeyEvent ignored) { }

    @Override
    public void keyReleased(KeyEvent e) {
        _monitor.getKeyStream().sendEvent(e);
    }
}