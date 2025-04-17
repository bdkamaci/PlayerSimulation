package tech;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages message queues for each player in the game.
 * Each player has a dedicated queue identified by their name.
 */
public class MessageQueue {

    private final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private final Map<String, BlockingQueue<Message>> queues = new ConcurrentHashMap<>();

    /**
     * Registers a player with a dedicated message queue.
     *
     * @param playerName the name of the player
     */
    public void registerPlayer(String playerName) {
        queues.putIfAbsent(playerName, new LinkedBlockingQueue<>());
        logger.info("Registered message queue for {}", playerName);
    }

    /**
     * Sends a message to the specified player.
     *
     * @param message the message to send
     * @throws InterruptedException if interrupted while adding to the queue
     */
    public void sendMessage(Message message) throws InterruptedException {
        BlockingQueue<Message> queue = queues.get(message.receiver());
        if (queue != null) {
            queue.put(message);
            logger.info("Message sent: {}", message);
        } else {
            logger.warn("Queue not found for receiver: {}", message.receiver());
        }
    }

    /**
     * Retrieves the next message for a player.
     *
     * @param playerName the name of the player
     * @return the next message
     * @throws InterruptedException if interrupted while waiting
     */
    public Message receiveMessage(String playerName) throws InterruptedException {
        BlockingQueue<Message> queue = queues.get(playerName);
        if (queue != null) {
            Message msg = queue.take();
            logger.info("{} received: {}", playerName, msg);
            return msg;
        } else {
            logger.warn("Queue not found for {}", playerName);
            return null;
        }
    }
}
