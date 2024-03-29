package com.yota.decoder;

import com.yota.decoder.cert.CertInfo;
import com.yota.decoder.html.HtmlDoc;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class Decoder {
    public Parse create(String type) throws ParseException {
        switch (type) {
            case "cert":
                return new CertInfo();
            case "html":
                return new HtmlDoc();
            default:
                throw new ParseException("Type not found");
        }
    }
}
