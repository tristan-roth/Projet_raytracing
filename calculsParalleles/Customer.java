package calculsParalleles;

// Importations
import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;

import java.rmi.RemoteException;
import java.time.Duration;
import java.time.Instant;

// Classe Customer
public class Customer extends Thread {
    // Attributs
    Scene scene;
    private DistributorService ds;
    private int x0, y0, l, h;
    private Disp disp;

    // Constructeur
    public Customer(Scene scene, Disp disp, DistributorService ds, int x0, int y0, int l, int h){
        this.scene = scene;
        this.ds = ds;
        this.x0 = x0;
        this.y0 = y0;
        this.l = l;
        this.h = h;
        this.disp = disp;
    }

    // Méthode run
    @Override
    public void run() {
        NodeService ns = null;

        while (true) {
            try {
                // Récupération d'un noeud
                Instant start = Instant.now();
                ns = ds.getNode();
                Instant end = Instant.now();
                long time = Duration.between(start, end).toMillis();
                System.out.println("Noeud récupéré en : " + time + " ms");

                // Si aucun noeud n'est disponible, on quitte
                if (ns == null) {
                    System.out.println("Erreur : Aucun noeud disponible");
                    System.exit(1);
                }

                // Calcul de l'image
                Instant startCalculate = Instant.now();
                Image image = ns.calculateImage(this.scene, this.x0, this.y0, this.l, this.h);
                Instant endCalculate = Instant.now();

                // Si l'image est calculée, on l'affiche
                if (image != null) {
                    long timeCalculate = Duration.between(startCalculate, endCalculate).toMillis();
                    System.out.println("Morceau d'image calculée en : " + timeCalculate + " ms");
                    disp.setImage(image, x0, y0);
                    break;
                }
            } catch (RemoteException e) {
                // Si une exception est levée, on retire le noeud de la liste
                try {
                    ds.removeNode(ns);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
