import java.io.*;

/**
 * Created by haimin-a on 05.06.2019.
 */
public class CheckCert {

    public void checkSert(String ca_id, String savePath) {
        try {
            File inputFile = new File(savePath + "\\0" + ca_id + ".cer");
            File tempFile = new File(savePath + "\\temp0" + ca_id + ".cer");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String lineToRemove = "ext1bw";

            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (!trimmedLine.equals(lineToRemove)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            writer.close();
            reader.close();
            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
