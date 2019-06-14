package config;

/**
 * Created by haimin-a on 14.06.2019.
 */
public class DbConfig {

    private String url;
    private String user;
    private String pass;

    private Boolean lastStatus = false;

    public DbConfig(String url, String user, String pass, Boolean lastStatus) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.lastStatus = lastStatus;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public Boolean getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Boolean lastStatus) {
        this.lastStatus = lastStatus;
    }
}
