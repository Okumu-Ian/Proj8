package com.c323proj8.yourname.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.c323proj8.yourname.ChatActivity;
import com.c323proj8.yourname.R;
import com.c323proj8.yourname.models.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{

    private List<ContactModel> contacts;
    private Context context;

    public ContactAdapter(List<ContactModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView contact_name;
        private CardView cardView;
        private AppCompatImageView imageView;
        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            contact_name = itemView.findViewById(R.id.txt_contact_name);
            cardView = itemView.findViewById(R.id.card_row);
            imageView = itemView.findViewById(R.id.img_open);
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(context).inflate(R.layout.contacts_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        final ContactModel contactModel = contacts.get(position);
        holder.contact_name.setText(contactModel.getContact_name());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getSharedPreferences("MESSAGES",Context.MODE_PRIVATE).edit().putString("TO_ID",contactModel.getContact_name()).apply();
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });
        holder.contact_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getSharedPreferences("MESSAGES",Context.MODE_PRIVATE).edit().putString("TO_ID",contactModel.getContact_name()).apply();
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.getSharedPreferences("MESSAGES",Context.MODE_PRIVATE).edit().putString("TO_ID",contactModel.getContact_name()).apply();
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
