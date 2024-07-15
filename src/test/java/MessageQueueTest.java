import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.MessageQueue;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MessageQueueTest {
    private MessageQueue messageQueue;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue();
    }

    @Test
    void testPutAndTake() throws InterruptedException {
        // Verifies that a message put into the queue can be successfully taken out.
        String message = "Test Message";
        messageQueue.put(message);

        String receivedMessage = messageQueue.take();
        assertEquals(message, receivedMessage);
    }

    @Test
    void testBlockingTake() throws InterruptedException {
        // Verifies the blocking behavior of the take() method.
        // take() method will wait for a message to be available if the queue is empty.
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                messageQueue.put("Delayed Message");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // Record the start time
        long startTime = System.currentTimeMillis();
        // Attempt to take a message from the queue
        String message = messageQueue.take();
        // Record the end time
        long endTime = System.currentTimeMillis();

        // Verify that the taken message is the expected one
        assertEquals("Delayed Message", message);
        // Verify that the take() method blocked for at least 1 second
        assertTrue((endTime - startTime) >= 1000);

    }

    @Test
    void testQueueIsEmptyInitially() {
        // Verifies that the message queue is empty when it is first created.
        assertTrue(messageQueue.getQueue().isEmpty(), "Message queue should be empty initially");
    }

    @Test
    void testQueueIsNotEmptyAfterPut() throws InterruptedException {
        // Verifies that the message queue is not empty after putting a message into it.
        messageQueue.put("Test Message");
        assertFalse(messageQueue.getQueue().isEmpty(), "Message queue should not be empty after putting a message");
    }

    @Test
    void testQueueIsEmptyAfterTake() throws InterruptedException {
        // Verifies that the message queue is empty after taking the only message out of it.
        messageQueue.put("Test Message");
        messageQueue.take();
        assertTrue(messageQueue.getQueue().isEmpty(), "Message queue should be empty after taking the message");
    }
}
