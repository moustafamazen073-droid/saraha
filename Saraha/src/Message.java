

// TODO: Create the Message class.

// Each message must contain:
// 1. Message ID
// 2. Sender ID
// 3. Receiver ID
// 4. Message content
// 5. Timestamp (long)

// TODO: Create a constructor.
// TODO: Create getters for all attributes.


public class Message {

    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp;

    // Constructor
    public Message(String messageId, String senderId,
                   String receiverId, String content, long timestamp) {

        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
