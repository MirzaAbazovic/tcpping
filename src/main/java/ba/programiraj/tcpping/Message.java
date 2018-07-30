package ba.programiraj.tcpping;

import java.nio.ByteBuffer;

public class Message {

    private static final int MESSAGE_ID_POSITION = 0;
    private static final int START_PITCH_POSITION = 1;
    private static final int START_CATCH_POSITION = 2;
    private static final int END_CATCH_POSITION = 3;
    private static final int END_PITCH_POSITION = 4;

    private final long messageId;
    private final int messageLenght;
    //[messageId ,timeStampStartPitch, timeStampStartCatch, timeStampEndCatch, timeStampEndPitch, .....]
    private byte[] messageBytes;
    // timeStampStartPitch <=  timeStampStartCatch <= timeStampEndCatch <= timeStampEndPitch
    private long timeStampStartPitch;
    private long timeStampStartCatch;
    private long timeStampEndCatch;
    private long timeStampEndPitch;

    public static Message createMessageFromByteArray(byte[] messageAsBytes) {

        ByteBuffer byteBuffer = ByteBuffer.wrap(messageAsBytes);
        long id = byteBuffer.getLong(MESSAGE_ID_POSITION * Long.BYTES);
        Message message = new Message(id, messageAsBytes.length);
        
        message.timeStampStartPitch = byteBuffer.getLong(START_PITCH_POSITION * Long.BYTES);
        message.timeStampStartCatch = byteBuffer.getLong(START_CATCH_POSITION * Long.BYTES);
        message.timeStampEndCatch = byteBuffer.getLong(END_CATCH_POSITION * Long.BYTES);
        message.timeStampEndPitch = byteBuffer.getLong(END_PITCH_POSITION * Long.BYTES);

        return message;
    }

    public Message(long messageId, int messageLength) {
        if (messageLength < 32) {
            throw new IllegalArgumentException("Message lenght must be greater than 32");
        }
        if (messageId <= 0) {
            throw new IllegalArgumentException("Message id must be greater than 0");
        }
        this.messageBytes = new byte[messageLength];
        insertToPosition(MESSAGE_ID_POSITION, messageId);
        this.messageId = messageId;
        this.messageLenght = messageLength;
    }

    public int getMessageLenght() {
        return messageLenght;
    }

    public long getMessageId() {
        return messageId;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public void setTimeStampStartPitch(long timeStampStartPitch) {
        this.timeStampStartPitch = timeStampStartPitch;
        insertToPosition(START_PITCH_POSITION, timeStampStartPitch);
    }

    public void setTimeStampStartCatch(long timeStampStartCatch) {
        this.timeStampStartCatch = timeStampStartCatch;
        insertToPosition(START_CATCH_POSITION, timeStampStartCatch);
    }

    public void setTimeStampEndCatch(long timeStampEndCatch) {
        this.timeStampEndCatch = timeStampEndCatch;
        insertToPosition(END_CATCH_POSITION, timeStampEndCatch);
    }

    public void setTimeStampEndPitch(long timeStampEndPitch) {
        this.timeStampEndPitch = timeStampEndPitch;
        insertToPosition(END_PITCH_POSITION, timeStampEndPitch);
    }

    
    public long getTimeFromPitcherToPitcher() {
        return timeStampEndPitch - timeStampStartPitch;
    }

    public long getTimeFromPitcherToCatcher() {
        return timeStampStartCatch - timeStampStartPitch;
    }

    public long getTimeFromCatcherToPitcher() {
        return timeStampEndPitch - timeStampEndCatch ;
    }
    @Override
    public String toString() {
            return "Message [messageId=" + messageId 
                    + ", messageLength=" + messageLenght
                    + ", timeStampStartPitch = " + timeStampStartPitch 
                    + ", timeStampStartCatch = " + timeStampStartCatch 
                    + ", timeStampEndCatch = " + timeStampEndCatch 
                    + ", timeStampEndPitch= " + timeStampEndPitch 
                    + "\nPITCHER->CATCHER->PITCHER time [ms]= " + getTimeFromPitcherToPitcher()
                    + ", PITCHER->CATHCER time [ms]= " + getTimeFromPitcherToCatcher()
                    + ", CATCHER->PITCHER time [ms]= " + getTimeFromCatcherToPitcher()                    
                    + "]";
    }
    
    private void insertToPosition(int position, long value) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(this.messageBytes);
        byteBuffer.position(position * Long.BYTES);
        byteBuffer.putLong(value);
    }

    // private long readFromPosition(int position) {
    //     ByteBuffer byteBuffer = ByteBuffer.wrap(this.messageBytes);
    //     return byteBuffer.getLong(position * Long.BYTES);
    // }

    
}
