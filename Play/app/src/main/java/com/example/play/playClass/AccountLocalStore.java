package com.example.play.playClass;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountLocalStore {
    public static final String SP_NAME = "accountDetails";
        SharedPreferences accountLocalDatabase;

        public AccountLocalStore(Context context) {
            accountLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        }

        public void storeAccountData(Account account) {
            SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
            spEditor.putInt("id", account.getId());
            spEditor.putString("username", account.getUsername());
            spEditor.putString("email", account.getEmail());
            spEditor.putString("password", account.getPassword());
            spEditor.putBoolean("active", account.isActive());
            spEditor.apply();
        }

        public Account getLoggedAccount() {
            Integer id = accountLocalDatabase.getInt("id", -1);
            String username = accountLocalDatabase.getString("username", "");
            String email = accountLocalDatabase.getString("email", "");
            String password = accountLocalDatabase.getString("password", "");
            boolean active = accountLocalDatabase.getBoolean("active", true);
            Account storedAccount = new Account(id, username, email, password, active);
            return storedAccount;
        }

        public void setAccountLoggedIn(boolean loggedIn) {
            SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
            spEditor.putBoolean("loggedIn", loggedIn);
            spEditor.apply();
        }

        public boolean getAccountLoggedIn() {
            if(accountLocalDatabase.getBoolean("loggedIn", false) == true) {
                return true;
            } else {
                return false;
            }
        }

        public void clearAccountData() {
            SharedPreferences.Editor spEditor = accountLocalDatabase.edit();
            spEditor.clear();
            spEditor.apply();
        }
}
