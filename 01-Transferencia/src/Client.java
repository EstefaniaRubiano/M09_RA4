import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Scanner;

public class Client {
    private static final String DIR_ARRIBADA = "/tmp";  
    
    private Socket socket;
    private ObjectOutputStream sortida;
    private ObjectInputStream entrada;

    public void connectar() throws IOException {
        socket = new Socket("localhost", 9999);
        System.out.println("Connectant a -> localhost:9999");
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        
        sortida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());
    }

    public void rebreFitxers() throws IOException, ClassNotFoundException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
                String nomFitxer = scanner.nextLine();

                sortida.writeObject(nomFitxer);
                sortida.flush();

                if (nomFitxer.equals("sortir")) {
                    System.out.println("Sortint...");
                    break;
                }

                byte[] contingut = (byte[]) entrada.readObject();

                String nomCurt = new File(nomFitxer).getName();
                String ruta = DIR_ARRIBADA + "/" + nomCurt;
                Files.write(Paths.get(ruta), contingut);
                System.out.println("Fitxer rebut i guardat com: " + ruta);
            }
        }
    }

    public void tancarConnexio() throws IOException {
        socket.close();
        System.out.println("Connexio tancada.");
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.connectar();
        client.rebreFitxers();
        client.tancarConnexio();
    }
}