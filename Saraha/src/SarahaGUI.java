import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Saraha desktop interface prototype (purple glass / dashboard style).
 * Visual design and navigation only: sample data, not connected to the managers or TXT files yet.
 */
public class SarahaGUI extends JFrame {
    static final Color WHITE = Color.WHITE;
    static final Color SOFT = new Color(205, 185, 240);
    static final Color LILAC = new Color(190, 150, 255);
    static final Color CARD = new Color(138, 54, 204);
    static final Color ACCENT = new Color(108, 0, 255);
    static final Color ORANGE = new Color(247, 148, 50);
    static final Color PINK = new Color(222, 34, 92);
    static final Color DARK = new Color(36, 38, 52);
    static final Color DARK_2 = new Color(52, 55, 72);
    static final Color[] AVATARS = {
        new Color(214, 110, 70), new Color(70, 150, 214), new Color(60, 170, 120),
        new Color(200, 90, 150), new Color(150, 120, 230)
    };

    private final CardLayout cards = new CardLayout();
    private final JPanel pageHost = new JPanel(cards);
    private final List<NavButton> navs = new ArrayList<>();
    private final List<Contact> allContacts = new ArrayList<>();
    private final DefaultListModel<Contact> contactModel = new DefaultListModel<>();
    private String contactFilter = "";
    private String currentUser = "Mazen";
    private JPanel sentList;
    private JPanel favList;

