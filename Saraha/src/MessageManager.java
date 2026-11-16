// TODO: Manage all message operations.

// Required methods:

// sendMessage(String senderId, String receiverId, String content)
// Return: boolean

// undoLastMessage(String senderId)
// Return: boolean

// getSentMessages(String senderId)
// Return: ArrayList

// getReceivedMessages(String receiverId)
// Return: ArrayList

// getMessageCount(String userId1, String userId2)
// Return: int

// TODO: Support viewing received messages
// from a specific contact.

// Requirements:
// - Store messages.
// - Display sent messages from newest to oldest.
// - Allow undoing the last sent message.

import java.util.ArrayList;

public class MessageManager {

    public boolean hasSentMessage(String senderId, String receiverId) {
        return false;
    }

    public int getMessageCount(String userId1, String userId2) {
        return 0;
    }
}
