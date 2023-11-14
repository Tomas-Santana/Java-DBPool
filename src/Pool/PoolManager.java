package Pool;

import java.sql.ResultSet;

public class PoolManager {

    private static Pool pool = Pool.INSTANCE;
    public DBConn conn;
    public int ID;

    public PoolManager(int ID){
        this.ID = ID;
    }

    public static synchronized DBConn getConnection(String DBID, int PMID) {
        while (true) {
            for (DBConn connection : pool.connections) {
                if (connection.isAvailable) {
                    System.out.println("Pool manager #" + PMID + " got connection "+ connection.connID +" from pool, free connections: " + (getFreeConnections() - 1) + " Pool size: " + pool.connections.size());
                    PoolManager.tryShrinkPool(PMID);
                    connection.Connect(DBID);
                    return connection;
                }
            }
            tryExtendPool(PMID);
            continue;
        }
    }
    public static boolean releaseConnection(PoolManager PMI) {
        if (PMI.conn.Disconnect()) {
            PoolManager.tryShrinkPool(PMI.ID);
            System.out.println("Pool manager #" + PMI.ID + " released connection,Pool size: " + pool.connections.size());
            PMI.conn = null;
            return true;
        }
        return false;
    }

    private static synchronized boolean tryExtendPool(int PMID) {
        if (pool.connections.size() < pool.MAX_CONN) {
            for (int i = 0; i < pool.GROWTH_RATE; i++) {
                DBConn conn = new DBConn(i + pool.connections.size());
                pool.connections.add(conn);
            }
            System.out.println("Pool manager #" + PMID + " extended pool. free connections: " + getFreeConnections() + " Pool size: " + getFreeConnections());
            return true;
        }
        return false;
    }
    private static synchronized boolean tryShrinkPool(int PMID) {
        int freeConnections = getFreeConnections();

        if (freeConnections > pool.MAX_FREE_CONN) {
            int numToRemove = pool.GROWTH_RATE;

            for (int i = 0; i < numToRemove; i++) {
                for (DBConn conn : pool.connections) {
                    if (conn.isAvailable) {
                        pool.connections.remove(conn);
                        
                        break;
                    }
                }
            }
            System.out.println("Pool manager #" + PMID + " shrinked pool, free connections: " + getFreeConnections() + " Pool size: " + pool.connections.size());
            return true;
        }
        return false;
    }
    private static synchronized int getFreeConnections() {
        int freeConnections = 0;
        for (DBConn conn : pool.connections) {
            if (conn.isAvailable) {
                freeConnections++;
            }
        }
        return freeConnections;
    }
    public static synchronized boolean isPoolFull() {
        return pool.connections.size() == pool.MAX_CONN && getFreeConnections() == 0;
    }
    public boolean executeQuery(String query) {
        System.out.println("Pool manager #" + ID + " executing query.");
        return conn.QueryFromString(query);
    }
    public ResultSet getResultSet() {
        return conn.getResultSet();
    }

}
