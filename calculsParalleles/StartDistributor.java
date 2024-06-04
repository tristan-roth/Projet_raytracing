package calculsParalleles;

// Importations
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Classe StartDistributor
public class StartDistributor {
    // Méthode main
    public static void main(String[] args) {
        // Adresse et port par défaut
        int port = 1099;
        String adress = "127.0.0.1";

        try {
            if (args.length != 2) {
                // Si le nombre d'arguments est incorrect
                // Affichage des valeurs par défaut
                System.out.println("Nombre d'arguments incorrect. Utilisation : java StartDistributor <adresse> <port>");
                System.out.println("Utilisation des valeurs par défaut : ");
                System.out.println("\t- Adresse IP de l'annuaire : 127.0.0.1");
                System.out.println("\t- Port de l'annuaire : 1099");
            } else {
                // Récupération de l'adresse et du port
                adress = args[0];
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException e) {
            // Si le port n'est pas un entier
            System.out.println("Erreur : Le port doit être un entier");
            System.exit(1);
        }

        // Création d'un distributeur
        Distributor distributor = null;
        try {
            distributor = new Distributor();
        } catch (java.lang.NoClassDefFoundError e) {
            System.out.println("Erreur : La classe Distributor n'est pas trouvée");
            System.exit(1);
        }

        // Exportation du distributeur
        DistributorService distributorService;
        Registry reg;
        try {
            // Exportation du distributeur
            distributorService = (DistributorService) UnicastRemoteObject.exportObject(distributor, 0);
            reg = LocateRegistry.getRegistry(adress, port);
            reg.rebind("Distributeur", distributorService);

        } catch (java.rmi.ConnectException e) {
            // Si l'annuaire n'est pas accessible
            System.out.println("Erreur : Impossible de se connecter à l'annuaire");
            System.exit(1);
        } catch (AccessException e) {
            // Si l'accès est refusé
            System.out.println("Erreur : Accès refusé");
            System.exit(1);
        } catch (RemoteException e) {
            // Si une erreur survient lors de l'exportation
            System.out.println("Erreur : Erreur lors de l'exportation du distributeur");
            System.exit(1);
        }
    }
}
