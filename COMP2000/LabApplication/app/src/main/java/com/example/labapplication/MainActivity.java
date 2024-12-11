package com.example.labapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String databasePath = getDatabasePath("users.db").getPath();
        Log.d("DatabasePath", "Database file path: " + databasePath);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleLoginButton(View v)
    {
        EditText editTextUsername = findViewById(R.id.editTextText);
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean authenticated = dbHelper.authenticateUser(username, password);

        if (authenticated) {
            Log.d("Login", "Successful Login");
            Intent iLaunchHomepage = new Intent(this, Homepage.class);
            startActivity(iLaunchHomepage);
        }
        else
        {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
            Log.d("Login", "Failed Login");
        }
    }


    public void handleRegisterButton(View v)
    {
        Intent iLaunchRegister = new Intent(this, Register.class);
        startActivity(iLaunchRegister);
    }


    public void handleForgotPasswordButton(View v)
    {
        EditText editTextUsername = findViewById(R.id.editTextText);
        String username = editTextUsername.getText().toString().trim();
        if (!username.isBlank())
        {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.remindPassword(username);
        }

        Intent iLaunchForgot = new Intent(this, ForgotPassword.class);
        startActivity(iLaunchForgot);
    }
}
