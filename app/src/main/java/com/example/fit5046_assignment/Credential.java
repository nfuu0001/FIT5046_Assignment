package com.example.fit5046_assignment;

public class Credential {
    private Integer credentialId;
    private String username;
    private String passwordHash;
    private String signUpDate;

    public Credential(){

    }
    public Credential(Integer credentialId, String username, String passwordHash, String signUpDate){
        this.credentialId = credentialId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.signUpDate = signUpDate;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public String getUsername() {
        return username;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }
}
