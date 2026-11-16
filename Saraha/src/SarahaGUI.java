import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Saraha desktop interface prototype.
 * This version focuses on the visual design and screen navigation.
 * It uses sample data only and is not connected to the project managers or TXT files yet.
 */
public class SarahaGUI extends JFrame {
    private static final Color BG = new Color(15, 17, 27);
    private static final Color PANEL = new Color(24, 27, 41);
    private static final Color PANEL_2 = new Color(31, 35, 52);
    private static final Color PURPLE = new Color(145, 92, 246);
    private static final Color TEXT = new Color(242, 242, 250);
    private static final Color MUTED = new Color(157, 163, 184);
    private final CardLayout cards = new CardLayout();
    private final JPanel pageHost = new JPanel(cards);
    private final JPanel shell = new JPanel(new BorderLayout());
    private String currentUser = "Mazen";

    public SarahaGUI() {
        setTitle("Saraha | Anonymous Messages");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        showAuth();
    }

    private void showAuth() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        JPanel card = panel(PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(38, 44, 38, 44));
        card.setPreferredSize(new Dimension(410, 510));

        JLabel logo = heading("s", 42, PURPLE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = heading("Welcome to Saraha", 25, TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = body("A space for honest, anonymous messages", MUTED, 13);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(7));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(32));
        card.add(fieldLabel("USERNAME"));
        JTextField username = input("Enter your username");
        card.add(username);
        card.add(Box.createVerticalStrut(17));
        card.add(fieldLabel("PASSWORD"));
        JPasswordField password = new JPasswordField();
        styleInput(password, "Enter your password");
        card.add(password);
        card.add(Box.createVerticalStrut(25));
        JButton login = primaryButton("Sign in");
        login.addActionListener(e -> {
            currentUser = username.getText().trim().isEmpty() ? "Mazen" : username.getText().trim();
            showDashboard();
        });
        card.add(login);
        card.add(Box.createVerticalStrut(12));
        JButton register = textButton("Create a new account");
        register.addActionListener(e -> showRegister());
        card.add(register);
        root.add(card);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private void showRegister() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        JPanel card = panel(PANEL);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(34, 42, 34, 42));
        card.setPreferredSize(new Dimension(410, 540));
        JLabel title = heading("Create account", 25, TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(28));
        card.add(fieldLabel("USERNAME"));
        card.add(input("Choose a username"));
        card.add(Box.createVerticalStrut(15));
        card.add(fieldLabel("PASSWORD"));
        JPasswordField pass = new JPasswordField();
        styleInput(pass, "Create a password");
        card.add(pass);
        card.add(Box.createVerticalStrut(15));
        card.add(fieldLabel("CONFIRM PASSWORD"));
        JPasswordField confirm = new JPasswordField();
        styleInput(confirm, "Confirm your password");
        card.add(confirm);
        card.add(Box.createVerticalStrut(25));
        JButton create = primaryButton("Create account");
        create.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Registration screen preview only.");
            showAuth();
        });
        card.add(create);
        card.add(Box.createVerticalStrut(10));
        JButton back = textButton("Back to sign in");
        back.addActionListener(e -> showAuth());
        card.add(back);
        root.add(card);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private void showDashboard() {
        shell.removeAll();
        shell.setBackground(BG);
        shell.add(buildSidebar(), BorderLayout.WEST);
        pageHost.removeAll();
        pageHost.setBackground(BG);
        pageHost.add(homePage(), "Home");
        pageHost.add(contactsPage(), "Contacts");
        pageHost.add(composePage(), "Send");
        pageHost.add(inboxPage(), "Inbox");
        pageHost.add(sentPage(), "Sent");
        pageHost.add(favoritesPage(), "Favorites");
        shell.add(pageHost, BorderLayout.CENTER);
        setContentPane(shell);
        cards.show(pageHost, "Home");
        revalidate();
        repaint();
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(PANEL);
        side.setPreferredSize(new Dimension(225, 0));
        side.setBorder(new EmptyBorder(25, 16, 20, 16));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        JLabel brand = heading("s  saraha", 25, TEXT);
        brand.setBorder(new EmptyBorder(0, 10, 28, 0));
        side.add(brand);
        side.add(navButton("▦", "Overview", "Home"));
        side.add(navButton("♧", "Contacts", "Contacts"));
        side.add(navButton("✉", "Send a message", "Send"));
        side.add(navButton("▤", "Inbox", "Inbox"));
        side.add(navButton("↗", "Sent messages", "Sent"));
        side.add(navButton("☆", "Favorites", "Favorites"));
        side.add(Box.createVerticalGlue());
        JLabel profile = body("SIGNED IN AS", MUTED, 10);
        profile.setBorder(new EmptyBorder(0, 10, 5, 0));
        side.add(profile);
        JLabel user = heading(currentUser, 15, TEXT);
        user.setBorder(new EmptyBorder(0, 10, 14, 0));
        side.add(user);
        JButton logout = navButton("⇦", "Log out", "Logout");
        side.add(logout);
        return side;
    }

    private JButton navButton(String icon, String label, String destination) {
        JButton b = new JButton(icon + "     " + label);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setForeground(destination.equals("Home") ? Color.WHITE : MUTED);
        b.setBackground(destination.equals("Home") ? new Color(58, 43, 91) : PANEL);
        b.setBorder(new EmptyBorder(13, 12, 13, 12));
        b.setFocusPainted(false);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            if (destination.equals("Logout")) showAuth();
            else cards.show(pageHost, destination);
        });
        return b;
    }

    private JPanel pageBase(String eyebrow, String title, String subtitle) {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setBackground(BG);
        page.setBorder(new EmptyBorder(30, 34, 30, 34));
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(body(eyebrow.toUpperCase(), PURPLE, 11));
        header.add(Box.createVerticalStrut(7));
        header.add(heading(title, 27, TEXT));
        header.add(Box.createVerticalStrut(5));
        header.add(body(subtitle, MUTED, 13));
        page.add(header, BorderLayout.NORTH);
        return page;
    }

    private JPanel homePage() {
        JPanel page = pageBase("Your workspace", "Good evening, " + currentUser, "Here's what's happening with your Saraha account.");
        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setOpaque(false);
        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);
        stats.add(statCard("TOTAL MESSAGES", "128", "+12 this week", "✉"));
        stats.add(statCard("CONTACTS", "24", "People you've connected with", "♧"));
        stats.add(statCard("FAVORITES", "08", "Saved messages", "☆"));
        content.add(stats, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 15, 0));
        lower.setOpaque(false);
        JPanel activity = panel(PANEL);
        activity.setLayout(new BoxLayout(activity, BoxLayout.Y_AXIS));
        activity.setBorder(new EmptyBorder(20, 20, 20, 20));
        activity.add(heading("Recent activity", 17, TEXT));
        activity.add(Box.createVerticalStrut(18));
        activity.add(activityRow("New anonymous message", "You received a new message", "2 min ago"));
        activity.add(activityRow("Favorite saved", "A message was added to favorites", "Yesterday"));
        activity.add(activityRow("Contact added", "Someone joined your contacts", "2 days ago"));
        JPanel quick = panel(PANEL);
        quick.setLayout(new BoxLayout(quick, BoxLayout.Y_AXIS));
        quick.setBorder(new EmptyBorder(20, 20, 20, 20));
        quick.add(heading("Quick actions", 17, TEXT));
        quick.add(Box.createVerticalStrut(18));
        JButton send = primaryButton("✉   Write a message");
        send.addActionListener(e -> cards.show(pageHost, "Send"));
        JButton contacts = secondaryButton("♧   View contacts");
        contacts.addActionListener(e -> cards.show(pageHost, "Contacts"));
        quick.add(send);
        quick.add(Box.createVerticalStrut(12));
        quick.add(contacts);
        lower.add(activity);
        lower.add(quick);
        content.add(lower, BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel statCard(String label, String value, String note, String icon) {
        JPanel p = panel(PANEL);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel top = body(label, MUTED, 11);
        JLabel val = heading(value, 31, TEXT);
        JLabel detail = body(note, MUTED, 12);
        JLabel symbol = heading(icon, 22, PURPLE);
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.add(top, BorderLayout.WEST);
        row.add(symbol, BorderLayout.EAST);
        p.add(row);
        p.add(Box.createVerticalStrut(14));
        p.add(val);
        p.add(Box.createVerticalStrut(5));
        p.add(detail);
        return p;
    }

    private JPanel activityRow(String title, String description, String time) {
        JPanel row = new JPanel(new BorderLayout(10, 2));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));
        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.add(body(title, TEXT, 13));
        texts.add(Box.createVerticalStrut(4));
        texts.add(body(description, MUTED, 11));
        row.add(texts, BorderLayout.CENTER);
        row.add(body(time, MUTED, 10), BorderLayout.EAST);
        return row;
    }

    private JPanel contactsPage() {
        JPanel page = pageBase("Your network", "Contacts", "Manage the people connected to your account.");
        JPanel content = panel(PANEL);
        content.setLayout(new BorderLayout(0, 18));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setOpaque(false);
        top.add(input("Search contacts..."), BorderLayout.CENTER);
        top.add(secondaryButton("＋  Add contact"), BorderLayout.EAST);
        content.add(top, BorderLayout.NORTH);
        String[] columns = {"CONTACT", "USER ID", "MESSAGES", "STATUS", "ACTION"};
        Object[][] data = {
            {"Ahmed Hassan", "U002", "32", "Connected", "•••"},
            {"Mariam Ali", "U003", "18", "Connected", "•••"},
            {"Omar Samy", "U004", "09", "Connected", "•••"},
            {"Nour Adel", "U005", "04", "Connected", "•••"}
        };
        JTable table = new JTable(data, columns);
        styleTable(table);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel composePage() {
        JPanel page = pageBase("Say what you think", "Send a message", "Your identity stays private from the receiver.");
        JPanel form = panel(PANEL);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(24, 24, 24, 24));
        form.add(fieldLabel("RECEIVER USER ID"));
        form.add(input("Enter receiver ID (e.g. U002)"));
        form.add(Box.createVerticalStrut(20));
        form.add(fieldLabel("YOUR MESSAGE"));
        JTextArea message = new JTextArea(8, 30);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBackground(PANEL_2);
        message.setForeground(TEXT);
        message.setCaretColor(TEXT);
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        message.setBorder(new EmptyBorder(12, 12, 12, 12));
        JScrollPane scroll = new JScrollPane(message);
        scroll.setBorder(new LineBorder(new Color(54, 59, 80), 1, true));
        form.add(scroll);
        form.add(Box.createVerticalStrut(18));
        JButton send = primaryButton("Send message  →");
        send.addActionListener(e -> JOptionPane.showMessageDialog(this, "Preview only: message sending is not connected yet."));
        form.add(send);
        page.add(form, BorderLayout.CENTER);
        return page;
    }

    private JPanel inboxPage() {
        JPanel page = pageBase("Messages", "Inbox", "Messages people have sent to you.");
        JPanel list = panel(PANEL);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(18, 20, 18, 20));
        list.add(messageRow("Anonymous user", "You have a way of making difficult days feel lighter.", "Today · 10:42 AM"));
        list.add(new JSeparator());
        list.add(messageRow("Anonymous user", "I really appreciate your honesty. Keep being yourself.", "Yesterday · 8:15 PM"));
        list.add(new JSeparator());
        list.add(messageRow("Ahmed Hassan", "Thanks for helping me out with the project!", "Nov 13 · 4:30 PM"));
        page.add(list, BorderLayout.CENTER);
        return page;
    }

    private JPanel sentPage() {
        JPanel page = pageBase("Your activity", "Sent messages", "Messages you've sent to other users.");
        JPanel list = panel(PANEL);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(18, 20, 18, 20));
        list.add(messageRow("To: U002", "Hope everything goes well with your exams.", "Today · 9:20 AM"));
        list.add(new JSeparator());
        list.add(messageRow("To: U003", "That was a really interesting point you made.", "Yesterday · 6:05 PM"));
        page.add(list, BorderLayout.CENTER);
        return page;
    }

    private JPanel favoritesPage() {
        JPanel page = pageBase("Saved for later", "Favorites", "Your favorite messages in one place.");
        JPanel list = panel(PANEL);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(18, 20, 18, 20));
        list.add(messageRow("Anonymous user", "Never underestimate the difference you make.", "Saved today"));
        list.add(new JSeparator());
        list.add(messageRow("Anonymous user", "Your effort will pay off. Keep going.", "Saved Nov 12"));
        page.add(list, BorderLayout.CENTER);
        return page;
    }

    private JPanel messageRow(String sender, String content, String time) {
        JPanel row = new JPanel(new BorderLayout(12, 8));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(15, 5, 15, 5));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(body(sender, PURPLE, 12));
        text.add(Box.createVerticalStrut(7));
        text.add(body(content, TEXT, 14));
        row.add(text, BorderLayout.CENTER);
        row.add(body(time, MUTED, 10), BorderLayout.EAST);
        return row;
    }

    private JPanel panel(Color color) {
        JPanel p = new JPanel();
        p.setBackground(color);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(43, 47, 66), 1, true), new EmptyBorder(1, 1, 1, 1)));
        return p;
    }

    private JLabel heading(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(color);
        return label;
    }

    private JLabel body(String text, Color color, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = body(text, MUTED, 10);
        label.setBorder(new EmptyBorder(0, 0, 7, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField input(String placeholder) {
        JTextField field = new JTextField();
        styleInput(field, placeholder);
        return field;
    }

    private void styleInput(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setPreferredSize(new Dimension(300, 46));
        field.setBackground(PANEL_2);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(new LineBorder(new Color(54, 59, 80), 1, true), new EmptyBorder(10, 12, 10, 12)));
        field.setToolTipText(placeholder);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PURPLE);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(13, 18, 13, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        return b;
    }

    private JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PANEL_2);
        b.setForeground(TEXT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(new LineBorder(new Color(65, 69, 91), 1, true), new EmptyBorder(11, 15, 11, 15)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton textButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(new Color(190, 164, 255));
        b.setBackground(PANEL);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 8, 8, 8));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTable(JTable table) {
        table.setBackground(PANEL);
        table.setForeground(TEXT);
        table.setGridColor(new Color(48, 52, 72));
        table.setRowHeight(48);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(68, 51, 105));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(PANEL_2);
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBorder(new EmptyBorder(12, 8, 12, 8));
    }
}
