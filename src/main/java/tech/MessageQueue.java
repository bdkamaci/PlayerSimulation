package tech;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageQueue {
    private final BlockingQueue<String> queue;
    private final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    public MessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void put(String message) throws InterruptedException {
        queue.put(message);
        logger.info("Message put into queue: {}", message);
    }

    public String take() throws InterruptedException {
        String message = queue.take();
        logger.info("Message taken from queue: {}", message);
        return message;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }
}
