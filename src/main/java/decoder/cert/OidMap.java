package decoder.cert;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class OidMap {
    public final static List<Pair<String, String>> oids = Arrays.asList(
            new Pair<>("1.2.840.113549.1.7.2", "Signed data\n"),
            new Pair<>("1.2.840.113549.1.7.1", "data\n"),
            new Pair<>("1.2.643.100.1", "ОГРН\n"),
            new Pair<>("1.2.643.3.131.1.1", "ИНН\n"),
            new Pair<>("2.5.4.9", "StreetAddress\n"),
            new Pair<>("1.2.840.113549.1.9.1", "E-Mail\n"),
            new Pair<>("2.5.4.6", "CountryName\n"),
            new Pair<>("2.5.4.8", "StateOrProvinceName\n"),
            new Pair<>("2.5.4.7", "LocalityName\n"),
            new Pair<>("2.5.4.10", "OrganizationName\n"),
            new Pair<>("2.5.4.11", "OrganizationalUnitName\n"),
            new Pair<>("2.5.4.3", "CommonName\n")
            );
}
