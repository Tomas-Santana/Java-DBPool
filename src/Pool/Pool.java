package Pool;

import java.util.ArrayList;
import PropertyHandler.PropertyHandler;

public enum Pool {
    INSTANCE;
    private PropertyHandler prop = new PropertyHandler("src/Pool/Pool.properties");

    protected int MAX_CONN = Integer.parseInt(prop.getProp("MAX_CONN", "100"));
    
    protected int GROWTH_RATE = Integer.parseInt(prop.getProp("GROWTH_RATE", "10"));

    protected int MAX_FREE_CONN = Integer.parseInt(prop.getProp("MAX_FREE_CONN", "20"));

    protected int BASE_CONN = Integer.parseInt(prop.getProp("BASE_CONN", "20"));

    protected ArrayList<DBConn> connections = new ArrayList<DBConn>();

    private Pool() {
        for (int i = 0; i < BASE_CONN; i++) {
            DBConn conn = new DBConn(i);
            connections.add(conn);
        }
    }
}
