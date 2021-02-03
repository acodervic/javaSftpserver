package mrRobot;

/**
 * This Class introduced how to handle Ctrl+C in your program <BR>
 * You can close file descriptor, close remote connection , release memory and
 * so on when Ctrl C happens.
 *
 * @author Ahui Wang
 *
 */
public class CtrlCExit implements Runnable {
    private boolean bExit = false;

    private class ExitHandler extends Thread {
        public ExitHandler() {
            super("Exit Handler");
        }

        public void run() {
            System.out.println("已经结束进程!");
            bExit = true;
        }
    }

    public CtrlCExit() {
        Runtime.getRuntime().addShutdownHook(new ExitHandler());
    }

    public void run() {
        while (!bExit) {
            // Do some thing
        }
        System.out.println("Exit OK");
    }

}