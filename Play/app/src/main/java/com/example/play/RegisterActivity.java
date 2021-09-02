package com.example.play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.play.playClass.Account;
import com.example.play.playClass.AccountLocalStore;
import com.example.play.serverCommunication.AsyncServerApi;
import com.example.play.serverCommunication.ServerPath;
import com.example.play.serverCommunication.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, email,password;
    private Button register, login;
    private TextView error;
    private ProgressBar progressBar;
    AccountLocalStore accountLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        accountLocalStore = new AccountLocalStore(this);

        initComponents();
    }

    private void initComponents() {
        username = findViewById(R.id.usernameRegisterInput);
        email = findViewById(R.id.emailRegisterInput);
        password = findViewById(R.id.passwordRegisterInput);
        error = findViewById(R.id.registerErrorLabel);
        progressBar = findViewById(R.id.progressBarRegister);

        register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });

        login = findViewById(R.id.buttonRegisterLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void registerAccount() {
        if(username.getText().toString().length() < 4 ) {
            error.setText("Username should be at least 4 characters long!");
        } else if(email.getText().toString().isEmpty()) {
            error.setText("Enter email address!");
        } else if(password.getText().toString().length() < 6) {
            error.setText("Password should be at least 6 characters long!");
        } else {
            Account new_account = new Account(username.getText().toString(),
                    email.getText().toString(), password.getText().toString());
            AsyncServerApi.jsonObjectRequest(Request.Method.POST,ServerPath.getServerPath() + "api/account",
                    RegisterActivity.this, Account.toJson(new_account), error, progressBar, new VolleyCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.getJSONObject("json").toString());
                                Account account = Account.fromJson(jsonObject);
                                accountLocalStore.storeAccountData(account);
                                accountLocalStore.setAccountLoggedIn(true);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onConnectionError(String message) {
                            error.setText(message);
                        }
                    });
        }
    }


}