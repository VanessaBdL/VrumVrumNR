package com.modelos.carros.carro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.modelos.carros.R;
import com.modelos.carros.modelo.Modelo;

public class CarroAdapter extends RecyclerView.Adapter<com.modelos.carros.carro.CarroAdapter.CarroViewHolder>{
    private final List<Carro> carros;
    private final List<String> carrosIds;
    private final FragmentActivity activity;

    CarroAdapter(List<Carro> carros, List<String> carrosIds, FragmentActivity activity){
        this.carros = carros;
        this.carrosIds = carrosIds;
        this.activity = activity;
    }

    static class CarroViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeView;
        private final TextView nomeModeloView;
        private final TextView nomeModeloMarcaView;

        CarroViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeView = itemView.findViewById(R.id.tvListCarroNome);
            nomeModeloView = itemView.findViewById(R.id.tvListCarroModeloNome);
            nomeModeloMarcaView = itemView.findViewById(R.id.tvListCarroModeloMarcaNome);
        }
    }

    @NonNull
    @Override
    public CarroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carro_item, parent, false);
        return new CarroViewHolder(v);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CarroViewHolder viewHolder, int i) {
        final String id = carrosIds.get(i);
        viewHolder.nomeView.setText(carros.get(i).getNome());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentModelo = db.collection("Modelos").document(String.valueOf(carros.get(i).getId_modelo()));
        documentModelo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Modelo m = task.getResult().toObject(Modelo.class);
                        viewHolder.nomeModeloView.setText(m.getNome());
                        viewHolder.nomeModeloMarcaView.setText(m.getNome());

                    } else {
                        Toast.makeText(activity, "Erro ao buscar o nome da modelo!", Toast.LENGTH_LONG).show();
                        Log.d("ListarModelo", "nenhum documento encontrado");
                    }
                } else {
                    Toast.makeText(activity, "Erro ao buscar o nome da modelo!", Toast.LENGTH_LONG).show();
                    Log.d("ListarModelo", "erro: ", task.getException());
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", id);

                EditarFragment editarFragment = new EditarFragment();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                editarFragment.setArguments(b);
                ft.replace(R.id.frameCarro, editarFragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carros.size();
    }
}