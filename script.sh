#!/bin/bash

CURRENT_DIR=$(pwd)

export CLASSPATH=$CURRENT_DIR/classes

gnome-terminal -- bash -c "javac -d classes src/server/*.java src/client/*.java; export CLASSPATH=$CLASSPATH; echo 'Démarrage du rmiregistry sur le port 6090'; rmiregistry 6090; exec bash"

sleep 2

gnome-terminal -- bash -c "export CLASSPATH=$CLASSPATH; echo 'Démarrage du serveur de chat...'; java -Djava.rmi.server.hostname=127.0.0.1 -cp $CLASSPATH server.ChatServer 6090; exec bash"

gnome-terminal -- bash -c "export CLASSPATH=$CLASSPATH; echo 'Démarrage du premier client de chat...'; java -cp $CLASSPATH client.UserClient 127.0.0.1 6090; exec bash"

gnome-terminal -- bash -c "export CLASSPATH=$CLASSPATH; echo 'Démarrage du second client de chat...'; java -cp $CLASSPATH client.UserClient 127.0.0.1 6090; exec bash"

