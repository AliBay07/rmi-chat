# Application de Chat en Java RMI

## Compilation de l'Application

Pour compiler et tester l'application de chat, suivez les étapes ci-dessous. Assurez-vous de les exécuter dans l'ordre spécifié pour garantir un fonctionnement correct.

### Rmiregistry

Dans un premier terminal, exécutez les commandes suivantes pour démarrer le registre RMI :

```bash
cd path/to/repository
export CLASSPATH=path/to/repository/classes
rmiregistry 6090
```
Cette étape prépare le registre RMI pour les connexions.

### Serveur

Dans un deuxième terminal, compilez et lancez le serveur en utilisant les commandes suivantes :

```bash
cd path/to/repository
export CLASSPATH=path/to/repository/classes
javac -d classes src/server/*.java src/client/*.java
java -Djava.rmi.server.hostname=127.0.0.1 -cp classes server.ChatServer 6090
```
Cela compile le code du serveur et des clients et lance le serveur.

### Clients

Dans un troisième terminal, lancez le client en utilisant les commandes suivantes. Vous pouvez exécuter plusieurs clients en ouvrant des terminaux supplémentaires pour tester la communication entre eux.

```bash
cd path/to/repository
export CLASSPATH=path/to/repository/classes
java -cp classes client.UserClient 127.0.0.1 6090
```
Vous pouvez maintenant observer la communication en temps réel entre le serveur et les clients dans les différents terminaux.

Si vous souhaitez tester l'application avec un serveur et deux clients directement, un script shell est fourni pour simplifier cette tâche. Exécutez simplement les commandes suivantes :

```bash
cd path/to/repository
chmod +x script.sh
./script.sh
```
