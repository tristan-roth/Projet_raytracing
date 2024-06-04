package calculsParalleles;

// Importations
import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface NodeService
public interface NodeService extends Remote {
    // Méthode calculateImage
    Image calculateImage(Scene scene, int x0, int y0, int w, int h) throws RemoteException;
}