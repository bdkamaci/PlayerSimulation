package tech;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a player in the game that can send and receive messages.
 * Each player runs in a separate thread.
 */
public class Player implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Player.class);

    private final MessageQueue messageQueue;

    private final String name;

    private final String partnerName;

    private final int maxMessageCount;

    @Getter
    private int messageCount = 0;

    private Thread thread;

    public Player(MessageQueue messageQueue, String name, String partnerName, int maxMessageCount) {
        this.messageQueue = messageQueue;
        this.name = name;
        this.partnerName = partnerName;
        this.maxMessageCount = maxMessageCount;

        this.messageQueue.registerPlayer(name);
    }

    /**
     * Starts the player thread.
     */
    public void start() {
        thread = new Thread(this);
        thread.setName(name + "-Thread");
        thread.start();
    }

    /**
     * Main player loop: receives and sends messages up to the max count.
     */
    @Override
    public void run() {
        try {
            while (messageCount < maxMessageCount) {
                Message received = messageQueue.receiveMessage(name);
                if (received != null) {
                    logger.info("{} received: {}", name, received);
                    sendReply(received.content());
                    Thread.sleep(1000); // Simulate processing time
                }
            }
            logger.info("{} finished playing.", name);
        } catch (InterruptedException e) {
            logger.error("{} was interrupted. Exiting...", name, e);
            Thread.currentThread().interrupt();
        }
    }

    private void sendReply(String receivedContent) throws InterruptedException {
        messageCount++;

        String replyTo = extractMessageId(receivedContent);
        String content = String.format("[Msg#%d] From: %s → %s | Reply to: %s",
                messageCount, name, partnerName, replyTo);

        Message message = new Message(name, partnerName, content, messageCount);
        messageQueue.sendMessage(message);
        logger.info("{} sent: {}", name, message);
    }

    private String extractMessageId(String content) {
        if (content == null || content.isBlank()) {
            return "Start";
        }

        int start = content.indexOf("[Msg#");
        int end = content.indexOf("]", start);

        if (start != -1 && end != -1 && end > start) {
            return content.substring(start, end + 1);
        }

        return "Unknown";
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }
}
