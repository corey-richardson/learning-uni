package com.example.productuserssqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void seedProducts(SQLiteDatabase db) {
        addProduct(db, "Laptop", 999.99, 50);
        addProduct(db, "Phone", 599.99, 100);
        addProduct(db, "Headphones", 49.99, 200);
    }

    private void addProduct(SQLiteDatabase db, String productName, double price, int stock) {
        ContentValues values = new ContentValues();
        values.put("productName", productName);
        values.put("price", price);
        values.put("stock", stock);
        db.insert("Product", null, values);
        Log.i("Product Insert", ("Added: " + productName));
    }


    public void seedUsers(SQLiteDatabase db) {
        addUser(db, "Alice Smith", "alice@example.com", "123 Main St, NY");
        addUser(db, "Bob Johnson", "bob@example.com", "456 Oak Ave, CA");
        addUser(db, "Carol Lee", "carol@example.com", "789 Pine Dr, TX");
    }

    private void addUser(SQLiteDatabase db, String username, String email, String address) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("address", address);
        db.insert("User", null, values);
        Log.i("User Insert", ("Added: " + username));
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductTable = "CREATE TABLE Product (" +
                    "id INTEGER PRIMARY KEY, " +
                    "productName TEXT NOT NULL UNIQUE, " +
                    "price REAL NOT NULL, " +
                    "stock INT NOT NULL, " +
                    "CHECK (stock >= 0)" +
                ")";

        String createUserTable = "CREATE TABLE User (" +
                "id INTEGER PRIMARY KEY, " +
                "username TEXT NOT NULL UNIQUE, " +
                "email TEXT NOT NULL UNIQUE, " +
                "address TEXT NOT NULL" +
                ")";

        db.execSQL(createProductTable);
        db.execSQL(createUserTable);

        seedProducts(db);
        seedUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }


    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch all products
        Cursor cursor = db.rawQuery("SELECT * FROM Product", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String productName = cursor.getString(cursor.getColumnIndex("productName"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                int stock = cursor.getInt(cursor.getColumnIndex("stock"));

                // Create a Product object and add it to the list
                Product product = new Product(id, productName, price, stock);
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return productList;
    }

    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM User", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                Person person = new Person(id, username, email, address);
                personList.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return personList;
    }
}
