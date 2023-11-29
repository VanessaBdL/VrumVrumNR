package com.modelos.carros.modelo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.modelos.carros.R;

public class AdicionarFragment extends Fragment {

    EditText etNome;
    Spinner spMarca;
    ArrayList<String> listMarcaId;
    ArrayList<String> listMarcaName;
    private FirebaseFirestore db;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.modelo_fragment_adicionar, container, false);

        spMarca = v.findViewById(R.id.spinnerMarca);
        etNome = v.findViewById(R.id.editTextNome);

        db = FirebaseFirestore.getInstance();

        CollectionReference collectionMarcas = db.collection("Marcas");
        collectionMarcas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listMarcaId = new ArrayList<String>();
                    listMarcaName = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listMarcaId.add(document.getId());
                        listMarcaName.add(document.toObject(Modelo.class).getNome());
                    }
                    ArrayAdapter<String> spMarcaArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMarcaName);
                    spMarca.setAdapter(spMarcaArrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as marcas!", Toast.LENGTH_LONG).show();
                    Log.d("ListarMarcas", "mensagem de erro: ", task.getException());
                }
            }
        });

        Button btnSalvar = v.findViewById(R.id.buttonAdicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        return v;
    }

    private void adicionar () {
        if (spMarca.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione a marca!", Toast.LENGTH_LONG).show();
        } else if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else {
            Modelo m = new Modelo();
            String nomeMarca = spMarca.getSelectedItem().toString();
            m.setId_marca((listMarcaId.get(listMarcaName.indexOf(nomeMarca))));
            m.setNome(etNome.getText().toString());

            CollectionReference collectionBebe = db.collection("Modelos");
            collectionBebe.add(m).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "Modelo salvo!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameModelo, new ListarFragment()).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Erro ao salvar o modelo!", Toast.LENGTH_LONG).show();
                    Log.d("AdicionarModelo", "mensagem de erro: ", e);
                }
            });

        }
    }
}