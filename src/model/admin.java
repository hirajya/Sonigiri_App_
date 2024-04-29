package model;

public class admin {
    private int admin_Id;
    private String admin_FName;
    private String admin_MName;
    private String admin_LName;
    private String admin_ContactInfo;
    private String admin_Username;
    private String admin_Password;

    public admin(int admin_Id, String admin_FName, String admin_MName, String admin_LName, String admin_ContactInfo, String admin_Username, String admin_Password) {
        this.admin_Id = admin_Id;
        this.admin_FName = admin_FName;
        this.admin_MName = admin_MName;
        this.admin_LName = admin_LName;
        this.admin_ContactInfo = admin_ContactInfo;
        this.admin_Username = admin_Username;
        this.admin_Password = admin_Password;
    }

    public int getAdmin_Id() {
        return admin_Id;
    }

    public void setAdmin_Id(int admin_Id) {
        this.admin_Id = admin_Id;
    }

    public String getAdmin_FName() {
        return admin_FName;
    }

    public void setAdmin_FName(String admin_FName) {
        this.admin_FName = admin_FName;
    }

    public String getAdmin_MName() {
        return admin_MName;
    }

    public void setAdmin_MName(String admin_MName) {
        this.admin_MName = admin_MName;
    }

    public String getAdmin_LName() {
        return admin_LName;
    }

    public void setAdmin_LName(String admin_LName) {
        this.admin_LName = admin_LName;
    }

    public String getAdmin_ContactInfo() {
        return admin_ContactInfo;
    }

    public void setAdmin_ContactInfo(String admin_ContactInfo) {
        this.admin_ContactInfo = admin_ContactInfo;
    }

    public String getAdmin_Username() {
        return admin_Username;
    }

    public void setAdmin_Username(String admin_Username) {
        this.admin_Username = admin_Username;
    }

    public String getAdmin_Password() {
        return admin_Password;
    }

    public void setAdmin_Password(String admin_Password) {
        this.admin_Password = admin_Password;
    }
    
}
