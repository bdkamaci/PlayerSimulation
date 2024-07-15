import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.MessageQueue;
import tech.Player;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private MessageQueue messageQueue;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue();
        player1 = new Player(messageQueue, "Player 1", 5);
        player2 = new Player(messageQueue, "Player 2", 5);

        player1.play();
        player2.play();
    }

    @Test
    void testPlayersCommunication() throws InterruptedException {
        // Verifies that both players can exchange messages successfully.
        messageQueue.put("Start");

        TimeUnit.SECONDS.sleep(11); // Allowing some time for messages to be exchanged

        // Ensure that both players have exchanged messages
        assertTrue(player1.getMessageCount() > 0);
        assertTrue(player2.getMessageCount() > 0);
    }

    @Test
    void testGameStart() throws InterruptedException {
        // Verifies that the game starts correctly by checking if at least one player has started sending messages.
        messageQueue.put("Start");

        TimeUnit.SECONDS.sleep(1); // Allowing some time for the first message to be processed

        assertTrue(player1.getMessageCount() > 0 || player2.getMessageCount() > 0, "Game did not start correctly.");
    }

    @Test
    void testGameEnd() throws InterruptedException {
        // Verifies that the game ends correctly by checking if both players have sent the maximum number of messages.
        messageQueue.put("Start");

        TimeUnit.SECONDS.sleep(11); // Allowing enough time for the game to end

        assertEquals(5, player1.getMessageCount(), "Player 1 did not finish correctly.");
        assertEquals(5, player2.getMessageCount(), "Player 2 did not finish correctly.");
    }

    @Test
    void testPlayerReceivesMessage() throws InterruptedException {
        // Verifies that each player receives messages from the queue.
        messageQueue.put("Start");

        TimeUnit.SECONDS.sleep(1); // Allowing some time for the message to be received

        assertTrue(player1.getMessageCount() > 0, "Player 1 did not receive any messages.");
        assertTrue(player2.getMessageCount() > 0, "Player 2 did not receive any messages.");
    }

    @Test
    void testPlayerSendsMessage() throws InterruptedException {
        // Verifies that each player sends messages to the queue.
        messageQueue.put("Start");

        TimeUnit.SECONDS.sleep(1); // Allowing some time for the message to be sent

        assertTrue(messageQueue.take().contains("Player 1 sent message") || messageQueue.take().contains("Player 2 sent message"), "Players did not send any messages.");
    }
}
