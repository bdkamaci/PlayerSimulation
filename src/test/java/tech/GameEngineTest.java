package tech;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameEngineTest {
    @Mock
    private MessageQueue messageQueue;

    @Mock
    private Player player1;

    @Mock
    private Player player2;

    // Test with regular constructor
    @Test
    public void testConstructor() {
        GameEngine gameEngine = new GameEngine();

        // Test that the gameEngine has created the necessary objects
        assertNotNull(getPrivateField(gameEngine, "messageQueue"));
        assertNotNull(getPrivateField(gameEngine, "player1"));
        assertNotNull(getPrivateField(gameEngine, "player2"));
    }

    // Test the init method with mocked dependencies
    @Test
    public void testInit() throws Exception {
        // Create a custom GameEngine with mocked dependencies
        GameEngine gameEngine = createGameEngineWithMocks();

        // Call init
        gameEngine.init();

        // Verify that players were started
        verify(player1).start();
        verify(player2).start();

        // Verify that the initial message was sent to Player 1
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(messageQueue).sendMessage(messageCaptor.capture());

        Message initialMessage = messageCaptor.getValue();
        assertEquals("GameEngine", initialMessage.sender());
        assertEquals("Player 1", initialMessage.receiver());
        assertEquals("Start", initialMessage.content());
        assertEquals(0, initialMessage.counter());
    }

    @Test
    public void testInit_withInterruptedException() throws Exception {
        // Create a custom GameEngine with mocked dependencies
        GameEngine gameEngine = createGameEngineWithMocks();

        // Make sendMessage throw an InterruptedException
        doThrow(new InterruptedException("Test interruption")).when(messageQueue).sendMessage(any(Message.class));

        // Call init - should handle the exception gracefully
        gameEngine.init();

        // Verify the thread interrupt status was set
        assertTrue(Thread.currentThread().isInterrupted());

        // Reset interrupt status for other tests
        Thread.interrupted();
    }

    // Helper method to create a GameEngine with mocked dependencies
    private GameEngine createGameEngineWithMocks() throws Exception {
        GameEngine gameEngine = new GameEngine();

        // Replace the real objects with mocks
        setPrivateField(gameEngine, "messageQueue", messageQueue);
        setPrivateField(gameEngine, "player1", player1);
        setPrivateField(gameEngine, "player2", player2);

        return gameEngine;
    }

    // Helper method to get the value of a private field
    private Object getPrivateField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            fail("Failed to get field " + fieldName + ": " + e.getMessage());
            return null;
        }
    }

    // Helper method to set the value of a private field
    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            fail("Failed to set field " + fieldName + ": " + e.getMessage());
        }
    }
}
