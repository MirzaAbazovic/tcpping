package ba.programiraj.tcpping;

import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class NodeSettings {

    private static final int MIN_MESSAGE_SIZE = 50;
    private static final int DEFAULT_MESSAGE_SIZE = 300;
    private static final int MAX_MESSAGE_SIZE = 3000;
    private static final int DEFAULT_SENDING_RATE = 1;

    private NodeType nodeType;
    private int port;
    private String ipAddress;
    private int sendingRateMsgPerSec;
    private int messageSize;
    private String catcherHostname;

    public NodeSettings() {
    }

    public NodeSettings(String args[]) throws ParseException {
        parseArguments(args);
    }

    private void parseArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("c", false, "Is Catcher");
        options.addOption("p", false, "Is Pitcher");
        options.addOption("bind", true, "Catcher binding address for listening");
        options.addOption("port", true, "port for listening/connecting");
        options.addOption("mps", true, "Rate messages per second");
        options.addOption("size", true, "Size of messages in bytes");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("p")) {
            this.setNodeType(NodeType.PITCHER);
            String mps = cmd.getOptionValue("mps");
            if (mps == null) {
                this.setSendingRateMsgPerSec(DEFAULT_SENDING_RATE);
            } else {
                this.setSendingRateMsgPerSec(Integer.parseInt(mps));
            }
            String size = cmd.getOptionValue("size");
            if (size == null) {
                this.setMessageSize(DEFAULT_MESSAGE_SIZE);
            } else {
                int msgSize = Integer.parseInt(size);
                if (msgSize > MAX_MESSAGE_SIZE) {
                    throw new ParseException("Message size is bigger than maximal size " + MAX_MESSAGE_SIZE);
                } else if (msgSize < MIN_MESSAGE_SIZE) {
                    throw new ParseException("Message size is smaller than minimal size " + MIN_MESSAGE_SIZE);
                }
                this.setMessageSize(msgSize);
            }

            CommandLineParser commandParser = new DefaultParser();
            CommandLine result = commandParser.parse(options, args);
            List<String> argList = result.getArgList();
            if (argList.size() == 1) {
                this.setCatcherHostname(argList.get(0));
            } else {
                throw new ParseException("Hostname of catcher must be provided and it can be only one");
            }
        } else if (cmd.hasOption("c")) {
            this.setNodeType(NodeType.CATCHER);
            String bind = cmd.getOptionValue("bind");
            if (bind == null) {
                throw new ParseException("Ip address of Catcher is required");
            }
            this.setIpAddress(bind);
        } else {
            throw new ParseException("Run mode must be specified -c for Catcher -p for pitcher");
        }
        String port = cmd.getOptionValue("port");
        if (port == null) {
            throw new ParseException("Port is required");
        }
        this.setPort(Integer.parseInt(port));
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getSendingRateMsgPerSec() {
        return sendingRateMsgPerSec;
    }

    public void setSendingRateMsgPerSec(int sendingRateMsgPerSec) {
        this.sendingRateMsgPerSec = sendingRateMsgPerSec;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getCatcherHostname() {
        return catcherHostname;
    }

    public void setCatcherHostname(String catcherHostname) {
        this.catcherHostname = catcherHostname;
    }
}
