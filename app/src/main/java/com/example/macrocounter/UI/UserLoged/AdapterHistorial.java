package com.example.macrocounter.UI.UserLoged;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macrocounter.R;
import com.example.macrocounter.UI.model.HistorialItem;
import com.example.macrocounter.UI.model.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.ViewHolder> {
    // Attributes and constructor omitted
    private static final String TAG = AdapterHistorial.class.getSimpleName();

    private List<HistorialItem> items;

 //   private ArrayAdapter<HistorialItem> adapter = new ArrayAdapter<HistorialItem>(this,items);


    AdapterHistorial() throws Exception {
        //no using
        throw new Exception("No use this constructor.");
    }
    public AdapterHistorial(@NonNull ArrayList<HistorialItem> historialLista) {
        super();
        this.items = historialLista;
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
        //notifyItemChanged(0);
    }

    public void updateList(int cal){
       /*  Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
       sdf.format(currentTime.getTime());*/


        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MMMM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);


        if(items.isEmpty()){
            items.add(new HistorialItem(cal,thisDate));
            notifyItemInserted(0);

        }
        else{

            HistorialItem itemN;

                if(thisDate.equals(items.get(0).getDate())) {
                    itemN =items.get(0);
                    cal+=itemN.getCalorieAmount();
                    itemN.setCalorieAmount(cal);
                    itemN.setDate(thisDate);
                    notifyItemChanged(0);
                }
                else{
                    items.add(new HistorialItem(cal,thisDate));
                    notifyItemInserted(0);
                }

        }


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