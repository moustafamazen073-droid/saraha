
import java.util.HashMap;

public class UserManager {

    private HashMap<String, User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    // Register a new user
    // Return false if ID or username already exists
    // Return true if registration succeeds
    public boolean register(String id, String username, String password) {

        if (users.containsKey(id)) {
            return false;
        }

        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }

        User newUser = new User(id, username, password);
        users.put(id, newUser);

        return true;
    }

    // Login using username and password
    // Return User if successful, otherwise null
    public User login(String username, String password) {

        for (User user : users.values()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    // Find user by ID
    // Return User if found, otherwise null
    public User findUser(String id) {
        return users.get(id);
    }
}
