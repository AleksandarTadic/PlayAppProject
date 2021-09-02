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

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login, register;
    private TextView error;
    private ProgressBar progressBar;
    AccountLocalStore accountLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        accountLocalStore = new AccountLocalStore(this);

        initComponents();

        if(accountLocalStore.getAccountLoggedIn()) {
            Account logged = accountLocalStore.getLoggedAccount();
            JSONObject accountLogin = new JSONObject();
            try {
                accountLogin.put("email", logged.getEmail());
                accountLogin.put("password", logged.getPassword());
            } catch (JSONException e) {

            }
            AsyncServerApi.jsonObjectRequest(Request.Method.POST, ServerPath.getServerPath() + "api/login",
                    LoginActivity.this, accountLogin, error, progressBar, new VolleyCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            accountLocalStore.setAccountLoggedIn(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onConnectionError(String message) {
                            error.setText(message);
                        }
                    });
        }
    }

    private void initComponents() {
        email = findViewById(R.id.emailLoginInput);
        password = findViewById(R.id.passwordLoginInput);
        error = findViewById(R.id.loginErrorLabel);
        progressBar = findViewById(R.id.progressBarLogin);
        login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        register = findViewById(R.id.buttonLoginRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginAccount() {
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            error.setText("Enter your email and password!");
        } else {
            JSONObject accountLogin = new JSONObject();
            try {
                accountLogin.put("email", email.getText().toString());
                accountLogin.put("password", password.getText().toString());
            } catch (JSONException e) {

            }
            AsyncServerApi.jsonObjectRequest(Request.Method.POST,ServerPath.getServerPath() + "api/login",
                    LoginActivity.this, accountLogin, error, progressBar, new VolleyCallBack() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.getJSONObject("json").toString());
                                Account account = Account.fromJson(jsonObject);
                                accountLocalStore.storeAccountData(account);
                                accountLocalStore.setAccountLoggedIn(true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onConnectionError(String message) {
                            error.setText(message);
                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}