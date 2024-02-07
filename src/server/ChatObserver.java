package server;

import client.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatObserver extends Remote {
    void onEnter(User u) throws RemoteException;
    void onExit(User u) throws RemoteException;
    void onMessage(User u, String message) throws RemoteException;
}
