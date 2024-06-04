package calculsParalleles;

// Importations
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

import java.util.ArrayList;
import java.util.List;

// Classe Distributor
public class Distributor implements DistributorService {
    // Attributs
    private static final boolean DEBUG = true;
    private List<NodeService> nodes = new ArrayList<>();
    private int currentNode = 0;

    // Méthode registerNode
    @Override
    public void registerNode(NodeService node) {
        // Récupération de l'hôte
        String host = "";
        try{
            host = RemoteServer.getClientHost();
        } catch(ServerNotActiveException e) {
            // Si l'hôte n'est pas trouvé, on quitte
            System.out.println("Erreur : Impossible de récupérer l'hôte");
            System.exit(1);
        }

        // Affichage du noeud enregistré
        if (DEBUG) {
            System.out.println("Noeud enregistré : " + host);
        }

        // Ajout du noeud à la liste
        this.nodes.add(node);
    }

    // Méthode removeNode
    @Override
    public void removeNode(NodeService node) {
        // Enlèvement du noeud de la liste
        this.nodes.remove(node);
    }

    // Méthode getNode
    @Override
    public synchronized NodeService getNode() throws RemoteException {
        // Si DEBUG est activé, on affiche le noeud actuel
        if (DEBUG) {
            System.out.println("Récupération du noeud " + currentNode);
        }
        // Si la liste est vide, on retourne null
        if (nodes.size() == 0) {
            return null;
        }

        NodeService n;
        try {
            // Récupération du noeud actuel
            n = this.nodes.get(currentNode);
        } catch (IndexOutOfBoundsException e) {
            // Si l'index est hors limites, on le remet à 0
            currentNode = 0;
            n = this.nodes.get(currentNode);
        }
        // On passe le noeudActuel au suivant
        currentNode = (currentNode == (nodes.size() - 1)) ? 0 : currentNode + 1;
        return n;
    }

    // Méthode getNodeCount
    @Override
    public int getNodeCount() {
        // Retourne le nombre de noeuds
        return this.nodes.size();
    }
}
