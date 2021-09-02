package com.example.play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.play.playClass.Account;
import com.example.play.playClass.AccountLocalStore;
import com.example.play.serverCommunication.AsyncServerApi;
import com.example.play.serverCommunication.ServerPath;
import com.example.play.serverCommunication.VolleyCallBack;

import org.json.JSONObject;

public class EditAccountDetails extends AppCompatActivity {
    TextView error;
    ImageView backButton;
    Button deleteAccount;
    Button applyChanges;

    EditText username;
    EditText email;
    EditText password;


    AccountLocalStore accountLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_account_details);

        accountLocalStore = new AccountLocalStore(this);

        initComponents();
    }

    private void initComponents() {
        username = findViewById(R.id.editAccountUsernameInput);
        email = findViewById(R.id.editAccountEmailInput);
        password = findViewById(R.id.editAccountPasswordInput);


        error = findViewById(R.id.editAccountErrorLabel);


        backButton = findViewById(R.id.editAccountDetailsBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        deleteAccount = findViewById(R.id.editAccountDelete);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account currentAccount = accountLocalStore.getLoggedAccount();
                AsyncServerApi.jsonObjectRequest(Request.Method.DELETE, ServerPath.getServerPath() + "api/account/" + currentAccount.getId(),
                        EditAccountDetails.this, null, error, null, new VolleyCallBack() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                accountLocalStore.clearAccountData();
                                accountLocalStore.setAccountLoggedIn(false);
                                startActivity(new Intent(EditAccountDetails.this, LoginActivity.class));
                                finish();
                            }

                            @Override
                            public void onConnectionError(String message) {
                                error.setText(message);
                            }
                        });
            }
        });

        applyChanges = findViewById(R.id.editAccountApplyChanges);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAccount();
            }
        });
    }

    private void editAccount() {
        Account currentAccount = accountLocalStore.getLoggedAccount();
        if(username.getText().length() == 0 && email.getText().length() == 0 && password.getText().length() == 0) {
            error.setText("Account already up to date!");
        } else {
            if(username.getText().length() > 0) {
                if(username.getText().length() > 4) {
                    currentAccount.setUsername(username.getText().toString());
                } else {
                    error.setText("Username should be at least 4 characters long!");
                }
            }
            if(email.getText().length() > 0) {
                currentAccount.setEmail(email.getText().toString());
            }

            if(password.getText().length() > 0) {
                currentAccount.setPassword(password.getText().toString());
            }
            AsyncServerApi.jsonObjectRequest(Request.Method.PUT, ServerPath.getServerPath() + "api/account/" + currentAccount.getId(),
                    EditAccountDetails.this, Account.toJson(currentAccount), error, null, new VolleyCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            accountLocalStore.clearAccountData();
                            accountLocalStore.storeAccountData(currentAccount);
                            startActivity(new Intent(EditAccountDetails.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onConnectionError(String message) {
                            error.setText(message);
                        }
                    });
        }
    }
}