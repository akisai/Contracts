package decoder.cert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class OidMap {
    public final static Map<String, String> oids = new HashMap<String, String>() {{
        put("1.2.840.113549.1.7.2", "Signed data\n");
        put("1.2.840.113549.1.7.1", "data\n");
        put("1.2.643.100.1", "ОГРН: ");
        put("1.2.643.3.131.1.1", "ИНН: ");
        put("2.5.4.9", "StreetAddress: ");
        put("1.2.840.113549.1.9.1", "E-Mail: ");
        put("2.5.4.6", "CountryName: ");
        put("2.5.4.8", "StateOrProvinceName: ");
        put("2.5.4.7", "LocalityName: ");
        put("2.5.4.10", "OrganizationName: ");
        put("2.5.4.11", "OrganizationalUnitName: ");
        put("2.5.4.3", "CommonName: ");
    }};
}
