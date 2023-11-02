package com.example.olympinav;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.olympinav.DB.User;
import com.example.olympinav.DB.UserWithTicketsAndEvents;
import com.example.olympinav.Utils.MyApp;

public class LoginActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    EditText username = findViewById(R.id.username);
    EditText password = findViewById(R.id.password);
    TextView errorText = findViewById(R.id.errorText);
    Button createAccountButton = findViewById(R.id.signup);
    createAccountButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    Button loginButton = findViewById(R.id.login);
    loginButton.setOnClickListener(v -> {
      errorText.setText("");
      errorText.setVisibility(View.GONE);
      if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
        Toast.makeText(this, "Please enter your username and password to login", Toast.LENGTH_LONG).show();
        return;
      }

      AsyncTask.execute(() -> {
        User user = MyApp.getAppDatabase().userDao().getUserByLoginDetails(username.getText().toString(), password.getText().toString());
        if (user == null) {
          runOnUiThread(() -> {
            errorText.setText("Username and password combination is incorrect, please try again or create an account.");
            errorText.setVisibility(View.VISIBLE);
          });
        } else {
          UserWithTicketsAndEvents loggedInUser =
              MyApp.getAppDatabase().userDao().getUserWithTicketsAndEvents(username.getText().toString());
          if (loggedInUser != null) {
            MyApp.setUser(loggedInUser);
            runOnUiThread(() -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
          } else {
            runOnUiThread(() -> Toast.makeText(this, "An unknown error occurred, please try again.", Toast.LENGTH_LONG).show());
          }
        }
      });
    });

    // Clear incase returning to this activity.
    errorText.setText("");
    errorText.setVisibility(View.GONE);
  }
}
