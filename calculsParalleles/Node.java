package calculsParalleles;

// Importations
import raytracer.Image;
import raytracer.Scene;

import java.io.Serializable;

// Classe Node
public class Node implements NodeService, Serializable {
    // Méthode calculateImage
    public Image calculateImage(Scene scene, int x0, int y0, int width, int height) {
        System.out.println("Calcul de l'image");
        // Calcul de l'image
        return scene.compute(x0, y0, width, height);
    }
}
