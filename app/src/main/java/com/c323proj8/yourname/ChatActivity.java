package com.c323proj8.yourname;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c323proj8.yourname.adapters.MessageAdapter;
import com.c323proj8.yourname.models.MessageModel;
import com.c323proj8.yourname.services.RefresherService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<MessageModel> models;
    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;
    private SQLiteDatabase database;
    String uid;
    String to_id;
    MessageAdapter adapter;
    RefresherService refresherService;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
    }

    private void initUI(){

        refresherService = new RefresherService();
        intent = new Intent(this,refresherService.getClass());


        database = openOrCreateDatabase("MESSAGES",MODE_PRIVATE,null);

        SharedPreferences preferences = getSharedPreferences("MESSAGES",MODE_PRIVATE);
        uid = preferences.getString("UID","UID");
        to_id = preferences.getString("TO_ID","TO_ID");

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(to_id);

        recyclerView = findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        models = new ArrayList<>();
        button = findViewById(R.id.btn_send);
        editText = findViewById(R.id.edt_message);

        button.setEnabled(false);

        textChangeListener();

        retrieveMessages(to_id,uid);

        intent.putExtra("FROM",uid);
        intent.putExtra("TO",to_id);
        intent.putExtra("COUNT",models.size());
        startService(intent);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(to_id,uid,editText.getText().toString());
                models.clear();
                retrieveMessages(to_id,uid);
            }
        });

    }

    private void textChangeListener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 0) button.setEnabled(false); else button.setEnabled(true);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) button.setEnabled(false); else button.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) button.setEnabled(false); else button.setEnabled(true);
            }
        });
    }

    public void retrieveMessages(String to, String fro){

        String fetchQuery = "SELECT * FROM messages WHERE (message_to = ? AND message_from = ?) OR (message_from = ? AND message_to = ?)";
        Cursor messages = database.rawQuery(fetchQuery,new String[]{to,fro,to,fro});
        if(messages!=null){
            if(messages.moveToFirst()){
                do{
                    MessageModel model = new MessageModel();
                    model.setMessage_id(messages.getInt(0));
                    model.setMessage_from(messages.getString(1));
                    model.setMessage_to(messages.getString(2));
                    model.setMessage_content(messages.getString(3));
                    models.add(model);
                }while (messages.moveToNext());
            }
            messages.close();
            adapter = new MessageAdapter(models, ChatActivity.this, uid);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(models.size() - 1);
        }
    }
    private void sendMessage(String to, String fro, String content){

        String addQuery = "INSERT into messages(message_from,message_to,message_content) VALUES (?,?,?)";
        database.execSQL(addQuery,new Object[]{fro,to,content});
        editText.setText("");
    }

    @Override
    public void onBackPressed() {
        stopService(intent);
        super.onBackPressed();
    }
}
