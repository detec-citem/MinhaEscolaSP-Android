package br.gov.sp.educacao.minhaescola.view;

import android.Manifest;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

public class SelecaoPerfilActivity
        extends AppCompatActivity {

    int PERMISSION_ALL = 1;

    public String[] permissions = {

                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            };

    MyPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_splash));
        }

        setContentView(R.layout.activity_selecao_perfil);

        if(!hasPermissions(this, permissions)) {

            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }

        contentOnCreate();
    }

    public void contentOnCreate() {

        mPref = new MyPreferences(getApplicationContext());
    }

    public void selecionarPerfil(View v) {

        if(v.getId() == R.id.perfil_imgAluno
                || v.getId() == R.id.perfil_txtAluno) {

            mPref.perfilAluno();

            startActivity(new Intent(SelecaoPerfilActivity.this, LoginAlunoActivity.class));

            finish();
        }
        else if(v.getId() == R.id.perfil_imgResp
                || v.getId() == R.id.perfil_txtResp) {

            mPref.perfilResponsavel();

            startActivity(new Intent(SelecaoPerfilActivity.this, LoginResponsavelActivity.class));

            finish();
        }
    }

    @Override
    public void onBackPressed() {

        return;
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    public boolean hasPermissions(Context context, String... permissions) {

        if (context != null
                && permissions != null) {

            for(int i = 0; i < permissions.length; i++) {

                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        permissions = null;
    }
}
