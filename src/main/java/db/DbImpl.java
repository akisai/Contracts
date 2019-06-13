package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haimin-a on 06.06.2019.
 */
public class DbImpl {

    public List<InputStream> getCa(String caId) throws DbException {
        List<InputStream> streams = new ArrayList<>();
        try ( Connection conn = SqlUtils.getConnection(DataBase.CA) ) {
            try ( PreparedStatement preparedStatement = conn.prepareStatement(SqlQuery.CA)) {
                preparedStatement.setString(1, caId);
                try ( ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("id").equals("")) {
                            throw new SQLException("2");
                        } else if(caId.startsWith("1")) {
                            streams.add(rs.getBinaryStream("eds"));
                            streams.add(rs.getBinaryStream("agreement"));
                        } else if(caId.startsWith("2")) {
                            streams.add(rs.getBinaryStream("eds"));
                            streams.add(rs.getBinaryStream("sign"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        if(streams.isEmpty()) {
            throw new DbException("Cert not found");
        }
        return streams;
    }

    public String getCaId(String iccid) throws DbException {
        String caId = null;
        try ( Connection cn = SqlUtils.getConnection(DataBase.CDI) ) {
            String sql;
            if(iccid.startsWith("02")) {
                sql = SqlQuery.CDI_PHONE;
            } else if (iccid.startsWith("01")) {
                sql = SqlQuery.CDI_MODEM;
            } else {
                throw new SQLException("Wrong iccid");
            }
            try ( PreparedStatement preparedStatement = cn.prepareStatement(sql)) {
                preparedStatement.setString(1, iccid);
                try(ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        caId = rs.getString("ca_id");
                    }
                }
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        if(caId == null) {
            throw new DbException("CaId not found");
        }
        return caId;
    }
}
