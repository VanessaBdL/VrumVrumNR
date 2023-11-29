package com.modelos.carros.modelo;

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

public class EditarFragment extends Fragment {

    Modelo m;
    EditText etNome;
    Spinner spMarca;
    ArrayList<String> listMarcaId;
    ArrayList<String> listMarcaName;
    private FirebaseFirestore db;
    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.modelo_fragment_editar, container, false);
        etNome = v.findViewById(R.id.editTextNome);
        spMarca = v.findViewById(R.id.spinnerMarca);

        // id enviado via parâmetro no ListarFragment
        Bundle bundle = getArguments();
        String id_modelo = bundle != null ? bundle.getString("id") : null;

        Button btnEditar = v.findViewById(R.id.buttonEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_modelo);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_excluir_modelo);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluir(id_modelo);
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

        // Obtém os dados do bebê que será editado
        assert id_modelo != null;
        DocumentReference documentModelo = db.collection("Modelos").document(id_modelo);
        documentModelo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        m = task.getResult().toObject(Modelo.class);
                        assert m != null;
                        etNome.setText(m.getNome());
                        spinnerMarcas();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao buscar o modelo!", Toast.LENGTH_LONG).show();
                        Log.d("ListarModelo", "nenhum documento encontrado");
                    }
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar o modelo!", Toast.LENGTH_LONG).show();
                    Log.d("ListarModelo", "erro: ", task.getException());
                }
            }
        });

        return v;
    }

    private void editar (String id) {
        if (spMarca.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Por favor, selecione a mãe!", Toast.LENGTH_LONG).show();
        } else if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else {
            Modelo m = new Modelo();
            String nomeMarca = spMarca.getSelectedItem().toString();
            m.setId_marca((listMarcaId.get(listMarcaName.indexOf(nomeMarca))));
            m.setNome(etNome.getText().toString());

            DocumentReference documentModelo = db.collection("Modelos").document(id);
            documentModelo.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Modelo atualizado!", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameModelo, new ListarFragment()).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("EditarModelo", "erro: ", e);
                        }
                    });

        }
    }

    private void excluir(String id) {
        db.collection("Modelos").document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Modelo excluído!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameModelo, new ListarFragment()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ExcluirModelo", "erro: ", e);
                    }
                });

    }

    private void spinnerMarcas() {
        CollectionReference collectionMarca = db.collection("Marcas");
        collectionMarca.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    // seta a seleção do spinner da mãe com os dados que estão no firestore
                    spMarca.setSelection(listMarcaId.indexOf(m.getId_marca()));
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar as marcas!", Toast.LENGTH_LONG).show();
                    Log.d("ListarMarca", "mensagem de erro: ", task.getException());
                }
            }
        });
    }
}