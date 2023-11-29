package com.modelos.carros.marca;

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
        View v = inflater.inflate(R.layout.marca_fragment_listar, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerViewMarcas = v.findViewById(R.id.recyclerViewMarcas);

        CollectionReference collectionMarca = db.collection("Marcas");
        collectionMarca.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
                    recyclerViewMarcas.setLayoutManager(manager);
                    recyclerViewMarcas.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
                    recyclerViewMarcas.setHasFixedSize(true);
                    List<Marca> marcas = task.getResult().toObjects(Marca.class);
                    List<String> marcasIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        marcasIds.add(document.getId());
                    }
                    MarcaAdapter adapterMarcas = new MarcaAdapter(marcas, marcasIds, getActivity());
                    recyclerViewMarcas.setAdapter(adapterMarcas);
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as marcas!", Toast.LENGTH_LONG).show();
                    Log.d("ListarMarcas", "mensagem de erro: ", task.getException());
                }
            }
        });
        return v;
    }
}