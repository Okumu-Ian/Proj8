package com.c323proj8.yourname;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText name;
    private AppCompatButton button;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

    }

    private void initUI(){
        database = openOrCreateDatabase("MESSAGES",MODE_PRIVATE,null);
        createTable();

        name = findViewById(R.id.edt_name);
        button = findViewById(R.id.btn_name_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("MESSAGES",MODE_PRIVATE).edit().putString("UID",
                        Objects.requireNonNull(name.getText()).toString()).apply();
                startActivity(new Intent(MainActivity.this,ContactsActivity.class));
            }
        });

    }

    private void createTable(){
        String createTable = "CREATE TABLE IF NOT EXISTS messages(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "message_from text not null, message_to text not null, message_content text not null)";
        database.execSQL(createTable);
    }

}
