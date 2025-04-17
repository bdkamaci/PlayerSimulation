package tech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerTest {
    @Mock
    private MessageQueue messageQueue;

    private Player player;

    private final String playerName = "TestPlayer";

    private final String partnerName = "Partner";

    private final int maxMessageCount = 3;

    @BeforeEach
    public void setUp() {
        player = new Player(messageQueue, playerName, partnerName, maxMessageCount);
    }

    @Test
    public void testConstructor() {
        // Verify the player was registered with the message queue
        verify(messageQueue).registerPlayer(playerName);

        // Verify initial message count is 0
        assertEquals(0, player.getMessageCount());
    }

    @Test
    public void testExtractMessageId_nullContent() {
        // Using reflection to test private method
        java.lang.reflect.Method method;
        try {
            method = Player.class.getDeclaredMethod("extractMessageId", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(player, (String) null);
            assertEquals("Start", result);
        } catch (Exception e) {
            fail("Failed to test extractMessageId: " + e.getMessage());
        }
    }

    @Test
    public void testExtractMessageId_blankContent() {
        // Using reflection to test private method
        java.lang.reflect.Method method;
        try {
            method = Player.class.getDeclaredMethod("extractMessageId", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(player, "");
            assertEquals("Start", result);
        } catch (Exception e) {
            fail("Failed to test extractMessageId: " + e.getMessage());
        }
    }

    @Test
    public void testExtractMessageId_validContent() {
        // Using reflection to test private method
        java.lang.reflect.Method method;
        try {
            method = Player.class.getDeclaredMethod("extractMessageId", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(player, "[Msg#5] From: Alice → Bob | Reply to: [Msg#4]");
            assertEquals("[Msg#5]", result);
        } catch (Exception e) {
            fail("Failed to test extractMessageId: " + e.getMessage());
        }
    }

    @Test
    public void testExtractMessageId_invalidContent() {
        // Using reflection to test private method
        java.lang.reflect.Method method;
        try {
            method = Player.class.getDeclaredMethod("extractMessageId", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(player, "No message ID here");
            assertEquals("Unknown", result);
        } catch (Exception e) {
            fail("Failed to test extractMessageId: " + e.getMessage());
        }
    }

    @Test
    public void testSendReply() throws Exception {
        // Using reflection to test private method
        java.lang.reflect.Method method = Player.class.getDeclaredMethod("sendReply", String.class);
        method.setAccessible(true);

        // Call the method
        method.invoke(player, "[Msg#1] Test content");

        // Verify message count incremented
        assertEquals(1, player.getMessageCount());

        // Verify a message was sent through the queue
        verify(messageQueue).sendMessage(any(Message.class));
    }

    @Test
    public void testRun_processesMessagesUpToMaxCount() throws Exception {
        // Mock the message queue to return messages
        Message message = new Message("Partner", playerName, "[Msg#1] Test content", 1);
        when(messageQueue.receiveMessage(playerName))
                .thenReturn(message)
                .thenReturn(message)
                .thenReturn(message);

        // Create a player with a small max message count
        Player testPlayer = new Player(messageQueue, playerName, partnerName, 3);

        // Create a thread that will run the player
        Thread playerThread = new Thread(testPlayer);
        playerThread.start();

        // Give the thread some time to execute
        Thread.sleep(3500); // Enough time for 3 messages with 1-second delays

        // Stop the player
        testPlayer.stop();

        // Verify the message count reached max
        assertEquals(3, testPlayer.getMessageCount());
    }

    @Test
    public void testStop() {
        // Create a player and start it
        Player testPlayer = new Player(messageQueue, playerName, partnerName, maxMessageCount);
        testPlayer.start();

        // Stop the player
        testPlayer.stop();

    }
}
