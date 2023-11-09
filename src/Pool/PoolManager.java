package Pool;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.xml.transform.Result;

public class PoolManager {

    private Pool pool = Pool.INSTANCE;
    private DBConn conn;
    private int ID;

    public PoolManager(int ID){
        this.ID = ID;
    }

    public synchronized boolean getConnection(String DBID) {
        while (true) {
            for (DBConn connection : pool.connections) {
                if (connection.isAvailable && connection.Connect(DBID)) {
                    this.conn = connection;

                    System.out.println("Pool manager #" + ID + " got connection from pool, free connections: " + getFreeConnections() + " Pool size: " + pool.connections.size());

                    tryShrinkPool();
                    return true;
                }
            }
            tryExtendPool();
            continue;
        }
    }
    public synchronized boolean releaseConnection() {
        if (conn.Disconnect()) {
            tryShrinkPool();
            this.conn = null;
            return true;
        }
        return false;
    }
    private synchronized boolean tryExtendPool() {
        if (pool.connections.size() < pool.MAX_CONN) {
            for (int i = 0; i < pool.GROWTH_RATE; i++) {
                DBConn conn = new DBConn();
                pool.connections.add(conn);
            }
            System.out.println("Pool manager #" + ID + " extended pool, new size: " + pool.connections.size() + " Free connections: " + getFreeConnections());
            return true;
        }
        return false;
    }
    private synchronized boolean tryShrinkPool() {
        if (getFreeConnections() < pool.MAX_CONN - pool.GROWTH_RATE*2) {
            for (int i = 0; i < pool.GROWTH_RATE; i++) {
                pool.connections.remove(pool.connections.size() - 1);
            }
            System.out.println("Pool manager #" + ID + " shrinked pool, new size: " + pool.connections.size() + " Free connections: " + getFreeConnections());
            return true;
        }
        return false;
    }
    private synchronized int getFreeConnections() {
        int freeConnections = 0;
        for (DBConn conn : pool.connections) {
            if (conn.isAvailable) {
                freeConnections++;
            }
        }
        return freeConnections;
    }
    public boolean executeQuery(String query) {
        System.out.println("Pool manager #" + ID + " executing query.");
        return conn.QueryFromString(query);
    }
    public ResultSet getResultSet() {
        return conn.getResultSet();
    }

}
