package tech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MessageQueueTest {
    private MessageQueue messageQueue;

    private final String player1 = "Player1";

    private final String player2 = "Player2";

    @BeforeEach
    public void setUp() {
        messageQueue = new MessageQueue();
    }

    @Test
    public void testRegisterPlayer() {
        // Register a player
        messageQueue.registerPlayer(player1);

        // Test if the player can receive messages (indirectly testing if registration worked)
        try {
            // Send a message to the player
            Message message = new Message("Sender", player1, "Test content", 1);
            messageQueue.sendMessage(message);

            // Try to receive the message with a timeout to avoid hanging the test
            java.util.concurrent.BlockingQueue<Message> queue =
                    (java.util.concurrent.BlockingQueue<Message>) getQueueField(player1);

            Message received = queue.poll(1, TimeUnit.SECONDS);

            // Assert that we received the correct message
            assertNotNull(received);
            assertEquals("Sender", received.sender());
            assertEquals(player1, received.receiver());
            assertEquals("Test content", received.content());
            assertEquals(1, received.counter());
        } catch (Exception e) {
            fail("Failed to test registerPlayer: " + e.getMessage());
        }
    }

    @Test
    public void testSendMessage_validQueue() throws InterruptedException {
        // Register a player
        messageQueue.registerPlayer(player1);

        // Create a message
        Message message = new Message("Sender", player1, "Test content", 1);

        // Send the message
        messageQueue.sendMessage(message);

        // Verify the message was enqueued
        java.util.concurrent.BlockingQueue<Message> queue =
                (java.util.concurrent.BlockingQueue<Message>) getQueueField(player1);

        assertNotNull(queue);
        assertEquals(1, queue.size());
        assertEquals(message, queue.peek());
    }

    @Test
    public void testSendMessage_invalidQueue() throws InterruptedException {
        // Note: No player is registered, so no queue exists

        // Create a message for an unregistered player
        Message message = new Message("Sender", "UnregisteredPlayer", "Test content", 1);

        // Send the message - should not throw exception but log a warning
        messageQueue.sendMessage(message);

    }

    @Test
    public void testReceiveMessage_validQueue() throws InterruptedException {
        // Register a player
        messageQueue.registerPlayer(player1);

        // Create and send a message
        Message message = new Message("Sender", player1, "Test content", 1);
        messageQueue.sendMessage(message);

        // Receive the message
        Message received = messageQueue.receiveMessage(player1);

        // Verify the received message is correct
        assertNotNull(received);
        assertEquals(message, received);
    }

    @Test
    public void testReceiveMessage_invalidQueue() throws InterruptedException {
        // Try to receive a message for an unregistered player
        Message received = messageQueue.receiveMessage("UnregisteredPlayer");

        // Should return null for non-existent queue
        assertNull(received);
    }

    @Test
    public void testMultiplePlayersAndMessages() throws InterruptedException {
        // Register two players
        messageQueue.registerPlayer(player1);
        messageQueue.registerPlayer(player2);

        // Create and send messages
        Message message1 = new Message("Sender", player1, "Message for player 1", 1);
        Message message2 = new Message("Sender", player2, "Message for player 2", 2);

        messageQueue.sendMessage(message1);
        messageQueue.sendMessage(message2);

        // Receive the messages
        Message received1 = messageQueue.receiveMessage(player1);
        Message received2 = messageQueue.receiveMessage(player2);

        // Verify messages were correctly routed
        assertEquals(message1, received1);
        assertEquals(message2, received2);
    }

    // Helper method to access the private queues map using reflection
    private Object getQueueField(String playerName) {
        try {
            java.lang.reflect.Field queuesField = MessageQueue.class.getDeclaredField("queues");
            queuesField.setAccessible(true);
            java.util.Map<String, java.util.concurrent.BlockingQueue<Message>> queues =
                    (java.util.Map<String, java.util.concurrent.BlockingQueue<Message>>) queuesField.get(messageQueue);

            return queues.get(playerName);
        } catch (Exception e) {
            fail("Failed to access queues field: " + e.getMessage());
            return null;
        }
    }
}
