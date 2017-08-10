package businessObjects;

/**
 * Created by sarathan732 on 8/9/2017.
 */

public class FormDataLogin {
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String userid;
    private String password;
    private String country;
}