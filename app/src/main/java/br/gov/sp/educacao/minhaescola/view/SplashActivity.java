package br.gov.sp.educacao.minhaescola.view;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

public class SplashActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_splash));
        }

        contentOnCreate();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void contentOnCreate() {

        UsuarioQueries usuarioQueries = new UsuarioQueries(getApplicationContext());

        final Intent intent;

        if(usuarioQueries.temUsuarioLogado()) {

            intent = new Intent(this, MenuActivity.class);
        }
        else {

            intent = new Intent(this, SelecaoPerfilActivity.class);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    TimeUnit.SECONDS.sleep(2);

                    startActivity(intent);

                    finish();
                }
                catch (InterruptedException e) {

                    startActivity(new Intent(intent));

                    finish();
                }
            }
        }).start();
    }
}
