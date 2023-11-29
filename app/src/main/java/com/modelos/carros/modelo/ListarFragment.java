package com.modelos.carros.modelo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import com.modelos.carros.R;

public class ListarFragment extends Fragment {

    public ListarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modelo_fragment_listar, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerViewModelos = v.findViewById(R.id.recyclerViewModelo);

        CollectionReference collectionModelo = db.collection("Modelos");
        collectionModelo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
                    recyclerViewModelos.setLayoutManager(manager);
                    recyclerViewModelos.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
                    recyclerViewModelos.setHasFixedSize(true);
                    List<Modelo> modelos = task.getResult().toObjects(Modelo.class);
                    // Obtém os IDs dos documentos das mães
                    List<String> modeloIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        modeloIds.add(document.getId());
                    }
                    ModeloAdapter adapterMoldelos = new ModeloAdapter(modelos, modeloIds, getActivity());
                    recyclerViewModelos.setAdapter(adapterMoldelos);
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as modelos!", Toast.LENGTH_LONG).show();
                    Log.d("ListarModelo", "mensagem de erro: ", task.getException());
                }
            }
        });
        return v;
    }
}