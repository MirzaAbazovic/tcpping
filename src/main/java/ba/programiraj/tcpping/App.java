package ba.programiraj.tcpping;

import org.apache.commons.cli.ParseException;

public class App {
    
    private static final String USAGE_MESSAGE = "Usage: tcpping [-c] [-p] [-port <port>] [-bind <ip_address>] [-mps <rate>] [-size <size>] hostname\n"
            + "\n"
            + "Options:\n"
            + "-c \t\t\t Run in Catcher mode.\n"
            + "-p \t\t\t Run in Pitcher mode.\n"
            + "-port <port> \t\t TCP socket port used for connection. Used in both Catcher and Pitcher mode.\n"
            + "-bind <ip_address> \t IP address to listen on. Used in Catcher mode.\n"
            + "-mps <rate> \t\t Speed of sending messaged \"messages per second\".Default 1. Used in Pitcher mode.\n"
            + "-size <size> \t\t Size of message in bytes Min: 50,  Max: 3000, Default: 300. Used in Pitcher mode.\n"
            + "hostname \t\t Computer name where Catcher is running. Used in Pitcher mode.";

    public static void main(String[] args) throws InterruptedException {
        Node node;
        try {
            node = NodeFactory.getNode(new NodeSettings(args));
            node.run();
        } catch (ParseException e) {
            System.out.println(USAGE_MESSAGE);
            System.out.println("\nERROR: " + e.getMessage());
        }  
    }
}
