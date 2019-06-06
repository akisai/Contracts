package db;

import java.sql.SQLException;

/**
 * Created by haimin-a on 06.06.2019.
 */
public class DbException extends SQLException {

    public DbException(String reason) {
        super(reason);
    }

    public DbException() {
    }
}
