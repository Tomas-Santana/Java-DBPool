import PropertyHandler.PropertyHandler;
import Pool.PoolManager;

public class ClientThread extends Thread {

    private String DBID;
    private int ID;
    private static String[] actions = {"query", "release", "sleep", "sleep", "sleep", "query", "query", "query"};

    public ClientThread(String DBID, int ID) {
        this.ID = ID;
        this.DBID = DBID;
    }

    @Override
    public void run() {
        PoolManager poolManager = new PoolManager(this.ID);

        poolManager.conn = PoolManager.getConnection(this.DBID, this.ID);
        while (true) {
            int actionIndex = (int) (Math.random() * actions.length);
            String action = actions[actionIndex]; 

            if (action.equals("query")) {
                poolManager.executeQuery("SELECT 1");
            } 
            else if (action.equals("release")) {
                PoolManager.releaseConnection(poolManager);
                break;
            } 
            else if (action.equals("sleep")) {
                try {
                    Thread.sleep(100);
                    System.out.println("Pool Manager #" + ID + " slept for 100 ms");
                } catch (InterruptedException e) {
                    System.err.println("Sleep Failed");
                }
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Sleep Failed");
            }
        }
    }

}
