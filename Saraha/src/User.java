
import java.util.ArrayList;

public class User {

    private String id;
    private String username;
    private String password;
    private ArrayList<String> contacts;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.contacts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void addContact(String userId) {
        if (!contacts.contains(userId)) {
            contacts.add(userId);
        }
    }

    public void removeContact(String userId) {
        contacts.remove(userId);
    }
}
