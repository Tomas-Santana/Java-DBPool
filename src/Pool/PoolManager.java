package Pool;

import java.sql.ResultSet;
import java.util.concurrent.TimeoutException;
import java.lang.Exception;

public class PoolManager {
    private static Pool pool = Pool.INSTANCE;
    public DBConn conn;
    public int ID;

    public PoolManager(int ID){
        this.ID = ID;
    }
    public static synchronized DBConn getConnection(int PMID) throws TimeoutException {
        int retries = 0;  
        while (retries < Pool.MAX_RETRIES) {
            for (DBConn connection : pool.connections) {
                if (connection.isFree) {
                    System.out.println("Pool manager #" + PMID + " got connection from pool, free connections: " + (getFreeConnections() - 1) + " Pool size: " + pool.connections.size());
                    connection.book();
                    PoolManager.tryShrinkPool(PMID);
                    return connection;
                }
            }
            tryExtendPool(PMID);
            retries++;
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                System.err.println("Sleep Error");
            }
        }
        throw new TimeoutException("Pool Manager #" + PMID + "exceded max retries. Giving up...");
    }
    public synchronized boolean releaseConnection(PoolManager PM) {
        if (PM.conn.Disconnect()) {
            System.out.println("Pool manager #" + PM.ID + " released connection,Pool size: " + pool.connections.size());
            PoolManager.tryShrinkPool(PM.ID);
            PM.conn = null;
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
                    if (conn.isFree) {
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
            if (conn.isFree) {
                freeConnections++;
            }
        }
        return freeConnections;
    }
    public boolean connect(String DBID) {
        return conn.Connect(DBID);
    }
    public static synchronized boolean isPoolFull() {
        return pool.connections.size() == pool.MAX_CONN && getFreeConnections() == 0;
    }
    public boolean executeQuery(String query) {
        // System.out.println("Pool manager #" + ID + " executing query.");
        return conn.QueryFromString(query);
    }
    public ResultSet getResultSet() {
        return conn.getResultSet();
    }

}
