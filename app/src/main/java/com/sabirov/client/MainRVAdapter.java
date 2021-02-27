package com.sabirov.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.ViewHolder> {
    public MainRVAdapter(MainActivity mainActivity, List<com.sabirov.client.Message> messages) {
        this.mainActivity=mainActivity;
        this.messages=messages;
    }

    private MainActivity mainActivity;
    private List<Message> messages;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mainActivity).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(messages.get(position).getText1());
        holder.textView2.setText(messages.get(position).getText2());
        holder.textView3.setText(messages.get(position).getText3());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> newList){
        messages=newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1, textView2, textView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.textView1);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
        }
    }
}
