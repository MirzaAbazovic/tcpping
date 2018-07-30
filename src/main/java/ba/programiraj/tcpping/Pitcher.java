package ba.programiraj.tcpping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pitcher extends Node {

    Socket pitcherEnd;
    List<Message> pitchedMessages = new ArrayList<Message>();
    List<Message> receivedMessages = new ArrayList<Message>();
    float totalMaxTime = 0;

    public Pitcher(NodeSettings settings) {
        super(settings);
    }

    @Override
    public void run() {
        Runnable runEverySecond = new Runnable() {
            @Override
            public void run() {
                long messageId = 0;
                //pitch N messages in one second
                while (messageId < settings.getSendingRateMsgPerSec()) {
                    try {
                        pitcherEnd = new Socket(settings.getIpAddress(), settings.getPort());
                    } catch (IOException ex) {
                        Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Message message = new Message(++messageId, settings.getMessageSize());//generate message
                    pitchMessage(pitcherEnd, message);//pitch message to socket pitcherEnd
                }
                String statistics = calculateStatistics();//calculate statistics from pitchedMessages & receivedMessages
                System.out.println(statistics);
                clearMessages();//clear pitchedMessages & receivedMessages
                try {
                    pitcherEnd.close();
                } catch (IOException ex) {
                    Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        //run every second 
        executor.scheduleAtFixedRate(runEverySecond, 0, 1, TimeUnit.SECONDS);
    }

    private void pitchMessage(Socket pitcherEnd, Message messageToPitch) {
        try {
            long startPitchTimeStamp = System.currentTimeMillis();
          
            InputStream fromCatcher = pitcherEnd.getInputStream();
            OutputStream toCatcher = pitcherEnd.getOutputStream();

            pitchedMessages.add(messageToPitch); //add to send messages list
          
            toCatcher.write(intToBytes(messageToPitch.getMessageLenght())); // write length of the message to catcher
            toCatcher.write(messageToPitch.getMessageBytes()); // write message to catcher
            
            byte[] returnedBytes = new byte[settings.getMessageSize()]; // placeholder for returned message
            int receivedBytes = fromCatcher.read(returnedBytes); // read back message from catcher
            Message receivedMessage = Message.createMessageFromByteArray(returnedBytes);
            
            receivedMessage.setTimeStampStartPitch(startPitchTimeStamp); //timestamp message
            receivedMessage.setTimeStampEndPitch(System.currentTimeMillis()); //timestamp messge 
            //to see details about every message uncomment next line
            //System.out.println(receivedMessage.toString());
            receivedMessages.add(receivedMessage);//add to received messages list
        } catch (IOException ex) {
            Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Pitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String calculateStatistics() {
        int receivedMsgCount = receivedMessages.size();
        StringBuffer sb = new StringBuffer();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        sb.append(dateFormat.format(new Date()));
        sb.append(" | pitched messages: ");
        sb.append(pitchedMessages.size());
        sb.append(" | received messages: ");
        sb.append(receivedMsgCount);
        float avgTimeABA, avgTimeAB, avgTimeBA;
        avgTimeABA = avgTimeAB = avgTimeBA = 0;
        long  maxABATime, totalABATimes, totalABTimes, totalBATimes; 
        maxABATime = totalABATimes = totalABTimes = totalBATimes = 0;
        for (Message msg : receivedMessages) {
            maxABATime = 0;
            totalABATimes += msg.getTimeFromPitcherToPitcher();
            totalABTimes += msg.getTimeFromPitcherToCatcher();
            totalBATimes += msg.getTimeFromCatcherToPitcher();
            if (msg.getTimeFromPitcherToPitcher() > maxABATime) {
                maxABATime = msg.getTimeFromPitcherToPitcher();
            }
        }
        if( maxABATime > totalMaxTime ){
            totalMaxTime = maxABATime;
        }
        avgTimeABA = (float) totalABATimes / receivedMsgCount ;
        avgTimeAB = (float) totalABTimes / receivedMsgCount;
        avgTimeBA = (float) totalBATimes / receivedMsgCount;
        sb.append(" | Average A->B->A time: ");
        sb.append(String.format("%.2f", avgTimeABA));
        sb.append(" | Max A->B->A time: ");
        sb.append(String.format("%.2f", totalMaxTime));
        sb.append(" | Average A->B time: ");
        sb.append(String.format("%.2f", avgTimeAB));
        sb.append(" | Average B->A time: ");
        sb.append(String.format("%.2f", avgTimeBA));

        return sb.toString();
    }

    private void clearMessages() {
        pitchedMessages.clear();
        receivedMessages.clear();
    }

    private byte[] intToBytes( final int i ) {
        ByteBuffer bb = ByteBuffer.allocate(4); 
        bb.putInt(i); 
        byte[] intAsbyteArray = bb.array();
        return intAsbyteArray;
    }

}
