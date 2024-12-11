package com.example.labapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void logAllUsers() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Log.d("Database", "User: " + name + ", Password: " + password);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    public void insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("password", password);

        try {
            long result = db.insert("users", null, values);
            if (result == -1) {
                // Handle failure case if insert fails
                Toast.makeText(mContext.getApplicationContext(), "Failed to register user.", Toast.LENGTH_LONG).show();
            } else {
                // Success case
                Log.d("Database Entry", String.format("Added %s to database", username));
            }
        } catch (Exception e) {
            // Handle unexpected exceptions
            Log.e("Database Error", "Error during insertion", e);
            Toast.makeText(mContext.getApplicationContext(), "Unexpected error occurred.", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM users WHERE username = \"%s\" AND password = \"%s\"", username, password), null);

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public void remindPassword(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT password FROM users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndex("password"));
            Log.d("RemindPassword", password);
        } else {
            Log.d("RemindPassword", "No user found with that username");
        }

        cursor.close();
        db.close();
    }

}
