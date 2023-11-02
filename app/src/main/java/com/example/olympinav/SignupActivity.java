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
import com.example.olympinav.Utils.MyApp;

public class SignupActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    EditText username = findViewById(R.id.username);
    EditText password = findViewById(R.id.password);
    TextView errorText = findViewById(R.id.errorText);
    Button signupButton = findViewById(R.id.signup);
    signupButton.setOnClickListener(v -> {
      errorText.setText("");
      errorText.setVisibility(View.GONE);
      if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
        Toast.makeText(this, "Please enter a username and password to create your account", Toast.LENGTH_LONG).show();
        return;
      }

      AsyncTask.execute(() -> {
        User user = MyApp.getAppDatabase().userDao().checkUsernameIsTaken(username.getText().toString());
        if (user != null) {
          runOnUiThread(() -> {
            errorText.setText("That username is taken, please enter a different username.");
            errorText.setVisibility(View.VISIBLE);
          });
        } else {
          MyApp.getAppDatabase().userDao().insert(new User(username.getText().toString(), password.getText().toString(), 0));
          runOnUiThread(() -> {
            Toast.makeText(this, "Account Successfully Created. Please Login.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
          });
        }
      });
    });
    // Clear incase returning to this activity.
    errorText.setText("");
    errorText.setVisibility(View.GONE);

  }
}
