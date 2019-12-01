package com.example.entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

public class Credential implements Serializable {
    private String username;
    private String passwordhash;
    private Date signupdate;
    private  Users userid;

    public Credential(){}

    public Credential(String username, String passwordhash, Date signupdate, Users userid) {
        this.username = username;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public void hashPassword(){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(getPasswordhash().getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            setPasswordhash(hexString.toString());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }
}
