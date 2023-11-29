package com.modelos.carros.marca;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.modelos.carros.R;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MarcaViewHolder>{
    private final List<Marca> marcas;
    private final List<String> marcasIds;
    private final FragmentActivity activity;

    MarcaAdapter(List<Marca> marcas, List<String> marcasIds, FragmentActivity activity){
        this.marcas = marcas;
        this.marcasIds = marcasIds;
        this.activity = activity;
    }

    static class MarcaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeMarcaView;

        MarcaViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeMarcaView = itemView.findViewById(R.id.tvListMarcaNome);
        }
    }

    @NonNull
    @Override
    public MarcaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marca_item, parent, false);
        return new MarcaViewHolder(v);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MarcaViewHolder viewHolder, int i) {
        final String id = marcasIds.get(i);
        viewHolder.nomeMarcaView.setText(marcas.get(i).getNome());
        Log.i("MarcaAdapter", marcas.get(i).getNome() + " - " + marcasIds.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", id);

                EditarFragment editarFragment = new EditarFragment();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                editarFragment.setArguments(b);
                ft.replace(R.id.frameMarca, editarFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return marcas.size();
    }
}