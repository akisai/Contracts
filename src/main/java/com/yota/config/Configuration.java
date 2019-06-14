package com.yota.config;

import com.yota.utils.Utils;

import java.io.*;
import java.util.Base64;

/**
 * Created by haimin-a on 14.06.2019.
 */
public class Configuration {

    private File configFolder;
    private File configFile;
    private Boolean exist;

    private File saveFolder;
    private DbConfig cdiDb;
    private DbConfig caDb;

    public Configuration() throws ConfigException {
        String dir = System.getProperty("user.home") + System.getProperty("file.separator") + ".contracts_1.0";
        this.configFolder = new File(dir);
        readConfig();
    }

    private void checkFoleder() {
        if (!this.configFolder.exists())
            configFolder.mkdir();
    }

    private void readConfig() throws ConfigException {
        checkFoleder();
        this.configFile = new File(configFolder, "conf.prop");
        this.exist = configFile.exists();
        if (exist) {
            getValues();
        }
    }

    private void getValues() throws ConfigException {
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            while ((line = br.readLine()) != null) {
                String decode = new String(Base64.getDecoder().decode(line));
                String[] prop = decode.split("\n");
                for (String string : prop) {
                    String[] strings = string.split("&:");
                    switch (strings[0]) {
                        case "cdiDb":
                            cdiDb = new DbConfig(
                                    strings[1],
                                    strings[2],
                                    strings[3],
                                    Boolean.valueOf(strings[4]));
                            break;
                        case "caDb":
                            caDb = new DbConfig(
                                    strings[1],
                                    strings[2],
                                    strings[3],
                                    Boolean.valueOf(strings[4]));
                            break;
                        case "saveFolder":
                            saveFolder = new File(strings[1]);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    public void updateConfig() throws ConfigException {
        StringBuilder conf = new StringBuilder();
        conf.append("saveFolder&:").append(saveFolder == null ? "path\n" : saveFolder.getPath() + "\n");
        conf.append("cdiDb&:").append(cdiDb == null ? "url&:user&:pass&: \n" : cdiDb.getUrl() + "&:" +
                cdiDb.getUser() + "&:" +
                cdiDb.getPass() + "&:" +
                cdiDb.getLastStatus() + "\n");
        conf.append("caDb&:").append(caDb == null ? "url&:user&:pass&: \n" : caDb.getUrl() + "&:" +
                caDb.getUser() + "&:" +
                caDb.getPass() + "&:" +
                caDb.getLastStatus() + "\n");
        String encode = new String(Base64.getEncoder().encode(conf.toString().getBytes()));
        try {
            Utils.writeFile(configFile, encode);
        } catch (FileNotFoundException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    public Boolean getExist() {
        return exist;
    }

    public File getSaveFolder() {
        return saveFolder;
    }

    public DbConfig getCdiDb() {
        return cdiDb;
    }

    public DbConfig getCaDb() {
        return caDb;
    }

    public void setSaveFolder(File saveFolder) {
        this.saveFolder = saveFolder;
    }

    public void setCdiDb(DbConfig cdiDb) {
        this.cdiDb = cdiDb;
    }

    public void setCaDb(DbConfig caDb) {
        this.caDb = caDb;
    }
}
