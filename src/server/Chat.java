package server;

import client.User;
import client.UserClient;

import java.rmi.RemoteException;
import java.sql.Array;
import java.util.ArrayList;

public class Chat implements ChatInterface {

    ArrayList<String> usernames = new ArrayList<>();

    @Override
    public int enter(User u) throws RemoteException {
        if (usernames.contains(u.getUserName())) {
            return -1;
        }
        usernames.add(u.getUserName());
        System.out.println(u.getUserName() + " entered the chat!");
        return 0;
    }

    @Override
    public int exit(User u) throws RemoteException {
        usernames.remove(u.getUserName());
        System.out.println(u.getUserName() + " left the chat!");
        return 0;
    }

    @Override
    public void say(User u, String message) throws RemoteException {
        System.out.println(u.getUserName() + ": " + message);
    }
}
