package client;

import server.ChatObserver;
import server.ChatInterface;
import sun.misc.Signal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class UserClient extends JFrame implements ChatObserver, Serializable {

    private User u;
    private ChatInterface h;
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JButton exitButton;
    private static String host;
    private static int port;

    public JTextArea getChatArea() {
        return this.chatArea;
    }

    public UserClient() {
        chatArea = new JTextArea();
        inputField = new JTextField();
        sendButton = new JButton("Send");
        exitButton = new JButton("Exit");

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitChat();
                System.exit(0);
            }
        });

        initGUI();
    }

    private void initGUI() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        add(chatScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(exitButton, BorderLayout.WEST);
        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setSize(600, 400);
        setLocationRelativeTo(null);

        startUserClient();
    }

    private void startUserClient() {
        try {

            Registry registry = LocateRegistry.getRegistry(host, port);
            h = (ChatInterface) registry.lookup("ChatService");

            ChatObserver stub = (ChatObserver) UnicastRemoteObject.exportObject(this, 0);

            int response = -1;
            String userName;

            while (response == -1) {
                userName = JOptionPane.showInputDialog("Enter your user name:");
                if (userName != null && !userName.isEmpty()) {
                    u = new User();
                    u.setUserName(userName);

                    response = h.enter(u, stub);

                    if (response == -1) {
                        JOptionPane.showMessageDialog(null, "UserName already taken, choose another one!");
                    } else {
                        Signal.handle(new Signal("INT"),
                                signal -> {
                                    try {
                                        h.exit(u, stub);
                                        System.exit(0);
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        SwingUtilities.invokeLater(() -> setVisible(true));
                    }
                } else {
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Error starting user client: " + e);
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            inputField.setText("");
            try {
                h.say(u, message);
            } catch (RemoteException e) {
                System.err.println("Error sending message: " + e);
                e.printStackTrace();
            }
        }
    }

    private void exitChat() {
        try {
            h.exit(u, this);
        } catch (RemoteException e) {
            System.err.println("Error exiting chat: " + e);
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> dispose());
    }

    @Override
    public void onEnter(User u) throws RemoteException {
        SwingUtilities.invokeLater(() -> this.getChatArea().append(u.getUserName() + " entered the chat!\n"));
    }

    @Override
    public void onExit(User u) throws RemoteException {
        SwingUtilities.invokeLater(() -> this.getChatArea().append(u.getUserName() + " left the chat!\n"));
    }

    @Override
    public void onMessage(User u, String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> this.getChatArea().append( message + "\n"));
    }

    @Override
    public String toString() {
        return this.u.getUserName();
    }

    public static void main(String[] args) {
        host = args[0];
        port = Integer.parseInt(args[1]);
        new UserClient();
    }
}