    public SarahaGUI() {
        setTitle("Saraha | Anonymous Messages");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 740);
        setMinimumSize(new Dimension(960, 620));
        setLocationRelativeTo(null);
        allContacts.add(new Contact("Ahmed Hassan", "U002", 32));
        allContacts.add(new Contact("Mariam Ali", "U003", 18));
        allContacts.add(new Contact("Omar Samy", "U004", 9));
        allContacts.add(new Contact("Nour Adel", "U005", 4));
        applyFilter();
        showAuth(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SarahaGUI().setVisible(true));
    }

    // =====================================================================
    // Auth (sign in / register)
    // =====================================================================

    private void showAuth(boolean register) {
        Backdrop root = new Backdrop(false);
        root.setLayout(new GridBagLayout());
        Backdrop win = new Backdrop(true);
        win.setLayout(new GridLayout(1, 2));
        win.setBorder(new LineBorder(new Color(255, 255, 255, 30)));
        win.add(welcomePanel());
        win.add(authCard(register));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(50, 60, 50, 60);
        root.add(win, gc);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private JPanel welcomePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(40, 60, 50, 20));
        p.add(left(new Logo()));
        p.add(Box.createVerticalGlue());
        p.add(left(label("Welcome!", Font.BOLD, 56, WHITE)));
        p.add(Box.createVerticalStrut(30));
        p.add(left(new Rule()));
        p.add(Box.createVerticalStrut(30));
        JLabel text = new JLabel("<html>Saraha is a space for honest, anonymous<br>messages. Say what you think, and hear<br>what others think of you.</html>");
        text.setFont(font(Font.PLAIN, 13));
        text.setForeground(WHITE);
        p.add(left(text));
        p.add(Box.createVerticalStrut(30));
        GButton learn = new GButton("Learn More", ORANGE, PINK);
        learn.setMaximumSize(new Dimension(130, 38));
        learn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Saraha lets you send and receive anonymous messages.\nThis is a visual prototype."));
        p.add(left(learn));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel authCard(boolean register) {
        JPanel host = new JPanel(new GridBagLayout());
        host.setOpaque(false);
        Round glass = new Round(new Color(255, 255, 255, 38), 4);
        glass.setLayout(new BoxLayout(glass, BoxLayout.Y_AXIS));
        glass.setBorder(new EmptyBorder(28, 44, 24, 44));
        glass.setPreferredSize(new Dimension(380, register ? 520 : 440));

        JLabel title = label(register ? "Create account" : "Sign in", Font.BOLD, 30, WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        glass.add(left(title));
        glass.add(Box.createVerticalStrut(22));

        glass.add(left(label("User Name", Font.BOLD, 13, WHITE)));
        glass.add(Box.createVerticalStrut(8));
        Field user = new Field("Enter your username", false);
        glass.add(user);
        glass.add(Box.createVerticalStrut(16));

        glass.add(left(label("Password", Font.BOLD, 13, WHITE)));
        glass.add(Box.createVerticalStrut(8));
        Field pass = new Field(register ? "Create a password" : "Enter your password", true);
        glass.add(pass);

        final Field confirm = register ? new Field("Confirm your password", true) : null;
        if (register) {
            glass.add(Box.createVerticalStrut(16));
            glass.add(left(label("Confirm Password", Font.BOLD, 13, WHITE)));
            glass.add(Box.createVerticalStrut(8));
            glass.add(confirm);
        }
        glass.add(Box.createVerticalStrut(26));

        GButton submit = new GButton(register ? "Create account" : "Submit", ORANGE, PINK);
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        submit.addActionListener(e -> {
            String u = user.text().trim();
            if (register) {
                if (u.isEmpty() || pass.text().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                    return;
                }
                if (!pass.text().equals(confirm.text())) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match.");
                    return;
                }
                JOptionPane.showMessageDialog(this, "Registration screen preview only.");
                showAuth(false);
            } else {
                currentUser = u.isEmpty() ? "Mazen" : u;
                showDashboard();
            }
        });
        glass.add(left(submit));
        SwingUtilities.invokeLater(() -> {
            if (getRootPane() != null) getRootPane().setDefaultButton(submit);
        });

        glass.add(Box.createVerticalStrut(10));
        JButton link = new JButton("<html><u>" + (register ? "Back to sign in" : "Create a new account") + "</u></html>");
        link.setContentAreaFilled(false);
        link.setBorderPainted(false);
        link.setFocusPainted(false);
        link.setForeground(SOFT);
        link.setFont(font(Font.PLAIN, 12));
        link.setHorizontalAlignment(SwingConstants.CENTER);
        link.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addActionListener(e -> showAuth(!register));
        glass.add(left(link));

        glass.add(Box.createVerticalGlue());
        JPanel social = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        social.setOpaque(false);
        social.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        social.add(label("f", Font.BOLD, 22, WHITE));
        social.add(label("◎", Font.BOLD, 22, WHITE));
        social.add(label("P", Font.BOLD, 22, WHITE));
        glass.add(left(social));

        host.add(glass);
        return host;
    }

    // =====================================================================
    // Dashboard shell
    // =====================================================================

    private void showDashboard() {
        navs.clear();
        pageHost.removeAll();
        pageHost.setOpaque(false);
        pageHost.add(homePage(), "Home");
        pageHost.add(contactsPage(), "Contacts");
        pageHost.add(sendPage(), "Send");
        pageHost.add(inboxPage(), "Inbox");
        pageHost.add(sentPage(), "Sent");
        pageHost.add(favoritesPage(), "Favorites");

        Grad shell = new Grad(new Color(82, 32, 128), new Color(38, 22, 66), false);
        shell.setLayout(new BorderLayout());
        shell.add(topBar(), BorderLayout.NORTH);
        shell.add(sidebar(), BorderLayout.WEST);
        shell.add(pageHost, BorderLayout.CENTER);
        setContentPane(shell);
        go("Home");
        revalidate();
        repaint();
    }

    private void go(String dest) {
        cards.show(pageHost, dest);
        for (NavButton n : navs) {
            n.selected = n.dest.equals(dest);
            n.repaint();
        }
    }

    private JPanel topBar() {
        Grad bar = new Grad(new Color(42, 36, 46), new Color(92, 36, 150), false);
        bar.setPreferredSize(new Dimension(0, 72));
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(0, 26, 0, 20));

        bar.add(centered(new ThemeToggle()), BorderLayout.WEST);

        Field searchField = new Field("Search contacts", false);
        searchField.setPreferredSize(new Dimension(320, 40));
        searchField.setMaximumSize(new Dimension(320, 40));
        GButton searchBtn = new GButton("⌕", ACCENT, ACCENT);
        searchBtn.setFont(font(Font.BOLD, 18));
        searchBtn.setBorder(new EmptyBorder(6, 16, 6, 16));
        Runnable doSearch = () -> {
            contactFilter = searchField.text().trim().toLowerCase();
            applyFilter();
            go("Contacts");
        };
        searchBtn.addActionListener(e -> doSearch.run());
        searchField.input.addActionListener(e -> doSearch.run());
        JPanel search = new JPanel(new BorderLayout(8, 0));
        search.setOpaque(false);
        search.add(searchField, BorderLayout.CENTER);
        search.add(searchBtn, BorderLayout.EAST);
        bar.add(centered(search), BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(new Badge(null, new Color(145, 80, 215), true));
        right.add(new Badge(initial(currentUser), AVATARS[0], false));
        bar.add(centered(right), BorderLayout.EAST);
        return bar;
    }

    private JPanel sidebar() {
        Grad side = new Grad(new Color(72, 26, 150), new Color(5, 0, 10), true);
        side.setPreferredSize(new Dimension(250, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(26, 0, 20, 0));

        String[][] items = {
            {"⌂", "Home", "Home"}, {"♧", "Contacts", "Contacts"}, {"✉", "Send a message", "Send"},
            {"▤", "Inbox", "Inbox"}, {"↗", "Sent messages", "Sent"}, {"☆", "Favorites", "Favorites"}
        };
        for (String[] it : items) {
            NavButton b = new NavButton(it[0], it[1], it[2]);
            b.addActionListener(e -> go(b.dest));
            navs.add(b);
            side.add(b);
            side.add(Box.createVerticalStrut(8));
        }
        side.add(Box.createVerticalGlue());

        Round card = new Round(new Color(138, 54, 204, 170), 28);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.add(left(label("SIGNED IN AS", Font.PLAIN, 10, SOFT)));
        card.add(Box.createVerticalStrut(4));
        card.add(left(label(currentUser, Font.BOLD, 18, WHITE)));
        card.add(Box.createVerticalStrut(14));
        GButton logout = new GButton("Log out", ACCENT, ACCENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logout.addActionListener(e -> showAuth(false));
        card.add(left(logout));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0, 14, 0, 14));
        wrap.add(card, BorderLayout.CENTER);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        side.add(left(wrap));
        return side;
    }

    // =====================================================================
    // Pages
    // =====================================================================

    private JPanel page(String title, String subtitle, JComponent body) {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(26, 34, 26, 34));
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(left(label(title, Font.BOLD, 34, WHITE)));
        header.add(Box.createVerticalStrut(4));
        header.add(left(label(subtitle, Font.BOLD, 14, SOFT)));
        page.add(header, BorderLayout.NORTH);
        page.add(body, BorderLayout.CENTER);
        return page;
    }

    private String greeting() {
        int h = LocalTime.now().getHour();
        if (h < 12) return "good morning";
        if (h < 18) return "good afternoon";
        return "good evening";
    }

    private JPanel homePage() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 150));
        stats.add(statCard("Messages", "128", "+12 this week"));
        stats.add(statCard("Contacts", "24", "People connected"));
        stats.add(statCard("Favorites", "08", "Saved messages"));

        JPanel rings = new JPanel(new GridLayout(2, 2, 20, 20));
        rings.setOpaque(false);
        rings.add(ringCard("Inbox", "Messages read today", 50));
        rings.add(ringCard("Replies", "Replies sent today", 70));
        rings.add(ringCard("Favorites", "Messages saved", 60));
        rings.add(ringCard("Contacts", "Active this week", 20));

        JPanel leftCol = new JPanel(new BorderLayout(0, 20));
        leftCol.setOpaque(false);
        leftCol.add(stats, BorderLayout.NORTH);
        leftCol.add(rings, BorderLayout.CENTER);

        Round chart = new Round(CARD, 30);
        chart.setLayout(new BorderLayout(0, 10));
        chart.setBorder(new EmptyBorder(20, 20, 20, 20));
        chart.setPreferredSize(new Dimension(230, 0));
        chart.add(label("Weekly Messages", Font.BOLD, 16, WHITE), BorderLayout.NORTH);
        chart.add(new Bars(), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setOpaque(false);
        body.add(leftCol, BorderLayout.CENTER);
        body.add(chart, BorderLayout.EAST);
        return page("Hi " + currentUser + ",", greeting(), body);
    }

    private JPanel statCard(String label, String value, String caption) {
        Round p = new Round(CARD, 30);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 24, 20, 24));
        p.add(Box.createVerticalGlue());
        p.add(left(label(label, Font.BOLD, 15, WHITE)));
        p.add(left(label(value, Font.BOLD, 38, WHITE)));
        p.add(left(label(caption, Font.PLAIN, 14, SOFT)));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel ringCard(String title, String sub, int pct) {
        Round p = new Round(CARD, 30);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(18, 24, 18, 20));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(left(label(title, Font.BOLD, 17, WHITE)));
        text.add(left(label(sub, Font.PLAIN, 14, SOFT)));
        p.add(text, BorderLayout.NORTH);
        JPanel east = new JPanel(new BorderLayout());
        east.setOpaque(false);
        east.add(new Ring(pct), BorderLayout.SOUTH);
        p.add(east, BorderLayout.EAST);
        return p;
    }

    // ---- Contacts (eM Client style, dark) -------------------------------

    private JPanel contactsPage() {
        Round box = new Round(DARK, 22);
        box.setLayout(new BorderLayout());

        JList<Contact> list = new JList<>(contactModel);
        list.setOpaque(false);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setCellRenderer(new ContactCell());
        list.setFixedCellHeight(58);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ---- details panel
        JLabel nameL = label("No contact selected", Font.BOLD, 16, WHITE);
        JLabel idL = label(" ", Font.PLAIN, 13, SOFT);
        JLabel msgL = label(" ", Font.PLAIN, 13, SOFT);
        JLabel stL = label(" ", Font.PLAIN, 13, SOFT);
        Badge avatar = new Badge("?", DARK_2, false);
        avatar.setPreferredSize(new Dimension(76, 76));
        avatar.setMaximumSize(new Dimension(76, 76));
        Runnable refresh = () -> {
            Contact c = list.getSelectedValue();
            if (c == null) {
                nameL.setText("No contact selected");
                idL.setText(" ");
                msgL.setText(" ");
                stL.setText(" ");
                avatar.glyph = "?";
                avatar.fill = DARK_2;
            } else {
                nameL.setText(c.name);
                idL.setText("User ID: " + c.id);
                msgL.setText("Messages: " + c.messages);
                stL.setText("Status: Connected");
                avatar.glyph = initial(c.name);
                avatar.fill = c.color();
            }
            avatar.repaint();
        };
        list.addListSelectionListener(e -> refresh.run());

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(new CompoundBorder(new MatteBorder(0, 1, 0, 0, new Color(255, 255, 255, 25)),
                new EmptyBorder(18, 20, 18, 20)));
        details.setPreferredSize(new Dimension(270, 0));
        details.add(left(label("Contact Details", Font.BOLD, 18, LILAC)));
        details.add(Box.createVerticalStrut(20));
        details.add(left(avatar));
        details.add(Box.createVerticalStrut(12));
        details.add(left(nameL));
        details.add(Box.createVerticalStrut(6));
        details.add(left(idL));
        details.add(Box.createVerticalStrut(4));
        details.add(left(msgL));
        details.add(Box.createVerticalStrut(4));
        details.add(left(stL));
        details.add(Box.createVerticalStrut(22));
        JLabel hist = label("History", Font.BOLD, 13, WHITE);
        hist.setBorder(new MatteBorder(0, 0, 2, 0, LILAC));
        details.add(left(hist));
        details.add(Box.createVerticalStrut(10));
        details.add(left(label("There are no items to show.", Font.PLAIN, 12, SOFT)));
        details.add(Box.createVerticalGlue());

        // ---- toolbar
        GButton add = new GButton("＋  New", ACCENT, ACCENT);
        GButton edit = new GButton("Edit", DARK_2, DARK_2);
        GButton del = new GButton("Delete", DARK_2, DARK_2);
        for (GButton b : new GButton[]{add, edit, del}) {
            b.setFont(font(Font.BOLD, 12));
            b.setBorder(new EmptyBorder(8, 16, 8, 16));
        }
        add.addActionListener(e -> {
            String n = JOptionPane.showInputDialog(this, "Contact name:");
            if (n != null && !n.trim().isEmpty()) {
                allContacts.add(new Contact(n.trim(), String.format("U%03d", 2 + allContacts.size()), 0));
                applyFilter();
            }
        });
        edit.addActionListener(e -> {
            Contact c = list.getSelectedValue();
            if (c == null) return;
            String n = JOptionPane.showInputDialog(this, "Contact name:", c.name);
            if (n != null && !n.trim().isEmpty()) {
                c.name = n.trim();
                list.repaint();
                refresh.run();
            }
        });
        del.addActionListener(e -> {
            Contact c = list.getSelectedValue();
            if (c == null) return;
            allContacts.remove(c);
            applyFilter();
            refresh.run();
        });
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(add);
        actions.add(edit);
        actions.add(del);
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(14, 20, 12, 18));
        bar.add(label("Contacts", Font.BOLD, 20, LILAC), BorderLayout.WEST);
        bar.add(actions, BorderLayout.EAST);

        // ---- alphabet strip
        JPanel alpha = new JPanel(new GridLayout(26, 1));
        alpha.setOpaque(false);
        alpha.setPreferredSize(new Dimension(38, 0));
        alpha.setBorder(new MatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 25)));
        for (char ch = 'a'; ch <= 'z'; ch++) {
            final char letter = ch;
            JLabel l = label(String.valueOf(ch), Font.PLAIN, 11, SOFT);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    for (int i = 0; i < contactModel.size(); i++) {
                        if (Character.toLowerCase(contactModel.get(i).name.charAt(0)) == letter) {
                            list.setSelectedIndex(i);
                            list.ensureIndexIsVisible(i);
                            return;
                        }
                    }
                }
            });
            alpha.add(l);
        }

        JScrollPane sp = scroll(list);
        JPanel mid = new JPanel(new BorderLayout());
        mid.setOpaque(false);
        mid.add(alpha, BorderLayout.WEST);
        mid.add(sp, BorderLayout.CENTER);

        box.add(bar, BorderLayout.NORTH);
        box.add(mid, BorderLayout.CENTER);
        box.add(details, BorderLayout.EAST);
        return page("Contacts", "Manage the people connected to your account.", box);
    }

    private void applyFilter() {
        contactModel.clear();
        for (Contact c : allContacts) {
            if (contactFilter.isEmpty() || c.name.toLowerCase().contains(contactFilter)
                    || c.id.toLowerCase().contains(contactFilter)) {
                contactModel.addElement(c);
            }
        }
    }

    // ---- Send -------------------------------------------------------------

    private JPanel sendPage() {
        Round card = new Round(new Color(255, 255, 255, 30), 30);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 28, 26, 28));
        card.add(left(label("RECEIVER USER ID", Font.BOLD, 11, SOFT)));
        card.add(Box.createVerticalStrut(8));
        Field receiver = new Field("Enter receiver ID (e.g. U002)", false);
        card.add(receiver);
        card.add(Box.createVerticalStrut(20));
        card.add(left(label("YOUR MESSAGE", Font.BOLD, 11, SOFT)));
        card.add(Box.createVerticalStrut(8));

        JTextArea area = new JTextArea(8, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(WHITE);
        area.setCaretColor(WHITE);
        area.setFont(font(Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(14, 16, 14, 16));
        JScrollPane sp = scroll(area);
        Round areaBox = new Round(new Color(255, 255, 255, 35), 24);
        areaBox.setLayout(new BorderLayout());
        areaBox.add(sp, BorderLayout.CENTER);
        card.add(left(areaBox));
        card.add(Box.createVerticalStrut(18));

        GButton send = new GButton("Send message  →", ORANGE, PINK);
        send.setMaximumSize(new Dimension(220, 44));
        send.addActionListener(e -> {
            String to = receiver.text().trim();
            String msg = area.getText().trim();
            if (to.isEmpty() || msg.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a receiver ID and a message.");
                return;
            }
            addSent(to, msg);
            receiver.input.setText("");
            area.setText("");
            JOptionPane.showMessageDialog(this, "Preview only: message sending is not connected yet.");
            go("Sent");
        });
        card.add(left(send));
        return page("Send a message", "Your identity stays private from the receiver.", card);
    }

    // ---- Inbox (chat style) -----------------------------------------------

    private JPanel inboxPage() {
        Round chat = new Round(new Color(20, 18, 44, 235), 30);
        chat.setLayout(new BorderLayout());

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(8, 22, 8, 22));
        JScrollPane sp = scroll(listWrap(list));

        list.add(chatRow("Anonymous user", "You have a way of making difficult days feel lighter.", "Today · 10:42 AM", false));
        list.add(chatRow("Anonymous user", "I really appreciate your honesty. Keep being yourself.", "Yesterday · 8:15 PM", false));
        list.add(chatRow("Ahmed Hassan", "Thanks for helping me out with the project!", "Nov 13 · 4:30 PM", false));
        list.add(chatRow("You", "Happy to help! Anytime.", "Nov 13 · 4:41 PM", true));

        JLabel head = label("Conversations Inbox", Font.BOLD, 20, WHITE);
        head.setBorder(new EmptyBorder(20, 24, 8, 24));
        chat.add(head, BorderLayout.NORTH);
        chat.add(sp, BorderLayout.CENTER);

        Round inputBar = new Round(new Color(255, 255, 255, 24), 44);
        inputBar.setLayout(new BorderLayout(8, 0));
        inputBar.setBorder(new EmptyBorder(6, 8, 6, 8));
        Field reply = new Field("Write a reply...", false);
        reply.fill = new Color(0, 0, 0, 0);
        GButton sendBtn = new GButton("Send  ➤", ACCENT, ACCENT);
        Runnable doReply = () -> {
            String t = reply.text().trim();
            if (t.isEmpty()) return;
            list.add(chatRow("You", t, "Just now", true));
            reply.input.setText("");
            list.revalidate();
            list.repaint();
            SwingUtilities.invokeLater(() -> {
                JScrollBar vb = sp.getVerticalScrollBar();
                vb.setValue(vb.getMaximum());
            });
        };
        sendBtn.addActionListener(e -> doReply.run());
        reply.input.addActionListener(e -> doReply.run());
        inputBar.add(reply, BorderLayout.CENTER);
        inputBar.add(sendBtn, BorderLayout.EAST);
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(new EmptyBorder(8, 22, 20, 22));
        south.add(inputBar, BorderLayout.CENTER);
        chat.add(south, BorderLayout.SOUTH);

        JPanel side = new JPanel();
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(250, 0));
        side.add(senderCard("Anonymous user", "2 new messages", 0));
        side.add(Box.createVerticalStrut(16));
        side.add(senderCard("Ahmed Hassan", "Last message: Nov 13", 1));
        side.add(Box.createVerticalGlue());

        JPanel body = new JPanel(new BorderLayout(20, 0));
        body.setOpaque(false);
        body.add(chat, BorderLayout.CENTER);
        body.add(side, BorderLayout.EAST);
        return page("Inbox", "Messages people have sent to you.", body);
    }

    private JPanel senderCard(String name, String info, int colorIdx) {
        Round card = new Round(new Color(20, 18, 44, 235), 26);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(230, 90));
        Badge b = new Badge(initial(name), AVATARS[colorIdx + 1], false);
        card.add(b, BorderLayout.WEST);
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(Box.createVerticalGlue());
        t.add(left(label(name, Font.BOLD, 14, WHITE)));
        t.add(left(label(info, Font.PLAIN, 12, SOFT)));
        t.add(Box.createVerticalGlue());
        card.add(t, BorderLayout.CENTER);
        return left(card);
    }

    private JPanel chatRow(String sender, String text, String time, boolean mine) {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        if (!mine) {
            col.add(left(label(sender, Font.BOLD, 11, LILAC)));
            col.add(Box.createVerticalStrut(4));
        }
        Round bubble = new Round(mine ? new Color(120, 70, 220) : new Color(78, 52, 160), 28);
        bubble.setLayout(new BorderLayout());
        bubble.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel msg = new JLabel("<html><body style='width:300px'>" + escape(text) + "</body></html>");
        msg.setFont(font(Font.PLAIN, 14));
        msg.setForeground(WHITE);
        bubble.add(msg, BorderLayout.CENTER);
        col.add(left(bubble));
        col.add(Box.createVerticalStrut(4));
        JPanel meta = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        meta.setOpaque(false);
        meta.add(label(time, Font.PLAIN, 10, SOFT));
        if (!mine) {
            JButton star = new JButton("☆ Save");
            star.setContentAreaFilled(false);
            star.setBorderPainted(false);
            star.setFocusPainted(false);
            star.setForeground(LILAC);
            star.setFont(font(Font.PLAIN, 11));
            star.setMargin(new Insets(0, 0, 0, 0));
            star.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            star.addActionListener(e -> {
                addFavorite(sender, text);
                star.setText("★ Saved");
                star.setEnabled(false);
            });
            meta.add(star);
        }
        col.add(left(meta));

        Badge avatar = new Badge(initial(sender), mine ? ACCENT : AVATARS[Math.abs(sender.hashCode()) % AVATARS.length], false);
        avatar.setPreferredSize(new Dimension(38, 38));
        JPanel avatarWrap = new JPanel(new BorderLayout());
        avatarWrap.setOpaque(false);
        avatarWrap.add(avatar, BorderLayout.NORTH);

        JPanel flow = new JPanel(new FlowLayout(mine ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        flow.setOpaque(false);
        flow.add(col);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));
        row.add(avatarWrap, mine ? BorderLayout.EAST : BorderLayout.WEST);
        row.add(flow, BorderLayout.CENTER);
        return row;
    }

    // ---- Sent / Favorites -------------------------------------------------

    private JPanel sentPage() {
        sentList = new JPanel();
        sentList.setOpaque(false);
        sentList.setLayout(new BoxLayout(sentList, BoxLayout.Y_AXIS));
        sentList.add(messageCard("To: U003", "That was a really interesting point you made.", "Yesterday · 6:05 PM"));
        sentList.add(messageCard("To: U002", "Hope everything goes well with your exams.", "Today · 9:20 AM"), 0);
        return page("Sent messages", "Messages you've sent to other users.", scroll(listWrap(sentList)));
    }

    private JPanel favoritesPage() {
        favList = new JPanel();
        favList.setOpaque(false);
        favList.setLayout(new BoxLayout(favList, BoxLayout.Y_AXIS));
        favList.add(messageCard("Anonymous user", "Your effort will pay off. Keep going.", "Saved Nov 12"));
        favList.add(messageCard("Anonymous user", "Never underestimate the difference you make.", "Saved today"), 0);
        return page("Favorites", "Your favorite messages in one place.", scroll(listWrap(favList)));
    }

    private void addSent(String to, String msg) {
        sentList.add(messageCard("To: " + to, msg, "Just now"), 0);
        sentList.revalidate();
        sentList.repaint();
    }

    private void addFavorite(String from, String msg) {
        favList.add(messageCard(from, msg, "Saved just now"), 0);
        favList.revalidate();
        favList.repaint();
    }

    private JPanel messageCard(String sender, String content, String time) {
        Round card = new Round(new Color(255, 255, 255, 28), 24);
        card.setLayout(new BorderLayout(12, 6));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(left(label(sender, Font.BOLD, 12, LILAC)));
        text.add(Box.createVerticalStrut(6));
        JLabel msg = new JLabel("<html><body style='width:480px'>" + escape(content) + "</body></html>");
        msg.setFont(font(Font.PLAIN, 14));
        msg.setForeground(WHITE);
        text.add(left(msg));
        card.add(text, BorderLayout.CENTER);
        card.add(label(time, Font.PLAIN, 10, SOFT), BorderLayout.EAST);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0, 0, 12, 0));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    // =====================================================================
    // Helpers
    // =====================================================================

    static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    static JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font(style, size));
        l.setForeground(color);
        return l;
    }

    static <T extends JComponent> T left(T c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    static JPanel centered(JComponent c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    static String initial(String name) {
        return name == null || name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
    }

    static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    static void aa(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    static JPanel listWrap(JPanel list) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(list, BorderLayout.NORTH);
        return wrap;
    }

    static JScrollPane scroll(JComponent view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar bar = sp.getVerticalScrollBar();
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(10, 0));
        bar.setUnitIncrement(16);
        bar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(150, 100, 230, 170);
                trackColor = new Color(0, 0, 0, 0);
            }
            @Override protected JButton createDecreaseButton(int o) { return zero(); }
            @Override protected JButton createIncreaseButton(int o) { return zero(); }
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) { }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                aa(g2);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 2, r.y, r.width - 4, r.height, 8, 8);
                g2.dispose();
            }
            private JButton zero() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
        return sp;
    }

    // =====================================================================
    // Model
    // =====================================================================

    static class Contact {
        String name;
        final String id;
        final int messages;

        Contact(String name, String id, int messages) {
            this.name = name;
            this.id = id;
            this.messages = messages;
        }

        Color color() {
            return AVATARS[Math.abs(id.hashCode()) % AVATARS.length];
        }
    }

    static class ContactCell extends JPanel implements ListCellRenderer<Contact> {
        private final JLabel name = label(" ", Font.BOLD, 14, WHITE);
        private final JLabel sub = label(" ", Font.PLAIN, 12, SOFT);
        private boolean selected;
        private String initial = "?";
        private Color avatar = DARK_2;

        ContactCell() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(9, 66, 9, 10));
            add(name);
            add(sub);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Contact> l, Contact c, int i,
                                                      boolean sel, boolean focus) {
            name.setText(c.name);
            sub.setText(c.id + "  ·  " + c.messages + " messages");
            selected = sel;
            initial = initial(c.name);
            avatar = c.color();
            return this;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            if (selected) {
                g.setColor(new Color(120, 80, 200, 110));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            int d = 38;
            int y = (getHeight() - d) / 2;
            g.setColor(avatar);
            g.fillOval(16, y, d, d);
            g.setColor(WHITE);
            g.setFont(font(Font.BOLD, 15));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(initial, 16 + (d - fm.stringWidth(initial)) / 2, y + (d + fm.getAscent() - fm.getDescent()) / 2);
            g.dispose();
            super.paintComponent(g0);
        }
    }

    // =====================================================================
    // Custom components
    // =====================================================================

    /** Solid two-color gradient background. */
    static class Grad extends JPanel {
        private final Color a, b;
        private final boolean vertical;

        Grad(Color a, Color b, boolean vertical) {
            this.a = a;
            this.b = b;
            this.vertical = vertical;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setPaint(vertical ? new GradientPaint(0, 0, a, 0, getHeight(), b)
                                : new GradientPaint(0, 0, a, getWidth(), getHeight(), b));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.dispose();
        }
    }

    /** Purple geometric background used on the auth screens. */
    static class Backdrop extends JPanel {
        private final boolean deep;

        Backdrop(boolean deep) {
            this.deep = deep;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            int w = getWidth(), h = getHeight();
            Color c1 = deep ? new Color(60, 6, 64) : new Color(70, 10, 80);
            Color c2 = deep ? new Color(64, 24, 104) : new Color(48, 16, 92);
            g.setPaint(new GradientPaint(0, 0, c1, w, h, c2));
            g.fillRect(0, 0, w, h);

            g.setColor(new Color(110, 100, 190, deep ? 130 : 80));
            g.fillRoundRect((int) (w * 0.33), 0, 10, (int) (h * 0.30), 10, 10);
            g.fillRoundRect((int) (w * 0.38), 0, 10, (int) (h * 0.30), 10, 10);

            g.setStroke(new BasicStroke(Math.max(10f, h * 0.03f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(25, 0, 35, 110));
            g.drawLine((int) (w * 0.30), (int) (h * 0.95), (int) (w * 0.55), (int) (h * 0.62));
            g.drawLine((int) (w * 0.50), (int) (h * 0.50), (int) (w * 0.32), (int) (h * 0.34));

            g.setStroke(new BasicStroke(Math.max(14f, h * 0.07f)));
            g.setColor(new Color(100, 100, 210, deep ? 110 : 90));
            int d = (int) (h * 0.75);
            g.drawOval((int) (w * 0.78), (int) (h * 0.68), d, d);

            g.setStroke(new BasicStroke(8f));
            g.setColor(new Color(120, 90, 200, 70));
            g.drawOval((int) (w * 0.02), (int) (h * 0.62), (int) (h * 0.22), (int) (h * 0.22));

            Graphics2D r = (Graphics2D) g.create();
            r.rotate(Math.toRadians(-45), w * 0.82, h * 0.2);
            r.setPaint(new GradientPaint((float) (w * 0.68), 0f, new Color(120, 100, 200, 170),
                    (float) w, 0f, new Color(90, 60, 160, 50)));
            r.fill(new RoundRectangle2D.Double(w * 0.68, h * 0.14, w * 0.32, h * 0.1, 200, 200));
            r.dispose();
            g.dispose();
        }
    }

    /** Rounded translucent panel. */
    static class Round extends JPanel {
        Color fill;
        private final int arc;

        Round(Color fill, int arc) {
            this.fill = fill;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            g.setColor(fill);
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            g.dispose();
            super.paintComponent(g0);
        }
    }

    static class PlainField extends JTextField {
        private final String hint;

        PlainField(String hint) {
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) paintHint(g, this, hint);
        }
    }

    static class PassField extends JPasswordField {
        private final String hint;

        PassField(String hint) {
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0) paintHint(g, this, hint);
        }
    }

    static void paintHint(Graphics g, JTextComponent c, String hint) {
        Graphics2D g2 = (Graphics2D) g.create();
        aa(g2);
        g2.setColor(new Color(255, 255, 255, 120));
        g2.setFont(c.getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, c.getInsets().left, (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2);
        g2.dispose();
    }

    /** Rounded text field (plain or password) with a placeholder. */
    static class Field extends Round {
        final JTextField input;

        Field(String hint, boolean password) {
            super(new Color(255, 255, 255, 40), 40);
            setLayout(new BorderLayout());
            input = password ? new PassField(hint) : new PlainField(hint);
            input.setOpaque(false);
            input.setForeground(WHITE);
            input.setCaretColor(WHITE);
            input.setFont(font(Font.PLAIN, 13));
            input.setBorder(new EmptyBorder(0, 18, 0, 18));
            add(input, BorderLayout.CENTER);
            setPreferredSize(new Dimension(300, 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        String text() {
            return input instanceof JPasswordField ? new String(((JPasswordField) input).getPassword()) : input.getText();
        }
    }

    /** Pill button with a horizontal gradient. */
    static class GButton extends JButton {
        private final Color a, b;

        GButton(String text, Color a, Color b) {
            super(text);
            this.a = a;
            this.b = b;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(WHITE);
            setFont(font(Font.BOLD, 13));
            setBorder(new EmptyBorder(11, 26, 11, 26));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            boolean hot = getModel().isRollover();
            Color c1 = hot ? a.brighter() : a;
            Color c2 = hot ? b.brighter() : b;
            g.setPaint(new GradientPaint(0, 0, c1, getWidth(), 0, c2));
            g.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight()));
            g.dispose();
            super.paintComponent(g0);
        }
    }

    /** Sidebar navigation item; the selected one is a filled pill touching the right edge. */
    static class NavButton extends JButton {
        final String dest;
        boolean selected;

        NavButton(String icon, String label, String dest) {
            super(icon + "     " + label);
            this.dest = dest;
            setHorizontalAlignment(SwingConstants.LEFT);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(WHITE);
            setFont(font(Font.PLAIN, 15));
            setBorder(new EmptyBorder(14, 28, 14, 10));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            if (selected) {
                g.setColor(new Color(138, 54, 204));
                g.fill(new RoundRectangle2D.Float(0, 2, getWidth() + 40, getHeight() - 4, 52, 52));
            } else if (getModel().isRollover()) {
                g.setColor(new Color(255, 255, 255, 25));
                g.fill(new RoundRectangle2D.Float(0, 2, getWidth() + 40, getHeight() - 4, 52, 52));
            }
            g.dispose();
            super.paintComponent(g0);
        }
    }

    /** Round avatar / bell badge. A null glyph draws a bell. */
    static class Badge extends JComponent {
        String glyph;
        Color fill;
        private final boolean dot;

        Badge(String glyph, Color fill, boolean dot) {
            this.glyph = glyph;
            this.fill = fill;
            this.dot = dot;
            setPreferredSize(new Dimension(46, 46));
            setMaximumSize(new Dimension(46, 46));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            int w = getWidth(), h = getHeight();
            int d = Math.min(w, h) - 2;
            g.setColor(fill);
            g.fillOval(1, 1, d, d);
            g.setColor(WHITE);
            if (glyph == null) {
                int cx = w / 2, cy = h / 2;
                Path2D bell = new Path2D.Double();
                bell.moveTo(cx - 9, cy + 6);
                bell.curveTo(cx - 9, cy + 2, cx - 7, cy - 9, cx, cy - 9);
                bell.curveTo(cx + 7, cy - 9, cx + 9, cy + 2, cx + 9, cy + 6);
                bell.closePath();
                g.fill(bell);
                g.fillRoundRect(cx - 11, cy + 5, 22, 3, 3, 3);
                g.fillOval(cx - 3, cy + 9, 6, 4);
            } else {
                g.setFont(font(Font.BOLD, Math.max(12, d / 2)));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(glyph, (w - fm.stringWidth(glyph)) / 2, (h + fm.getAscent() - fm.getDescent()) / 2);
            }
            if (dot) {
                g.setColor(new Color(235, 40, 60));
                g.fillOval(w - 14, 4, 9, 9);
            }
            g.dispose();
        }
    }

    /** Circular progress indicator. */
    static class Ring extends JComponent {
        private final int pct;

        Ring(int pct) {
            this.pct = pct;
            setPreferredSize(new Dimension(76, 76));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            int d = Math.min(getWidth(), getHeight()) - 10;
            int x = (getWidth() - d) / 2, y = (getHeight() - d) / 2;
            g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 255, 255, 50));
            g.drawOval(x, y, d, d);
            g.setColor(new Color(255, 255, 255, 215));
            g.draw(new Arc2D.Double(x, y, d, d, 90, -360.0 * pct / 100.0, Arc2D.OPEN));
            g.setFont(font(Font.BOLD, 13));
            FontMetrics fm = g.getFontMetrics();
            String s = pct + "%";
            g.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g.dispose();
        }
    }

    /** Rounded bar chart for the weekly messages card. */
    static class Bars extends JComponent {
        private final int[] values = {45, 62, 90, 55, 75, 38, 68};
        private final String[] days = {"M", "T", "W", "T", "F", "S", "S"};

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            int w = getWidth(), h = getHeight() - 24;
            int n = values.length;
            int gap = 10;
            int bw = Math.max(10, (w - gap * (n - 1)) / n);
            int total = bw * n + gap * (n - 1);
            int x0 = (w - total) / 2;
            g.setFont(font(Font.PLAIN, 11));
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < n; i++) {
                int bh = (int) (h * values[i] / 100.0);
                int x = x0 + i * (bw + gap);
                g.setPaint(new GradientPaint(0, h - bh, new Color(70, 24, 135), 0, h, new Color(110, 50, 175)));
                g.fillRoundRect(x, h - bh, bw, bh, bw, bw);
                g.setColor(SOFT);
                g.drawString(days[i], x + (bw - fm.stringWidth(days[i])) / 2, h + 18);
            }
            g.dispose();
        }
    }

    /** Light / dark switch look from the dashboard mock-up (visual only). */
    static class ThemeToggle extends JComponent {
        private boolean dark = true;

        ThemeToggle() {
            setPreferredSize(new Dimension(70, 30));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    dark = !dark;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            aa(g);
            int w = getWidth(), h = getHeight();
            g.setColor(new Color(10, 5, 20));
            g.fillRoundRect(0, 0, w, h, h, h);
            g.setColor(new Color(145, 80, 215));
            g.fillOval(dark ? 2 : w - h + 2, 2, h - 4, h - 4);
            g.setColor(WHITE);
            g.setFont(font(Font.BOLD, 13));
            g.drawString(dark ? "☾" : "☀", dark ? 10 : w - h + 10, h - 9);
            g.dispose();
        }
    }

    static class Logo extends JComponent {
        Logo() {
            setPreferredSize(new Dimension(46, 38));
            setMaximumSize(new Dimension(46, 38));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(WHITE);
            g.fillRect(0, 0, 18, 36);
            g.fillRect(24, 0, 22, 12);
            g.fillRect(24, 18, 14, 18);
        }
    }

    static class Rule extends JComponent {
        Rule() {
            setPreferredSize(new Dimension(52, 3));
            setMaximumSize(new Dimension(52, 3));
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, 52, 2);
        }
    }
}
