package Programs;

import Structure.Program;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Start extends Program {

    private static int count = 1;

    public Start(int width, int height) {
        super(width, height, "Start"+count);
        count++;
    }

    @Override
    protected void _manager() {
        System.out.println(" -- Starting the _manager Thread for " + getProgramName());

        new Thread(()-> {
            System.out.println(" -- _manager Thread for " + getProgramName() + " started");
            while (!(isTerminated())) {
                // If there is at least one KeyEvent and at least one MouseEvent ready for consumption
                if (getKeyStream().hasNext() && getMouseStream().hasNext()) {

                    // Consuming a single KeyEvent
                    KeyEvent key_event = getKeyStream().getNext();
                    if (consumeKeyEvent(key_event))
                        break;

                    // Consuming a single MouseEvent
                    MouseEvent mouse_event = getMouseStream().getNext();
                    consumeMouseEvent(mouse_event);
                }
                else if (getKeyStream().hasNext()) {
                    // Consuming a single KeyEvent
                    KeyEvent key_event = getKeyStream().getNext();
                    if (consumeKeyEvent(key_event))
                        break;
                }
                else if (getMouseStream().hasNext()) {
                    // Consuming a single MouseEvent
                    MouseEvent mouse_event = getMouseStream().getNext();
                    consumeMouseEvent(mouse_event);
                }
            }

            System.out.println(" -- _manager Thread for " + getProgramName() + " ended");
            terminate();
        }, "Structure." + getProgramName() + "._manager").start();
    }

    private boolean consumeKeyEvent(KeyEvent event) {
        int code = event.getKeyCode();

        if (code == 45) {
            // Window Escape KeyBind
            if (event.isAltDown() && event.isControlDown())
                return true;
        }
        return false;
    }

    private void consumeMouseEvent(MouseEvent event) {

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, this.width, this.height);
    }
}
