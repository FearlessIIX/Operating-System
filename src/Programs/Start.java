package Programs;

import Structure.Logger;
import Structure.Program;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Start extends Program {

    private final Font control_font = new Font(Font.MONOSPACED, Font.PLAIN, 30);
    private static int count = 1;
    private int draw_line = 1;
    private int chars_past = 0;
    private String[] ScreenLines = new String[this.height / 30];
    private final ArrayList<String> lines = new ArrayList<>();

    public Start(int width, int height) {
        super(width, height, "Start"+count);
        count++;

        lines.add("");

        Logger.logMessage(ScreenLines.length+"");
    }

    @Override
    protected void _manager() {
        Logger.logMessage(" -- Starting the _manager Thread for " + getProgramName());

        new Thread(()-> {
            Logger.logMessage(" -- _manager Thread for " + getProgramName() + " started");
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

            Logger.logMessage(" -- _manager Thread for " + getProgramName() + " ended");
            terminate();
        }, "Structure." + getProgramName() + "._manager").start();

        new Thread(() -> {
            while (!isTerminated()) {
                int cursor = chars_past;
                int line = draw_line;
                try {
                    Thread.sleep(100);
                    toggleCursor(true, cursor, line);
                    Thread.sleep(100);
                    toggleCursor(false, cursor, line);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, getProgramName() + ".carrotAnimator").start();
    }

    private boolean consumeKeyEvent(KeyEvent event) {
        // Code for printing the character, is modified when there are modifiers present
        int code = event.getKeyChar();
        String modifiers = InputEvent.getModifiersExText(event.getModifiersEx());

        if (code == 65535 && event.getKeyCode() == 45)
            if (modifiers.contains("Alt") && modifiers.contains("Ctrl"))
                return true;

        if (code == 8 && lines.size() >= draw_line) {
            String current_line = lines.get(draw_line-1);
            if (current_line.length() > 0) {
                doBackspace(current_line, draw_line);
                lines.set(draw_line-1, current_line.substring(0, current_line.length()-1));
                chars_past--;
            }
            else {
                if (lines.size() - 1 > 0) {
                    draw_line--;
                    chars_past = lines.get(draw_line-1).length();

                    lines.remove(draw_line);
                }
            }
        }

        if (code == 10) {
            draw_line++;
            chars_past = 0;
            lines.add("");
        }

        if (code > 31 && code < 127) {
            char ch = (char)code;

            String current_line = lines.get(draw_line-1);
            lines.set(draw_line-1, current_line + ch);

            chars_past++;
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
    private void doBackspace(String str, int line) {
        Graphics g = getGraphics();
        g.setColor(Color.black);

        g.fillRect(18*(str.length()-1), 30*(line-1), 18, 30);
    }
    private void toggleCursor(boolean state, int cursor, int line) {
        Graphics g = getGraphics();
        if (state) g.setColor(Color.gray.brighter());
        else g.setColor(Color.black);

        g.fillRect(18*(cursor), 30*(line-1), 2, 30);
    }
}
