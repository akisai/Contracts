package db;

import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haimin-a on 06.06.2019.
 */
public class SqlUtils {

    public static Connection getConnection(DataBase dbName) {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new OracleDriver());
            Map<String, String> properties = getDbFromConfig(dbName);
            conn =  DriverManager.getConnection(
                    properties.get("url"),
                    properties.get("user"),
                    properties.get("pass")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static Map<String, String> getDbFromConfig(DataBase dbName) {
        return new HashMap<>();
    }
}
