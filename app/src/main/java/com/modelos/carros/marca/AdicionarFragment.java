package com.modelos.carros.marca;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.modelos.carros.R;

public class AdicionarFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText etNome;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.marca_fragment_adicionar, container, false);

        db = FirebaseFirestore.getInstance();
        etNome = v.findViewById(R.id.editTextNome);

        Button btnAdicionar = v.findViewById(R.id.buttonAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        return v;
    }

    private void adicionar() {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else {
            Marca m = new Marca();
            m.setNome(etNome.getText().toString());

            CollectionReference collectionMarca = db.collection("Marcas");
            collectionMarca.add(m)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Marca salva!", Toast.LENGTH_LONG).show();
                            // Esta transação deve ocorrer apenas após o sucesso da adição do documento
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frameMarca, new ListarFragment())
                                    .commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro ao salvar a marca!", Toast.LENGTH_LONG).show();
                            Log.d("AdicionarMarca", "mensagem de erro: ", e);
                        }
                    });
        }
    }

}