package ba.programiraj.tcpping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CatcherHandler implements Runnable {

    private final Socket catcherEndSocket;

    CatcherHandler(Socket socket) {
        this.catcherEndSocket = socket;
    }

    @Override
    public void run() {
        try {
            long timeStampCatcherStart = System.currentTimeMillis();//begin work in catcher timestamp
            
            InputStream fromPitcher = catcherEndSocket.getInputStream();
            OutputStream toPitcher = catcherEndSocket.getOutputStream();
            
            byte[] receivedBytesMessageLength = new byte[4];//placeholder for message length
            byte[] receivedBytesFromPitcher =  null;//placeholder for received message
            
            int recivedBytes = fromPitcher.read(receivedBytesMessageLength); //read message length int as 4 bytes
            ByteBuffer byteBuffer = ByteBuffer.wrap(receivedBytesMessageLength);//convert byte[] to int
            int messageLength = byteBuffer.getInt();

            if( messageLength > 0 ) {
                receivedBytesFromPitcher = new byte[messageLength];
                recivedBytes = fromPitcher.read(receivedBytesFromPitcher);//read all bytes from pitcher
            }
            Message receivedMessage = Message.createMessageFromByteArray(receivedBytesFromPitcher);//parse bytes to message
            //timestamp message
            receivedMessage.setTimeStampStartCatch(timeStampCatcherStart);
            receivedMessage.setTimeStampEndCatch(System.currentTimeMillis());
            toPitcher.write(receivedMessage.getMessageBytes());//return message to pitcher
        }
        catch (IOException ioEx) {
            Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ioEx);
        }
        catch (Exception ex) {
            Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
