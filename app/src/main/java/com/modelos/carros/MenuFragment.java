package com.modelos.carros;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_marca) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameMain, new com.modelos.carros.marca.MainFragment())
                    .commit();
            return true;
        } else if (id == R.id.menu_modelo) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameMain, new com.modelos.carros.modelo.MainFragment())
                    .commit();
            return true;
        } else if (id == R.id.menu_carro) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameMain, new com.modelos.carros.carro.MainFragment())
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}