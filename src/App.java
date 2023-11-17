
public class App {
    public static void main(String[] args) throws Exception {
        int numClients = 1000;

        ClientThread[] clients = new ClientThread[numClients];

        long start = System.currentTimeMillis();
        for (int i = 0; i < numClients; i++) {
            ClientThread client = new ClientThread("SISTEMA_TESIS", i);
            clients[i] = client;
        }
        for (int i = 0; i < numClients; i++) {
            clients[i].start();
            Thread.sleep(0);
        }
        
        while (ClientThread.finished < numClients) {
            Thread.sleep(0);
        }
        
        long end = System.currentTimeMillis();

        System.out.println("Time elapsed: " + (end - start) + "ms");
    }
}
