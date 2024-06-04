package calculsParalleles;

// Importations
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Classe StartNode
public class StartNode {
    // Méthode main
    public static void main(String[] args) {
        Registry reg = null;
        DistributorService distributor = null;

        try {
            if (args.length != 2) {
                // Si le nombre d'arguments est incorrect
                // Affichage des valeurs par défaut
                System.out.println("Nombre d'arguments incorrect. Utilisation : java StartNode <adresse> <port>");
                System.out.println("Utilisation des valeurs par défaut : ");
                System.out.println("\t- Adresse IP de l'annuaire : 127.0.0.1");
                System.out.println("\t- Port de l'annuaire : 1099");
                System.out.println();
                reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            } else {
                // Récupération du registre
                reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            }
        } catch (RemoteException e) {
            System.out.println("Erreur : Connexion au registre impossible");
            System.exit(1);
        }

        // Récupération du distributeur
        try {
            distributor = (DistributorService) reg.lookup("Distributeur");
        } catch (java.rmi.NotBoundException e) {
            // Si le service n'existe pas
            System.out.println("Erreur : Service non trouvé");
            System.exit(1);
        } catch (java.rmi.ConnectException e) {
            // Si la connexion est impossible
            System.out.println("Erreur : Connexion au registre impossible");
            System.exit(1);
        } catch (RemoteException e) {
            // Si la connexion est impossible
            System.out.println("Erreur : Connexion au serveur impossible");
            System.exit(1);
        } catch (java.lang.IllegalArgumentException e) {
            // Si le port est invalide
            System.out.println("Erreur : Port invalide");
            System.exit(1);
        }

        // Création d'un noeud
        Node node = new Node();
        try {
            // Enregistrement du noeud
            distributor.registerNode((NodeService) UnicastRemoteObject.exportObject(node, 0));
        } catch (RemoteException e) {
            // Si le noeud n'est pas accessible
            System.out.println("Erreur : Noeud non accessible");
            e.printStackTrace();
            System.exit(1);
        }


    }
}
