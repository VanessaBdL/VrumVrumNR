package com.modelos.carros.modelo;

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

public class ModeloAdapter extends RecyclerView.Adapter<ModeloAdapter.ModeloViewHolder>{
    private final List<Modelo> modelos;
    private final List<String> modeloIds;
    private final FragmentActivity activity;

    ModeloAdapter(List<Modelo> modelos, List<String> modeloIds, FragmentActivity activity){
        this.modelos = modelos;
        this.modeloIds = modeloIds;
        this.activity = activity;
    }

    static class ModeloViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeModeloView;
        private final TextView marcaNomeView;

        ModeloViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeModeloView = itemView.findViewById(R.id.tvListModeloNome);
            marcaNomeView = itemView.findViewById(R.id.tvListModeloMarcaNome);
        }
    }

    @NonNull
    @Override
    public ModeloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_item, parent, false);
        return new ModeloViewHolder(v);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ModeloViewHolder viewHolder, int i) {
        final String id = modeloIds.get(i);
        viewHolder.nomeModeloView.setText(modelos.get(i).getNome());
        viewHolder.marcaNomeView.setText(modelos.get(i).getId_marca());
        Log.i("ModeloAdapter", modelos.get(i).getNome() + " - " + modeloIds.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", id);

                EditarFragment editarFragment = new EditarFragment();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                editarFragment.setArguments(b);
                ft.replace(R.id.frameModelo, editarFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelos.size();
    }
}