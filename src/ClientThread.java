import java.util.HashMap;
import java.util.ArrayList;
import PropertyHandler.PropertyHandler;
import Pool.PoolManager;

public class ClientThread extends Thread {
    private static PropertyHandler prop = new PropertyHandler("src/ClientThread.properties");

    private String DBID;

    private int ID;

    private ArrayList<HashMap<String, String>> actions = new ArrayList<HashMap<String, String>>();

    public ClientThread(String DBID, int ID) {
        this.ID = ID;
        this.DBID = DBID;
        for (int i = 0; i<5; i++) {
            HashMap<String, String> action = new HashMap<String, String>();
            action.put("action", prop.getProp("ACTION" + i, "Failed"));
            action.put("value", prop.getProp("VALUE" + i, "No value"));
            actions.add(action);
        }
    }

    @Override
    public void run() {
        PoolManager poolManager = new PoolManager(this.ID);

        poolManager.getConnection(DBID);

        while (true) {
            int actionIndex = (int) (Math.random() * actions.size());
            HashMap<String, String> action = actions.get(actionIndex);

            if (action.get("action").equals("query")) {
                poolManager.executeQuery(action.get("value"));
            } 
            else if (action.get("action").equals("release")) {
                poolManager.releaseConnection();
                break;
            } 
            else if (action.get("action").equals("sleep")) {
                try {
                    Thread.sleep(Integer.parseInt(action.get("value")));
                    System.out.println("Client #" + ID + " slept for " + action.get("value") + "ms");
                } catch (InterruptedException e) {
                    System.err.println("Sleep Failed");
                }
            }
            else {
                System.err.println("Invalid action: " + action.get("action"));
            }
        }
    }

}
