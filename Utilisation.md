# Notice d'utilisation

## Prérequis
Assurez-vous d'avoir Java installé sur votre machine. Vous pouvez vérifier votre installation en exécutant `java -version` et `javac -version` dans votre terminal.

## Étape 0 : Compilation
Pour compiler le projet, exécutez la commande suivante dans la racine du projet :
```sh
javac *.java calculsParalleles/*.java raytracer/*.java
```

## Étape 1 : Lancer le registre RMI
Dans un terminal, lancez le registre RMI en exécutant :
```sh
rmiregistry
```

## Étape 2 : Démarrer le distributeur
Dans un nouveau terminal, démarrez le distributeur en utilisant la commande suivante :
```sh
java calculsParalleles/StartDistributor <ip> <port>
```
Remplacez `<ip>` et `<port>` par l'adresse IP et le port souhaités.

## Étape 3 : Démarrer les nœuds
Dans autant de terminaux que nécessaire (un terminal par client), démarrez les nœuds en utilisant la commande suivante :
```sh
java calculsParalleles/StartNode <ip> <port>
```
Remplacez `<ip>` et `<port>` par l'adresse IP et le port du distributeur.

## Étape 4 : Lancer le raytracer
Dans un terminal, lancez le raytracer avec la commande suivante :
```sh
java LancerRaytacer <fichier-scène> <largeur> <hauteur> <ip> <port>
```
Remplacez `<fichier-scène>` par le fichier de la scène à rendre, `<largeur>` et `<hauteur>` par les dimensions souhaitées de l'image, et `<ip>` et `<port>` par l'adresse IP et le port du distributeur.
