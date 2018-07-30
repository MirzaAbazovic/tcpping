package ba.programiraj.tcpping;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NodeSettingsTest {

    private final String IP_ADDRESS = "192.168.0.1";
    private final int PORT = 9900;
    private final String PORT_STR = Integer.toString(PORT);
    private final int SENDING_RATE = 30;
    private final String SENDING_RATE_STR = Integer.toString(SENDING_RATE);
    private final String HOSTNAME = "kompB";
    private final int MSG_SIZE = 1000;
    private final String MSG_SIZE_STR = Integer.toString(MSG_SIZE);

    @Test
    public void parseArgumentsAsCatcher() {
        String[] args = new String[]{"-c", "-bind", IP_ADDRESS, "-port", PORT_STR};

        NodeSettings settings = null;
        try {
            settings = new NodeSettings(args);
        } catch (ParseException ex) {

        }

        assertEquals(NodeType.CATCHER, settings.getNodeType());
        assertEquals(PORT, settings.getPort());
        assertEquals(IP_ADDRESS, settings.getIpAddress());
    }

    @Test
    public void parseArgumentsAsPitcher() {
        String[] args = new String[]{"-p", "-port", PORT_STR, "-mps", SENDING_RATE_STR, "-size", MSG_SIZE_STR, HOSTNAME};

        NodeSettings settings = null;
        try {
            settings = new NodeSettings(args);
        } catch (ParseException ex) {

        }

        assertEquals(NodeType.PITCHER, settings.getNodeType());
        assertEquals(PORT, settings.getPort());
        assertEquals(SENDING_RATE, settings.getSendingRateMsgPerSec());
        assertEquals(MSG_SIZE, settings.getMessageSize());
        assertEquals(HOSTNAME, settings.getCatcherHostname());
    }

    @Test
    public void nodeTypeIsRequired() {
        Throwable exception = assertThrows(ParseException.class, () -> {
            String[] args = new String[]{"-bind", IP_ADDRESS, "-port", Integer.toString(PORT)};
            @SuppressWarnings("unused")
            NodeSettings settings = new NodeSettings(args);
        });
        assertEquals("Run mode must be specified -c for Catcher -p for pitcher", exception.getMessage());
    }

    @Test
    public void portIsRequired() {
        Throwable exception = assertThrows(ParseException.class, () -> {
            String[] args = new String[]{"-c", "-bind", IP_ADDRESS};
            @SuppressWarnings("unused")
            NodeSettings settings = new NodeSettings(args);
        });
        assertEquals("Port is required", exception.getMessage());
    }
}
