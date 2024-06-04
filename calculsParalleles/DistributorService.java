package calculsParalleles;

// Importations
import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface DistributorService
public interface DistributorService extends Remote {
    // Méthodes

    // Enregistrer un noeud
    void registerNode(NodeService noeud) throws RemoteException;
    
    // Retirer un noeud
    void removeNode(NodeService noeud) throws RemoteException;

    // Récupérer un noeud
    NodeService getNode() throws RemoteException;

    // Récupérer le nombre de noeuds
    int getNodeCount() throws RemoteException;
}