import raytracer.Disp;
import raytracer.Scene;

import calculsParalleles.Customer;
import calculsParalleles.DistributorService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur] [adresseIP] [port]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n\tadresseIp : adresse de l'annuaire (par défaut 127.0.0.1)\n\tport : port de l'annuaire (par défaut 1099)\n";
     
    public static void main(String[] args){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;
        
        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2)
                    hauteur = Integer.parseInt(args[2]);
            }
        }else{
            System.out.println(aide);
        }
        
   
        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);
        
        // Calcul de l'image de la scène les paramètres : 
        // - x0 et y0 : correspondant au coin haut à gauche
        // - l et h : hauteur et largeur de l'image calculée
        // Ici on calcule toute l'image (0,0) -> (largeur, hauteur)
        
        int x0 = 0, y0 = 0;
        int l = largeur, h = hauteur;

        System.out.println("Calcul de l'image :\n - Coordonnées : "+x0+","+y0
                           +"\n - Taille "+ largeur + "x" + hauteur);
        
        // Affichage de l'image calculée
        try {
            // On récupère l'adresse et le port
            String adresse = args.length > 3 ? args[3] : "127.0.0.1";
            int port = 1099;
            if (args.length > 4) {
                port = Integer.parseInt(args[4]);
            }
            // On récupère le registre distant
            Registry reg = LocateRegistry.getRegistry(adresse, port);
            // On récupère le service distant
            DistributorService sd = (DistributorService) reg.lookup("Distributeur");

            // On récupère le nombre de noeuds
            int nodesCount = sd.getNodeCount();
            // Si aucun noeud n'est disponible, on quitte
            if (nodesCount <= 0) {
                System.out.println("Erreur : Aucun noeud disponible");
                System.exit(1);
            }

            // On découpe l'image en fonction du nombre de noeuds
            int baseWidth = l / nodesCount; // largeur de base pour chaque Customer
            int remainder = l % nodesCount; // reste à répartir (si la largeur n'est pas divisible par le nombre de noeuds)

            // On crée un client par noeud
            int currentX = 0;
            for (int i = 0; i < nodesCount; i++) {
                // Calculer la largeur pour ce Customer
                int width = baseWidth + (i < remainder ? 1 : 0); // On ajoute 1 de largeur si on a un reste

                // Créer et démarrer le Customer
                Customer c = new Customer(scene, disp, sd, currentX, y0, width, h);
                c.start();

                // Mettre à jour la position de départ pour le prochain Customer
                currentX += width;
            }

        // Catch des exceptions possibles
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Une IP ou un hôte doit être spécifié en argument");
        } catch (NotBoundException e) {
            System.out.println("Le service distant appelé est introuvable");
        } catch (UnknownHostException e) {
            System.out.println("Serveur inexistant ou introuvable");
        } catch (ConnectException e) {
            System.out.println("Impossible de se connecter à l’annuaire rmiregistry distant");
        } catch (RemoteException e) {
            System.out.println("Impossible de se connecter au serveur distant");
        }
    }	
}
