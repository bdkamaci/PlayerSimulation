package tech;

public record Message(String sender, String receiver, String content, int counter) {
    @Override
    public String toString() {
        return String.format("From: %s To: %s | #%d - %s", sender, receiver, counter, content);
    }
}
