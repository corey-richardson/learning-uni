package com.example.labapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleRegistrationButton(View v)
    {
        EditText editTextUsername = findViewById(R.id.editTextRegisterUsername);
        EditText editTextPassword = findViewById(R.id.editTextRegisterPassword);
        EditText editTextConfirmPassword = findViewById(R.id.editTextRegisterConfirmPassword);

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmedPassword = editTextConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmedPassword))
        {
            Toast.makeText(Register.this, "Passwords don't match. Try again.", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(Register.this);
        dbHelper.insertUser(username, password);

        Intent iLaunchLogin = new Intent(this, MainActivity.class);
        startActivity(iLaunchLogin);
    }
}