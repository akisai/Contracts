package decoder.cert;

import decoder.Parse;
import decoder.ParseException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Enumeration;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class CertInfo implements Parse {

    @Override
    public void parse(InputStream inputStream) throws ParseException {
        try {
            InputStream in = Base64.getMimeDecoder().wrap(inputStream);
            ASN1InputStream asn1In = new ASN1InputStream(in);
            ASN1Primitive certInfo = asn1In.readObject();
            ASN1Sequence seq = ASN1Sequence.getInstance(certInfo);
            Enumeration first = seq.getObjects();
            StringBuilder result = new StringBuilder();

            CertUtils.parseASN1(result, first);
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
