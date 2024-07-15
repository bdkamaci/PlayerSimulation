package tech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine {
    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final MessageQueue messageQueue;
    private final Player player1;
    private final Player player2;

    // Constants for player configuration
    private static final int MAX_MESSAGES = 10;
    private static final String INITIAL_MESSAGE = "Start";

    public GameEngine() {
        this.messageQueue = new MessageQueue();
        this.player1 = new Player(messageQueue, "Player 1", MAX_MESSAGES);
        this.player2 = new Player(messageQueue, "Player 2", MAX_MESSAGES);
    }

    public void init() {
        logger.info("Game initialization started.");

        player1.play();
        player2.play();

        try {
            messageQueue.put(INITIAL_MESSAGE); // Starting the conversation with an initial message
            logger.info("Game started with initial message: {}", INITIAL_MESSAGE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread was interrupted", e);
        }

        logger.info("Game initialization finished.");
    }
}
