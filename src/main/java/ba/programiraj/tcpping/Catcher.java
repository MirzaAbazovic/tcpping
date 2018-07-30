package ba.programiraj.tcpping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Catcher extends Node {

    public Catcher(NodeSettings settings) {
        super(settings);
    }

    @Override
    public void run() {
        ServerSocket catcherServerSocket = null;
        try {
            catcherServerSocket = new ServerSocket(settings.getPort());
            while (true) {
                //wait for connection
                Socket catcherEnd = catcherServerSocket.accept();
                //handle new connection in thread CatcherHandler
                new Thread(new CatcherHandler(catcherEnd)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Catcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Catcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}