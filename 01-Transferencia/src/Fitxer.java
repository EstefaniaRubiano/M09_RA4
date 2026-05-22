import java.io.*;
import java.nio.file.Files;

public class Fitxer implements Serializable {
    private final String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    // Comprova que el fitxer existeix i carrega els bytes
    public byte[] getContinut() throws IOException {
        File f = new File(nom);
        if (!f.exists()) {
            throw new FileNotFoundException("No existeix: " + nom);
        }
        this.contingut = Files.readAllBytes(f.toPath());
        return contingut;
    }
}