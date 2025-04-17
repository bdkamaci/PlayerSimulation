package tech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates the interaction between two players using the MessageQueue.
 * Responsible for initializing players, starting the game, and sending the initial message.
 */
public class GameEngine {
    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final MessageQueue messageQueue;

    private final Player player1;

    private final Player player2;

    private static final int MAX_MESSAGES = 10;

    public GameEngine() {
        this.messageQueue = new MessageQueue();

        this.player1 = new Player(messageQueue, "Player 1", "Player 2", MAX_MESSAGES);
        this.player2 = new Player(messageQueue, "Player 2", "Player 1", MAX_MESSAGES);

        logger.info("GameEngine initialized with Player 1 and Player 2.");
    }

    /**
     * Initializes and starts the game.
     */
    public void init() {
        logger.info("Game initialization started.");

        // Start both player threads
        player1.start();
        player2.start();

        // Kick off the first message to Player 1
        try {
            Message startMessage = new Message("GameEngine", "Player 1", "Start", 0);
            messageQueue.sendMessage(startMessage);
            logger.info("Game started with initial message: {}", startMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Failed to send initial message", e);
        }

        logger.info("Game initialization finished.");
    }
}
