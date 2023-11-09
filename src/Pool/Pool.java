package Pool;

import java.util.ArrayList;
import PropertyHandler.PropertyHandler;

public enum Pool {
    INSTANCE;
    private PropertyHandler prop = new PropertyHandler("src/Pool/Pool.properties");

    protected int MAX_CONN = Integer.parseInt(prop.getProp("MAX_CONN", "100"));
    
    protected int GROWTH_RATE = Integer.parseInt(prop.getProp("GROWTH_RATE", "10"));

    protected ArrayList<DBConn> connections = new ArrayList<DBConn>();

    private Pool() {
        String baseConnString = prop.getProp("BASE_CONN", "20");
        int baseConn = Integer.parseInt(baseConnString);

        for (int i = 0; i < baseConn; i++) {
            DBConn conn = new DBConn();
            connections.add(conn);
        }
    }
}
