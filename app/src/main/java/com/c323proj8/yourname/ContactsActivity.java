package com.c323proj8.yourname;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.c323proj8.yourname.adapters.ContactAdapter;
import com.c323proj8.yourname.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private List<ContactModel> models;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setTitle(getSharedPreferences("MESSAGES",MODE_PRIVATE).getString("UID",null));
        initUI();
    }

    private void initUI(){
        models = new ArrayList<>();
        recyclerView = findViewById(R.id.contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        checkPermission();
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},20);
        }else{
            retrieveAllContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            retrieveAllContacts();
        }else{
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void retrieveAllContacts(){
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        assert cursor!= null;
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                int hasPhone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(hasPhone > 0){
                    ContactModel model = new ContactModel();
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phone = null;
                    Cursor phoneNumberCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[]{id},
                            null);
                    if(phoneNumberCursor != null){
                        if(phoneNumberCursor.moveToNext()){
                            phone = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNumberCursor.close();
                        }
                    }

                    model.setContact_id(id);
                    model.setContact_name(name.split(" ")[0]);
                    if(phone != null){
                        model.setContact_number(phone);
                    }
                    models.add(model);
                }
            }
            adapter = new ContactAdapter(models,this);
            recyclerView.setAdapter(adapter);
        }
        cursor.close();
    }
}
