package com.yota.utils;

import com.yota.decoder.cert.OidMap;
import org.bouncycastle.asn1.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class CertUtils {

    private static boolean first = true;
    private static boolean parse = true;

    public static void parseASN1(StringBuilder result, Enumeration e) {
        if (parse) {
            while (e.hasMoreElements()) {
                Object obj = e.nextElement();
                if (obj instanceof ASN1ObjectIdentifier) {
                    result.append(checkId(((ASN1ObjectIdentifier) obj).getId()));
                } else if (obj instanceof DERTaggedObject) {
                    if (((DERTaggedObject) obj).getObject() instanceof DERSequence) {
                        parseASN1(result, ((DERSequence) ((DERTaggedObject) obj).getObject()).getObjects());
                    }
                } else if (obj instanceof DERSequence) {
                    parseASN1(result, ((DERSequence) obj).getObjects());
                } else if (obj instanceof DERSet) {
                    parseASN1(result, ((DERSet) obj).getObjects());
                } else if (obj instanceof DERUTF8String) {
                    if (((DERUTF8String) obj).getString().startsWith("2") || ((DERUTF8String) obj).getString().startsWith("1")) {
                        result.append(((DERUTF8String) obj).getString()).append("\n");
                        parse = false;
                        return;
                    } else {
                        result.append(((DERUTF8String) obj).getString()).append("\n");
                    }
                } else if (obj instanceof ASN1UTCTime) {
                    try {
                        if (first) {
                            result.append("NotBefore: ");
                            result.append(((ASN1UTCTime) obj).getDate().toString()).append("\n");
                            first = false;
                        } else {
                            result.append("NotAfter: ");
                            result.append(((ASN1UTCTime) obj).getDate().toString()).append("\n");
                            first = true;
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else if (obj instanceof DERIA5String) {
                    result.append(((DERIA5String) obj).getString()).append("\n");
                } else if (obj instanceof DERNumericString) {
                    result.append(((DERNumericString) obj).getString()).append("\n");
                } else if (obj instanceof DERPrintableString) {
                    result.append(((DERPrintableString) obj).getString()).append("\n");
                } else if (obj instanceof  ASN1Integer) {
                    if(obj.toString().length() > 5) {
                        result.append("Serial: ").append(new BigInteger(obj.toString()).toString(16)).append("\n");
                    }
                }
            }
        }
    }

    private static String checkId(String id) {
        for (Map.Entry<String, String> entry : OidMap.oids.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return "";
    }
}
