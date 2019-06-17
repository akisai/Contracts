package com.yota.decoder;

import java.io.File;
import java.io.InputStream;

/**
 * Created by haimin-a on 13.06.2019.
 */
public interface Parse {

    void parse(InputStream inputStream, File saveFolder) throws ParseException;
}
