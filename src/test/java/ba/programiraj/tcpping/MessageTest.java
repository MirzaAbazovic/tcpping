package ba.programiraj.tcpping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @Test
    public void messageSerialization() {
        //Arrange
        long messageId,  startPitch, startCatch, endCatch, endPitch;
        int messageLength;
        messageId = 8;
        messageLength = 500;
        startPitch = 5;
        startCatch = 10;
        endCatch = 20;
        endPitch = 37;
        Message msg = new Message(messageId, messageLength);
        msg.setTimeStampStartPitch(startPitch);
        msg.setTimeStampStartCatch(startCatch);
        msg.setTimeStampEndCatch(endCatch);
        msg.setTimeStampEndPitch(endPitch);
        //Act
        byte[] messageAsBytes = msg.getMessageBytes();
        Message msgFromBytes = Message.createMessageFromByteArray(messageAsBytes);
        //Assert
        assertEquals(messageId,msgFromBytes.getMessageId());
        assertEquals(messageLength,msgFromBytes.getMessageLenght());
        assertEquals(endPitch-startPitch,msgFromBytes.getTimeFromPitcherToPitcher());
        assertEquals(startCatch-startPitch,msgFromBytes.getTimeFromPitcherToCatcher());
        assertEquals(endPitch - endCatch,msgFromBytes.getTimeFromPitcherToCatcher());
        
    }

}
