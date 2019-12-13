package br.gov.sp.educacao.minhaescola.view;

import android.content.Intent;

import android.net.Uri;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import br.gov.sp.educacao.minhaescola.R;

public class EsqueceuSenhaActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_esqueceu_senha);

        contentOnCreate();
    }

    private void contentOnCreate() {

    }

    public void voltarLogin(View v) {

        onBackPressed();
    }

    public void abrirSed(View v) {

        Uri uri = Uri.parse(getString(R.string.link_sed));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }
}
