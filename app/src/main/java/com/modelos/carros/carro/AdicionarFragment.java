package com.modelos.carros.carro;

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
import com.modelos.carros.modelo.Modelo;

public class AdicionarFragment extends Fragment {

    EditText etNome;
    EditText etRenavam;
    EditText etPlaca;
    EditText etValor;
    EditText etAno;
    FirebaseFirestore db;
    Spinner spModelo;
    ArrayList<String> listModeloId;
    ArrayList<String> listModeloName;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.carro_fragment_adicionar, container, false);

        db = FirebaseFirestore.getInstance();

        spModelo = v.findViewById(R.id.spinnerModelo);
        etNome    = v.findViewById(R.id.editTextNome);
        etRenavam = v.findViewById(R.id.editTextRenavam);
        etPlaca = v.findViewById(R.id.editTextPlaca);
        etValor = v.findViewById(R.id.editTextValor);
        etAno = v.findViewById(R.id.editTextAno);


        CollectionReference collectionModelo = db.collection("Modelos");
        collectionModelo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listModeloId = new ArrayList<String>();
                    listModeloName = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listModeloId.add(document.getId());
                        listModeloName.add(document.toObject(Modelo.class).getNome());
                    }
                    ArrayAdapter<String> spModeloArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listModeloName);
                    spModelo.setAdapter(spModeloArrayAdapter);
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as modelos!", Toast.LENGTH_LONG).show();
                    Log.d("ListarModelo", "mensagem de erro: ", task.getException());
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
        if (spModelo.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione o modelo!", Toast.LENGTH_LONG).show();
        } else if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        }else if (etRenavam.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o renavam!", Toast.LENGTH_LONG).show();
        }else if (etPlaca.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a placa!", Toast.LENGTH_LONG).show();
        }else if (etValor.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o valor!", Toast.LENGTH_LONG).show();
        }else if (etAno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o ano!", Toast.LENGTH_LONG).show();
        } else {
            Carro c = new Carro();
            String nomeModelo = spModelo.getSelectedItem().toString();
            c.setId_modelo((listModeloId.get(listModeloName.indexOf(nomeModelo))));
            c.setNome(etNome.getText().toString());
            c.setRenavam(Integer.parseInt(etRenavam.getText().toString()));
            c.setValor(Integer.parseInt(etValor.getText().toString()));
            c.setAno(Integer.parseInt(etAno.getText().toString()));
            c.setPlaca(etPlaca.getText().toString());

            CollectionReference collectionCarro = db.collection("Carros");
            collectionCarro.add(c).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "Bebê salvo!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameCarro, new ListarFragment()).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Erro ao salvar o carro!", Toast.LENGTH_LONG).show();
                    Log.d("AdicionarCarro", "mensagem de erro: ", e);
                }
            });

        }
    }
}