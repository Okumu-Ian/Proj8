package com.c323proj8.yourname.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.c323proj8.yourname.R;
import com.c323proj8.yourname.models.MessageModel;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    private List<MessageModel> messages;
    private Context context;
    private String uid;

    public MessageAdapter(List<MessageModel> messages, Context context, String uid) {
        this.messages = messages;
        this.context = context;
        this.uid = uid;
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView message_text_view;
        private LinearLayout layout;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            message_text_view = itemView.findViewById(R.id.txt_message);
            layout = itemView.findViewById(R.id.message_row);
        }
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageHolder(LayoutInflater.from(context).inflate(R.layout.message_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        MessageModel messageModel = messages.get(position);
        if(messageModel.getMessage_from().equals(uid)){
            holder.layout.setBackgroundResource(R.drawable.shape_bg_outgoing_bubble);
            setMargins(holder.layout,350,5,2,5);
            holder.message_text_view.setText(messageModel.getMessage_content());

        }else{
            holder.layout.setBackgroundResource(R.drawable.shape_bg_incoming_bubble);
            setMargins(holder.layout,2,5,350,5);
            holder.message_text_view.setText(messageModel.getMessage_content());
        }
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        ///if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.setLayoutParams(p);
        //}
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
