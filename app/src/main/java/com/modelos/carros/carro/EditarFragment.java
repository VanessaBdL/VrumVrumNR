package com.modelos.carros.carro;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.modelos.carros.R;
import com.modelos.carros.modelo.Modelo;

public class EditarFragment extends Fragment {

    EditText etNome;
    EditText etRenavam;
    EditText etPlaca;
    EditText etValor;
    EditText etAno;
    Spinner spModelo;
    ArrayList<String> listModeloId;
    ArrayList<String> listModeloName;
    Carro c;
    FirebaseFirestore db;
    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.carro_fragment_editar, container, false);
        etNome = v.findViewById(R.id.editTextNome);
        etRenavam = v.findViewById(R.id.editTextRenavam);
        etPlaca = v.findViewById(R.id.editTextPlaca);
        etValor = v.findViewById(R.id.editTextValor);
        etAno = v.findViewById(R.id.editTextAno);
        spModelo = v.findViewById(R.id.spinnerModelo);

        // id enviado via parâmetro no ListarFragment
        Bundle bundle = getArguments();
        String id_carro = bundle != null ? bundle.getString("id") : null;

        Button btnEditar = v.findViewById(R.id.buttonEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_carro);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_excluir_carro);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluir(id_carro);
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Não faz nada
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        db = FirebaseFirestore.getInstance();

        assert id_carro != null;
        DocumentReference documentCarro = db.collection("Carros").document(id_carro);
        documentCarro.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        c = task.getResult().toObject(Carro.class);
                        assert c != null;
                        etNome.setText(c.getNome());
                        etRenavam.setText(String.valueOf(c.getRenavam()));
                        etPlaca.setText(String.valueOf(c.getPlaca()));
                        etValor.setText(String.valueOf(c.getValor()));
                        etAno.setText(String.valueOf(c.getAno()));
                        spinnerModelo();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao buscar o carro!", Toast.LENGTH_LONG).show();
                        Log.d("ListarCarro", "nenhum documento encontrado");
                    }
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar o carro!", Toast.LENGTH_LONG).show();
                    Log.d("ListarCarro", "erro: ", task.getException());
                }
            }
        });

        return v;
    }

    private void editar (String id) {
        if (spModelo.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione a modelo!", Toast.LENGTH_LONG).show();
        } else if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etRenavam.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o peso!", Toast.LENGTH_LONG).show();
        } else if (etPlaca.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a altura!", Toast.LENGTH_LONG).show();
        } else if (etValor.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a data de nascimento!", Toast.LENGTH_LONG).show();
        } else if (etAno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe a data de nascimento!", Toast.LENGTH_LONG).show();
        } else {
            Carro c = new Carro();
            String nomeModelo = spModelo.getSelectedItem().toString();
            c.setId_modelo((listModeloId.get(listModeloName.indexOf(nomeModelo))));
            c.setNome(etNome.getText().toString());
            c.setRenavam(Integer.parseInt(etRenavam.getText().toString()));
            c.setPlaca(etPlaca.getText().toString());
            c.setValor(Float.parseFloat(etValor.getText().toString()));
            c.setAno(Integer.parseInt(etAno.getText().toString()));

            DocumentReference documentCarro = db.collection("Carros").document(id);
            documentCarro.set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Carro atualizado!", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameCarro, new ListarFragment()).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditarCarro", "erro: ", e);
                        }
                    });

        }
    }

    private void excluir(String id) {
        db.collection("Carros").document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Carro excluído!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameCarro, new ListarFragment()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ExcluirCarro", "erro: ", e);
                    }
                });

    }

    private void spinnerModelo() {
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
                    // seta a seleção do spinner da mãe com os dados que estão no firestore
                    spModelo.setSelection(listModeloId.indexOf(c.getId_modelo()));
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as modelos!", Toast.LENGTH_LONG).show();
                    Log.d("ListarModelo", "mensagem de erro: ", task.getException());
                }
            }
        });
    }
}