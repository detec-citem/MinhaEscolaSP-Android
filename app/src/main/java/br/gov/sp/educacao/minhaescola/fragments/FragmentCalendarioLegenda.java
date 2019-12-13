package br.gov.sp.educacao.minhaescola.fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.gov.sp.educacao.minhaescola.R;

public class FragmentCalendarioLegenda
        extends Fragment {

    public FragmentCalendarioLegenda() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fragment_calendario_legenda, container, false);
    }
}
