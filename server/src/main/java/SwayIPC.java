public class SwayIPC {

    static {
        System.loadLibrary("swayipc");
    }

    public static String getSwaysock() {
        return System.getenv("SWAYSOCK");
    }

    public native void connect(String socketPath);

    public native String sendCommand(String command);

    public native String getWorkspaces();

    public native String getOutputs();

    public native String getTree();

    public native String getMarks();

    public native String getVersion();

    public native String getBindingModes();

    public native String getConfig();

    public native String getInputs();

    public native String getSeats();

    public native boolean lolLol(String[] e);

    public native int closeConnection();
}
