import java.util.concurrent.TimeoutException;
import Pool.PoolManager;
import PropertyHandler.PropertyHandler;

public class ClientThread extends Thread {

    private String DBID;
    private int ID;
    public static int finished = 0;
    private static PropertyHandler prop = new PropertyHandler("src/ClientThread.properties");
    private static String TEST_QUERY = prop.getProp("TEST_QUERY", "SELECT 1");

    public ClientThread(String DBID, int ID) {
        this.ID = ID;
        this.DBID = DBID;
    }

    public void run() {
        PoolManager poolManager = new PoolManager(this.ID);

        try{
            poolManager.conn = PoolManager.getConnection(this.ID);
            poolManager.connect(this.DBID);
        
            poolManager.executeQuery(TEST_QUERY);
            poolManager.releaseConnection(poolManager);
        }
        catch(TimeoutException e) {
            System.out.println("Client #" + this.ID + "couldn't connect to DB" + DBID);
        }
        finished++;

    }

}
