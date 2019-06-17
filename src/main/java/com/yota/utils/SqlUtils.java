package com.yota.utils;

import com.yota.config.ConfigException;
import com.yota.config.Configuration;
import com.yota.config.DbConfig;
import com.yota.db.DataBase;
import com.yota.db.DbException;
import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by haimin-a on 06.06.2019.
 */
public class SqlUtils {

    public static Connection getConnection(DataBase dbName) throws DbException {
        Connection conn;
        try {
            DriverManager.registerDriver(new OracleDriver());
            DbConfig dbConfig = getDbFromConfig(dbName);
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + dbConfig.getUrl(),
                    dbConfig.getUser(),
                    dbConfig.getPass()
            );
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return conn;
    }

    private static DbConfig getDbFromConfig(DataBase dbName) throws DbException {
        switch (dbName) {
            case CDI:
                try {
                    return new Configuration().getCdiDb();
                } catch (ConfigException e) {
                    throw new DbException(e.getMessage());
                }
            case CA:
                try {
                    return new Configuration().getCaDb();
                } catch (ConfigException e) {
                    throw new DbException(e.getMessage());
                }
            default:
                throw new DbException("Unknown data base");
        }
    }

    public static boolean testConnection(DataBase dataBase) throws DbException {
        try {
            return getConnection(dataBase).isValid(5);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
