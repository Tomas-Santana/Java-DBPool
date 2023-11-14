
public class App {
    public static void main(String[] args) throws Exception {
        int numClients = 3000;

        ClientThread[] clients = new ClientThread[numClients];

        for (int i = 0; i < numClients; i++) {
            ClientThread client = new ClientThread("SISTEMA_TESIS", i);
            clients[i] = client;
        }
        for (int i = 0; i < numClients; i++) {
            clients[i].start();
            Thread.sleep(0);
        }
    }
}
