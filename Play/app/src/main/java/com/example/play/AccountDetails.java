package com.example.play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.play.playClass.Account;
import com.example.play.playClass.AccountLocalStore;

public class AccountDetails extends AppCompatActivity {
    ImageView backButton;
    TextView edit;
    ImageView accountImage;
    TextView username;
    TextView email;

    Button logout;

    AccountLocalStore accountLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_details);

        accountLocalStore = new AccountLocalStore(this);

        initComponents();
    }

    public void initComponents() {
        backButton = findViewById(R.id.accountDetailsBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edit = findViewById(R.id.accountDetailsEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetails.this, EditAccountDetails.class));

            }
        });

        logout = findViewById(R.id.accountDetailsLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLocalStore.setAccountLoggedIn(false);
                accountLocalStore.clearAccountData();
                startActivity(new Intent(AccountDetails.this, LoginActivity.class));
                finish();
            }
        });

        Account account = accountLocalStore.getLoggedAccount();

        accountImage = findViewById(R.id.accountDetailsImage);

        username = findViewById(R.id.accountDetailsUsername);
        username.setText(account.getUsername());

        email = findViewById(R.id.accountDetailsEmail);
        email.setText(account.getEmail());
    }
}