package decoder.cert;

import javafx.util.Pair;
import org.bouncycastle.asn1.*;

import java.text.ParseException;
import java.util.Enumeration;

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
                            result.append("NotBefore:\n");
                            result.append(((ASN1UTCTime) obj).getDate().toString()).append("\n");
                            first = false;
                        } else {
                            result.append("NotAfter:\n");
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
                }
            }
        }
    }

    private static String checkId(String id) {
        for(Pair<String, String> oid: OidMap.oids) {
            if(oid.getKey().equals(id)) {
                return oid.getValue();
            }
        }
        return "";
    }
}
