import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class SwayIPCTest {

    private static SwayIPC swayIPC;

    @BeforeAll
    static void setUp() {
        swayIPC = new SwayIPC();
    }

    @AfterEach
    void tearDown() {
        swayIPC.closeConnection();
    }

    @Test
    void connect() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::closeConnection);
    }

    @Test
    void sendCommand() {
        assertDoesNotThrow(() -> {
            swayIPC.connect(SwayIPC.getSwaysock());
        });
        assertDoesNotThrow(() -> swayIPC.sendCommand("floating toggle"));
        String response = swayIPC.sendCommand("floating toggle");
        assertThat(response, containsString("true"));
    }

    @Test
    void getWorkspaces() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getWorkspaces);
        assertThat(swayIPC.getWorkspaces(), containsString("\"type\": \"workspace\","));
    }

    @Test
    void getOutputs() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getOutputs);
        assertThat(swayIPC.getOutputs(), containsString("\"type\": \"output\","));
    }

    //    @Test
    void getTree() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getTree);
        assertThat(swayIPC.getTree(), containsString("\"type\": \"root\","));
    }

    @Test
    void getMarks() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getMarks);
        assertThat(swayIPC.getMarks(), containsString("[ ]"));
    }

//    @Test
//    void getBarConfig() {
//        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
//        assertDoesNotThrow(() -> swayIPC.getBarConfig("bar-0"));
//        assertThat(swayIPC.getBarConfig("bar-0"), containsString("\"mode\": \"dock\","));
//    }

    @Test
    void getVersion() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getVersion);
        assertThat(swayIPC.getVersion(), containsString("\"variant\": \"sway\","));
    }

    @Test
    void getBindingModes() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getBindingModes);
        assertThat(swayIPC.getBindingModes(), containsString("default"));
    }

    @Test
    void getConfig() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getConfig);
        assertThat(swayIPC.getConfig(), containsString("\"config\": "));
    }

    @Test
    void getInputs() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getInputs);
        assertThat(swayIPC.getInputs(), containsString("\"type\": \"keyboard\""));
    }

    @Test
    void getSeats() {
        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
        assertDoesNotThrow(swayIPC::getSeats);
        assertThat(swayIPC.getSeats(), containsString("\"name\": \"seat0\""));
    }

//        @Test
//    void subscribe() {
//        assertDoesNotThrow(() -> swayIPC.connect(SwayIPC.getSwaysock()));
//        assertDoesNotThrow(() -> swayIPC.lolLol(new String[]{"workspace"}));
//        swayIPC.sendCommand("workspace test");
//        assertThat(swayIPC.sendCommand("get_workspaces"), containsString("\"name\": \"test\""));
//    }
}