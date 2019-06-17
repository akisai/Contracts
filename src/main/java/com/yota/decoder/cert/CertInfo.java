package com.yota.decoder.cert;

import com.yota.decoder.Parse;
import com.yota.decoder.ParseException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import com.yota.utils.CertUtils;
import com.yota.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Enumeration;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class CertInfo implements Parse {

    @Override
    public void parse(InputStream inputStream, File saveFolder) throws ParseException {

        StringBuilder result = new StringBuilder();

        try {
            final InputStream in = Base64.getMimeDecoder().wrap(inputStream);
            final ASN1InputStream asn1In = new ASN1InputStream(in);
            final ASN1Primitive certInfo = asn1In.readObject();
            final ASN1Sequence seq = ASN1Sequence.getInstance(certInfo);
            Enumeration enumeration = seq.getObjects();

            CertUtils.parseASN1(result, enumeration);

            Utils.writeFile(new File(saveFolder + "_certificate.txt"), result.toString());
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
