package com.yota.decoder.html;

import com.yota.decoder.Parse;
import com.yota.decoder.ParseException;
import com.yota.utils.Utils;

import java.io.*;

/**
 * Created by haimin-a on 13.06.2019.
 */
public class HtmlDoc implements Parse {

    @Override
    public void parse(InputStream inputStream, File saveFolder) throws ParseException {

        CharArrayWriter writer = new CharArrayWriter();
        char[] unicodeChar = new char[4];

        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ) {
            int d;
            while ((d = reader.read()) != -1) {
                if (d == 38) {
                    int next;
                    if ((next = reader.read()) == 35) {
                        reader.read(unicodeChar);
                        reader.skip(1L);
                        String charCode = new String(unicodeChar);
                        char ch = Character.toChars(Integer.parseInt(charCode))[0];
                        writer.append(ch);
                    } else {
                        writer.append((char) d);
                        writer.append((char) next);
                    }
                } else {
                    writer.append((char) d);
                }
            }
            String encodedDocument = new String(writer.toCharArray());

            Utils.writeFile(new File(saveFolder + "_agreement.doc"), encodedDocument);
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
