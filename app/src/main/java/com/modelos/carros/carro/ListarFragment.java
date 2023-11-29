package com.modelos.carros.carro;

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
        View v = inflater.inflate(R.layout.carro_fragment_listar, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerViewCarros = v.findViewById(R.id.recyclerViewCarro);

        CollectionReference collectionCarro = db.collection("Carros");
        collectionCarro.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
                    recyclerViewCarros.setLayoutManager(manager);
                    recyclerViewCarros.addItemDecoration(new DividerItemDecoration(v.getContext(), LinearLayoutManager.VERTICAL));
                    recyclerViewCarros.setHasFixedSize(true);
                    List<Carro> carros = task.getResult().toObjects(Carro.class);
                    List<String> carrosIds = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        carrosIds.add(document.getId());
                    }
                    CarroAdapter adapterCarros = new CarroAdapter(carros, carrosIds, getActivity());
                    recyclerViewCarros.setAdapter(adapterCarros);
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as carros!", Toast.LENGTH_LONG).show();
                    Log.d("ListarCarro", "mensagem de erro: ", task.getException());
                }
            }
        });
        return v;
    }
}