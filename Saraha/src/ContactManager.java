
import java.util.ArrayList;
import java.util.Collections;

public class ContactManager {

    private UserManager userManager;
    private MessageManager messageManager;

    public ContactManager(UserManager userManager,
                          MessageManager messageManager) {
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    // Add contact only if that contact sent a message to the user
    public boolean addContact(String userId, String contactId) {

        User user = userManager.findUser(userId);
        User contact = userManager.findUser(contactId);

        if (user == null || contact == null) {
            return false;
        }

        if (user.getContacts().contains(contactId)) {
            return false;
        }

        if (!messageManager.hasSentMessage(contactId, userId)) {
            return false;
        }

        user.addContact(contactId);
        return true;
    }

    // Remove contact without deleting old messages
    public boolean removeContact(String userId, String contactId) {

        User user = userManager.findUser(userId);

        if (user == null) {
            return false;
        }

        if (!user.getContacts().contains(contactId)) {
            return false;
        }

        user.removeContact(contactId);
        return true;
    }

    // Search for a contact
    // Return User if found, otherwise null
    public User searchContact(String userId, String contactId) {

        User user = userManager.findUser(userId);

        if (user == null) {
            return null;
        }

        if (!user.getContacts().contains(contactId)) {
            return null;
        }

        return userManager.findUser(contactId);
    }

    // Get contacts sorted by exchanged message count
    // Highest count comes first
    public ArrayList<User> getSortedContacts(String userId) {

        ArrayList<User> contacts = new ArrayList<>();

        User user = userManager.findUser(userId);

        if (user == null) {
            return contacts;
        }

        for (String contactId : user.getContacts()) {
            contacts.add(userManager.findUser(contactId));
        }

        for (int i = 0; i < contacts.size(); i++) {

            for (int j = i + 1; j < contacts.size(); j++) {

                int count1 = messageManager.getMessageCount(
                    userId, contacts.get(i).getId()
                );

                int count2 = messageManager.getMessageCount(
                    userId, contacts.get(j).getId()
                );

                if (count1 < count2) {
                    Collections.swap(contacts, i, j);
                }
            }
        }

        return contacts;
    }
}
