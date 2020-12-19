package com.example.macrocounter.UI.UserLoged;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.HistorialItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.ViewHolder> {
    private static final String TAG = AdapterHistorial.class.getSimpleName();

    private List<HistorialItem> items;


    AdapterHistorial() throws Exception {
        //no using
        throw new Exception("No use this constructor.");
    }
    public AdapterHistorial(@NonNull ArrayList<HistorialItem> historialLista) {
        super();
        this.items = historialLista;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_item, parent, false);
        return new ViewHolder(v);
        //notifyItemChanged(0);
    }

    public int updateList(int cal){

        Date todayDate = new Date();

        int calorieCount = cal;

        if(items.isEmpty()){
            items.add(new HistorialItem(cal,todayDate));
            notifyItemInserted(0);

        }
        else{

            HistorialItem itemN;
            int indexToInstert = hoyRegistrado(HistorialItem.FORMAT_DATE_ITEM_HISTORIAL.format(todayDate));

            if(indexToInstert == -1){
                items.add(new HistorialItem(cal,todayDate));
                notifyItemInserted(items.size());
                return calorieCount;
            }

            itemN =items.get(indexToInstert);
            cal+=itemN.getCalorieAmount();
            calorieCount = cal;
            itemN.setCalorieAmount(cal);
            notifyItemChanged(indexToInstert);

        }

        return calorieCount;

    }


    public int hoyRegistrado(String date){
        for (int indexHistorial = 0; indexHistorial < items.size(); indexHistorial++) {
            if(items.get(indexHistorial).getHistorialItemTime().equals(date)){
                return indexHistorial;
            }
        }
        return -1;
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
}