package com.example.play.playClass;

import org.json.JSONObject;

public class Account {
    private int id;
    private String username;
    private String email;
    private String password;
    private boolean active;

    public Account() {
    }

    public Account(int id, String username, String email, String password, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public Account(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = true;
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Account fromJson(JSONObject json) {
        Account account = new Account();
        try {
            if(json.has("id")) {
                account.id = json.getInt("id");
            }
            if(json.has("username")) {
                account.username = json.getString("username");
            }
            if(json.has("email")) {
                account.email = json.getString("email");
            }
            if(json.has("password")) {
                account.password = json.getString("password");
            }
            if(json.has("active")) {
                if(json.getInt("active") == 1) {
                    account.active = true;
                } else {
                    account.active = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    public static JSONObject toJson(Account account) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", account.getId());
            object.put("username", account.getUsername());
            object.put("email", account.getEmail());
            object.put("password", account.getPassword());
            object.put("active", account.isActive());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;

    }


}
