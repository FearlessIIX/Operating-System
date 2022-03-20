package Control;

import Structure.Program;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * Extension of MouseInputAdapter, hands out MouseStreams to its Monitor
 */
public class Mouse extends MouseInputAdapter {

    /**
     * The program that currently has the Monitor of Mouse
     */
    private Program _monitor;

    /**
     * Passes in initial value for Keyboard Monitor
     * @param monitor The Initial Monitor
     */
    public Mouse(Program monitor) { setMonitor(monitor); }

    /**
     * Dereferences old Monitor by setting the new Monitor
     * @param new_monitor The new Monitor to be set
     */
    public void setNewMonitor(Program new_monitor) { setMonitor(new_monitor); }

    private void setMonitor(Program monitor) {
        _monitor = monitor;
        System.out.println(" -- Mouse Monitor transferred to -> " + monitor.getProgramName());
    }

    @Override
    public void mousePressed(MouseEvent ignored) { }
    @Override
    public void mouseReleased(MouseEvent ignored) { }

    @Override
    public void mouseClicked(MouseEvent e) {
        _monitor.getMouseStream().sendEvent(e);
    }
}
