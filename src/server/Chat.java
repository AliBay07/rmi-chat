package server;

import client.User;

import java.io.*;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat implements ChatInterface {

    private final List<User> users;
    private final List<ChatObserver> observers;
    private final List<String> messages;
    private final String chatHistoryFile = "chat_history.txt";

    public Chat() throws RemoteException {
        super();
        users = Collections.synchronizedList(new ArrayList<>());
        observers = Collections.synchronizedList(new ArrayList<>());
        messages = Collections.synchronizedList(new ArrayList<>());
        loadChatHistory();
    }

    private void loadChatHistory() {
        try {
            File file = new File(chatHistoryFile);

            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("Chat history file created: " + file.getAbsolutePath());
                } else {
                    System.err.println("Failed to create chat history file.");
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    messages.add(line);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading or creating chat history: " + e);
            e.printStackTrace();
        }
    }

    private void saveChatHistory(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chatHistoryFile, true))) {
                writer.write(message);
                writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing chat history: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public int enter(User u, ChatObserver observer) throws RemoteException {
        for (User user : users) {
            if (user.getUserName().equals(u.getUserName())) {
                return -1;
            }
        }
        users.add(u);
        observers.add(observer);
        for (String message : messages) {
            observer.onMessage(u, message);
        }
        notifyObserversEnter(u);
        return 0;
    }

    @Override
    public int exit(User u, ChatObserver observer) throws RemoteException {
        users.remove(u);
        observers.remove(observer);
        notifyObserversExit(u);
        return 0;
    }

    @Override
    public void say(User u, String message) throws RemoteException {
        String formattedMessage = u.getUserName() + ": " + message;
        messages.add(formattedMessage);
        notifyObserversMessage(u, formattedMessage);
        saveChatHistory(formattedMessage);
    }

    private void handleClientConnectionError(ChatObserver observer) {
        observers.remove(observer);
    }

    private void notifyObserversEnter(User u) throws RemoteException {
        for (ChatObserver observer : observers) {
            try {
                observer.onEnter(u);
            } catch (Exception e) {
                handleClientConnectionError(observer);
            }
        }
    }

    private void notifyObserversExit(User u) throws RemoteException {
        for (ChatObserver observer : observers) {
            try {
                observer.onExit(u);
            } catch (Exception e) {
                handleClientConnectionError(observer);
            }
        }
    }

    private void notifyObserversMessage(User u, String message) throws RemoteException {
        for (ChatObserver observer : observers) {
            try {
                observer.onMessage(u, message);
            } catch (Exception e) {
                handleClientConnectionError(observer);
            }
        }
    }
}
