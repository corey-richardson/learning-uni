package com.example.productuserssqlite;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.ListView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView productListView;
    private ListView personListView;
    private DatabaseHelper databaseHelper;
    private ProductAdapter productAdapter;
    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String databasePath = getDatabasePath("database.db").getPath();
        Log.d("DatabasePath", "Database file path: " + databasePath);

        // Initialize the ListViews and DatabaseHelper
        productListView = findViewById(R.id.productListView);
        personListView = findViewById(R.id.personListView);
        databaseHelper = new DatabaseHelper(this);
        // Get the list of products and users from the database
        List<Product> productList = databaseHelper.getAllProducts();
        List<Person> personList = databaseHelper.getAllPersons();
        // Set the adapters
        productAdapter = new ProductAdapter(this, productList);
        productListView.setAdapter(productAdapter);
        personAdapter = new PersonAdapter(this, personList);
        personListView.setAdapter(personAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
