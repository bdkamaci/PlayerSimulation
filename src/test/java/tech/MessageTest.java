package tech;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    @Test
    public void testMessageConstruction() {
        String sender = "Alice";
        String receiver = "Bob";
        String content = "Hello, Bob!";
        int counter = 1;

        Message message = new Message(sender, receiver, content, counter);

        assertEquals(sender, message.sender());
        assertEquals(receiver, message.receiver());
        assertEquals(content, message.content());
        assertEquals(counter, message.counter());
    }

    @Test
    public void testGetters() {
        String sender = "Alice";
        String receiver = "Bob";
        String content = "Hello, Bob!";
        int counter = 1;

        Message message = new Message(sender, receiver, content, counter);

        assertEquals(sender, message.sender());
        assertEquals(receiver, message.receiver());
        assertEquals(content, message.content());
        assertEquals(counter, message.counter());
    }

    @Test
    public void testToString() {
        String sender = "Alice";
        String receiver = "Bob";
        String content = "Hello, Bob!";
        int counter = 1;

        Message message = new Message(sender, receiver, content, counter);

        String expected = "From: Alice To: Bob | #1 - Hello, Bob!";
        assertEquals(expected, message.toString());
    }

    @Test
    public void testEquality() {
        Message message1 = new Message("Alice", "Bob", "Hello", 1);
        Message message2 = new Message("Alice", "Bob", "Hello", 1);
        Message message3 = new Message("Bob", "Alice", "Hello", 1);

        // Test equals
        assertEquals(message1, message2);
        assertNotEquals(message1, message3);

        // Test hashCode
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    public void testWithDifferentCounters() {
        Message message1 = new Message("Alice", "Bob", "Hello", 1);
        Message message2 = new Message("Alice", "Bob", "Hello", 2);

        assertNotEquals(message1, message2);
    }

    @Test
    public void testWithDifferentContent() {
        Message message1 = new Message("Alice", "Bob", "Hello", 1);
        Message message2 = new Message("Alice", "Bob", "Hi there", 1);

        assertNotEquals(message1, message2);
    }
}
