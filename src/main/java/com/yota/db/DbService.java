package com.yota.db;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by haimin-a on 14.06.2019.
 */
public interface DbService {

    Map<String, InputStream> getCa(String caId) throws DbException;

    String getCaId(String iccid) throws DbException;
}
