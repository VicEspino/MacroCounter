package com.example.macrocounter.UI.GlobalChat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.Message;

import java.util.ArrayList;
import java.util.List;

public class AdapterGlobalChat extends RecyclerView.Adapter<AdapterGlobalChat.ViewHolder> {

    private static final String TAG = AdapterGlobalChat.class.getSimpleName();

    private List<Message> items;

    private AdapterGlobalChat() throws Exception {
        //no using
        throw new Exception("No use this constructor.");
    }

    public AdapterGlobalChat(@NonNull ArrayList<Message> historialLista) {
        super();
        this.items = historialLista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position).getUser().equals(FragmentGlobalChat.userName)){
            return R.layout.item_chat_owned;
        }
        return R.layout.item_chat_external;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message item = items.get(position);

        holder.txtMessage.setText( item.getMessageContent() );
        holder.txtTime.setText(item.getMessageTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMessage;
        TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtMessage = itemView.findViewById(R.id.txt_msg);
            txtTime = itemView.findViewById(R.id.txt_time);

        }
    }

}