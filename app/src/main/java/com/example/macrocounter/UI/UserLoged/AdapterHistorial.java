package com.example.macrocounter.UI.UserLoged;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.HistorialItem;
import com.example.macrocounter.UI.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.ViewHolder> {
    // Attributes and constructor omitted
    private static final String TAG = AdapterHistorial.class.getSimpleName();

    private final int ITEM_COUNT;
    private List<HistorialItem> items;

    private AdapterHistorial () throws Exception {
        //no using
        throw new Exception("No use this constructor.");
    }
    public AdapterHistorial(@NonNull ArrayList<HistorialItem> historialLista) {
        super();
        this.ITEM_COUNT = historialLista.size();
        this.items = items;
        // Create some items
       /* items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(
                    new HistorialItem( 2312 ,  new Date(65413) )
            );
        }*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistorialItem item = items.get(position);

        holder.txtCalorieAmount.setText((item.getCalorieAmount()+""));
        holder.txtDate.setText(item.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCalorieAmount;
        TextView txtDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCalorieAmount =  itemView.findViewById(R.id.txtCalorieAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
    // ViewHolder definition omitted
}