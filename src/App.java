
public class App {
    public static void main(String[] args) throws Exception {
        int numClients = 10;

        ClientThread[] clients = new ClientThread[numClients];

        for (int i = 0; i < numClients; i++) {
            ClientThread client = new ClientThread("SISTEMA_TESIS", i);
            clients[i] = client;
        }
        for (int i = 0; i < numClients; i++) {
            clients[i].start();
        }
    }
}
