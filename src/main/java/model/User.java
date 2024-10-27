package model;

public class User {
    private String username;
    private String password;
    private String fname;
    private String lname;
    public User() {
    }
    // Constructor
    public User(String username, String password, String fname, String lname) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getFname() {return fname;}
    public String getLname() {return lname;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {this.password = password;}
    public void setFname(String fname) {this.fname = fname;}
    public void setLname(String lname) {this.lname = lname;}
}
