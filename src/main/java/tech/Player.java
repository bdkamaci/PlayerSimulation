package tech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player {
    private final Logger logger = LoggerFactory.getLogger(Player.class);
    private final MessageQueue messageQueue;
    private final String name;
    private final int maxMessageCount;
    private int messageCount = 0;

    public Player(MessageQueue messageQueue, String name, int maxMessageCount) {
        this.messageQueue = messageQueue;
        this.name = name;
        this.maxMessageCount = maxMessageCount;
    }

    public void play() {
        Runnable task = () -> {
            try {
                while (messageCount < maxMessageCount) {
                    receiveMessage();
                    sendMessage();
                    Thread.sleep(1000); // Simulating some processing time
                }
                logger.info("{} has finished playing.", name);
            } catch (InterruptedException e) {
                logger.error("Message not sent. Exception: {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        };

        new Thread(task).start();
    }

    private void sendMessage() {
        messageCount++;
        String newMessage = String.format("Message %d of %s", messageCount, name);
        logger.info("{} sending message: {}", name, newMessage);
        try {
            messageQueue.put(newMessage);
        } catch (InterruptedException e) {
            logger.error("Failed to put message into queue. Exception: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private void receiveMessage() throws InterruptedException {
        String message = messageQueue.take();
        logger.info("{} received message: {}", name, message);
    }

    public int getMessageCount() {
        return messageCount;
    }
}
