package Structure;

import Control.Keyboard;
import Control.Mouse;
import Programs.Start;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Main extends JFrame {
    private final int width;
    private final int height;
    private final Keyboard keyboard;
    private final Mouse mouse;
    private final Stack<Program> program_stack = new Stack<>();

    public Main() {
        Logger.logMessage(" -- Initializing Main");

        // Setting the Frame to fullscreen mode
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        setResizable(false);
        this.width = getWidth();
        this.height = getHeight();

        Logger.logMessage(" -- Creating initial Start Program");

        Program start = new Start(this.width, this.height);
        Container container = getContentPane();
        container.add(start);

        Logger.logMessage(" -- Program " + start.getProgramName() + " created");

        addKeyListener(this.keyboard = new Keyboard(start));
        addMouseListener(this.mouse = new Mouse(start));

        Logger.logMessage(" -- Setting final window settings");

        setTitle("Foxy-OS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        Logger.logMessage(" -- Window finalized\n");

        start._manager();
        program_stack.push(start);

        new Thread(() -> {
            while (program_stack.size() > 0) {
                if (program_stack.peek().isTerminated()) {
                    Program program = program_stack.pop();
                    Logger.logMessage(" -- Terminated Program -> " + program.getProgramName());

                    if (program_stack.size() > 0) {
                        program = program_stack.peek();

                        Logger.logMessage(" -- Giving Precedence to -> " + program.getProgramName());

                        keyboard.setNewMonitor(program);
                        mouse.setNewMonitor(program);
                    }
                }
            }

            Logger.logMessage(" -- All Programs have been Terminated");
            System.exit(0);
        }, "Structure.Main.Main").start();

    }

    public static void main(String[] args) { SwingUtilities.invokeLater(Main::new); }
}
