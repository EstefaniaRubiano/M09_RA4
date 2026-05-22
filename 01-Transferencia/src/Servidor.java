import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    
    private ServerSocket serverSocket;

    public Socket connectar() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");
        Socket socket = serverSocket.accept();
        System.out.println("Connexio acceptada: " + socket.getInetAddress());
        return socket;
    }

    public void tancarConnexio(Socket socket) throws IOException {
        try (socket) {
            System.out.println("Tancant connexio amb el client: " + socket.getInetAddress());
        }
        serverSocket.close();
    }

    public void enviarFitxers(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream sortida = new ObjectOutputStream(socket.getOutputStream());

        while (true) {
            System.out.println("Esperant el nom del fitxer del client...");
            String nomFitxer = (String) entrada.readObject();
            System.out.println("Nom fitxer rebut: " + nomFitxer);

            if (nomFitxer == null || nomFitxer.equals("sortir")) {
                System.out.println("Nom del fitxer buit o nul. Sortint...");
                break;
            }

            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] contingut = fitxer.getContinut();
            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
            sortida.writeObject(contingut);
            sortida.flush();
            System.out.println("Fitxer enviat al client: " + nomFitxer);
        }
    }

    public static void main(String[] args) throws Exception {
        Servidor servidor = new Servidor();
        Socket socket = servidor.connectar();
        servidor.enviarFitxers(socket);
        servidor.tancarConnexio(socket);
    }
}
