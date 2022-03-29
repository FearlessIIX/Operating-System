package Programs;

import Structure.Logger;
import Structure.Program;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Start extends Program {
    /**
     * A pre-initialized Font for the System font. Should be the default Font for writing
     */
    private final Font control_font = new Font(Font.MONOSPACED, Font.PLAIN, 30);

    /**
     * Static member, used to count repeat initializations of this Program, for debuggging purposes
     */
    private static int count = 1;

    /**
     * The line that the user is currently adding characters to
     */
    private int line_pointer = 1;

    /**
     * The amount of Characters the Carrot is in the current line
     */
    private int char_pointer = 0;

    /**
     * Array of lines that are being shown on the screen, does not represent everything, only what the Screen sees
     */
    private String[] ScreenLines = new String[this.height / 30];

    /**
     * All the lines that the current Window contains, including past history. For scrolling purposes
     */
    private final ArrayList<String> lines = new ArrayList<>();

    public Start(int width, int height) {
        super(width, height, "Start"+count);
        count++;

        lines.add("");
    }

    @Override
    protected void _manager() {
        Logger.logMessage(" -- Starting the _manager Thread for " + getProgramName());

        // Starting the "Manager" Thread
        new Thread(()-> {
            Logger.logMessage(" -- _manager Thread for " + getProgramName() + " started");

            // Condition allows for Programs to be terminated externally if needed
            while (!(isTerminated())) {
                // If there is at least one KeyEvent and at least one MouseEvent ready for consumption
                if (getKeyStream().hasNext() && getMouseStream().hasNext()) {

                    // Consuming a single KeyEvent
                    KeyEvent key_event = getKeyStream().getNext();

                    // If the result of the keyEvent was termination of this program
                    if (consumeKeyEvent(key_event))
                        break;

                    // Consuming a single MouseEvent
                    MouseEvent mouse_event = getMouseStream().getNext();
                    consumeMouseEvent(mouse_event);
                }
                else if (getKeyStream().hasNext()) {
                    // Consuming a single KeyEvent
                    KeyEvent key_event = getKeyStream().getNext();

                    // If the result of the keyEvent was termination of this program
                    if (consumeKeyEvent(key_event))
                        break;
                }
                else if (getMouseStream().hasNext()) {
                    // Consuming a single MouseEvent
                    MouseEvent mouse_event = getMouseStream().getNext();
                    consumeMouseEvent(mouse_event);
                }
            }

            Logger.logMessage(" -- _manager Thread for " + getProgramName() + " ended");
            terminate();
        }, "Structure." + getProgramName() + "._manager").start();

        // Starting the "Carrot Animator" Thread
        new Thread(() -> {
            // Condition allows for Programs to be terminated externally
            while (!isTerminated()) {
                // Grabs meta information for the Carrot every loop
                int cursor = char_pointer;
                int line = line_pointer;
                try {
                    // Turns the Carrot on
                    toggleCursor(true, cursor, line);
                    // Sleeps for 1 ms 500 times
                    for (int i = 0; i < 500; i++) {
                        Thread.sleep(1);

                        // Checks if any Carrot meta-data has changed
                        if (cursor != char_pointer || line != line_pointer) {
                            // Turns off the old Carrot, and Changes the Carrot meta-data to reflect the new location
                            toggleCursor(false, cursor, line);
                            cursor = char_pointer; line = line_pointer;

                            // Turns on the new Carrot, and resets the 500ms loop back to 0
                            toggleCursor(true, cursor, line);
                            i = 0;
                        }
                    }

                    // Turns the Carrot off, then waits for 100ms
                    toggleCursor(false, cursor, line);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, getProgramName() + ".carrotAnimator").start();
    }

    private boolean consumeKeyEvent(KeyEvent event) {
        // Integer value for event, if Modifiers are present, integer will read as 65535
        int code = event.getKeyChar();
        String modifiers = InputEvent.getModifiersExText(event.getModifiersEx());   // Contains mods separated by '+'

//        Logger.logMessage(event.getKeyCode()+"  |  " + (int)event.getKeyChar()+"  |  " + event.getKeyChar());

        // Minus Key Event, contains the Program termination KeyBind detection
        if (code == 65535 && event.getKeyCode() == 45)
            if (modifiers.contains("Alt") && modifiers.contains("Ctrl"))
                return true;
        // Detection for Arrow Keys (Specifically left and right)
        if (code == 65535 && event.getKeyCode() == 37 || event.getKeyCode() == 39) {
            if (event.getKeyCode() == 39) {
                if (lines.get(line_pointer-1).length() > char_pointer) char_pointer++;
            }
            if (event.getKeyCode() == 37) {
                if (char_pointer > 0) char_pointer--;
            }
        }

        // Backspace Key Event
        if (code == 8 && lines.size() >= line_pointer) {
            String current_line = lines.get(line_pointer -1);
            // If there are characters left in current line
            if (current_line.length() > 0 && char_pointer > 0) {
                doBackspace(char_pointer, line_pointer);

                // Gets rid of the Character at the end of the line, then moves the Character pointer back by one
                lines.set(line_pointer -1, current_line.substring(0, current_line.length()-1));
                char_pointer--;
                Logger.logMessage(char_pointer+": Backspaced");
            }
        }

        // Enter Key Event
        if (code == 10) {
            // Sets the line pointer up by one, then sets the Character pointer to Zero. Adds a new line
            line_pointer++;
            char_pointer = 0;
            lines.add("");
        }

        // If the code is within the ASCII range for Printable characters
        if (code > 31 && code < 127) {
            char ch = (char)code;

            // Adds the Character to the end of the line, then sets the Character Pointer up by One
            String current_line = lines.get(line_pointer -1);

            Logger.logMessage(char_pointer+": Insert Character");
            if (char_pointer == 0) lines.set(line_pointer-1, ch + current_line);
            else if (char_pointer == current_line.length()) lines.set(line_pointer -1, current_line + ch);
            else {
                String pre = current_line.substring(0, char_pointer);
                String post = current_line.substring(char_pointer);

                lines.set(line_pointer-1, pre + ch + post);

                for (int i = 0; i <= post.length(); i++) {
                    doBackspace((pre.length() +1)+i, line_pointer);
                }
            }

            char_pointer++;
        }

        for (int i = 0; i < lines.size(); i++) {
            paintLine(lines.get(i), i+1);
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

    private void paintLine(String line, int line_num) {

        Graphics g = getGraphics();
        g.setColor(Color.white);
        g.setFont(control_font);
        if (line_num == 1) g.drawString(line, 0, 20);
        else g.drawString(line, 0, 20+((line_num-1)*30));
    }

    //TODO: when we change this to allow in line backspacing, we should change str to base off of char pointer instead
    // when backspacing mid line we will get rid of all char past char pointer, then add concat substring of char
    // before pointer, and char one after pointer
    private void doBackspace(int ch, int line) {
        Graphics g = getGraphics();
        g.setColor(Color.black);

        g.fillRect(18*(ch-1), 30*(line-1), 18, 30);
    }

    /**
     *
     * Either draws or un-draws the Cursor on the screen
     * @param state Whether the Cursor should be drawn or un-drawn
     * @param cursor The location of the Cursor in the line
     * @param line The line that the Cursor is on
     */
    private void toggleCursor(boolean state, int cursor, int line) {
        Graphics g = getGraphics();
        if (state) g.setColor(Color.gray.brighter());
        else g.setColor(Color.black);
        // 18: line_width  |  30: line_height
        g.fillRect(18*(cursor), 30*(line-1), 2, 30);
    }
}
