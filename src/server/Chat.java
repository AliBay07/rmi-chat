package server;

import client.User;
import client.UserClient;

import java.rmi.RemoteException;

public class Chat implements ChatInterface {

    @Override
    public int enter(User u) throws RemoteException {
        return 0;
    }

    @Override
    public int exit(User u) throws RemoteException {
        return 0;
    }

    @Override
    public void say(User u, String message) throws RemoteException {
        System.out.println(u.getFirstName() + " : " + message);
    }
}
