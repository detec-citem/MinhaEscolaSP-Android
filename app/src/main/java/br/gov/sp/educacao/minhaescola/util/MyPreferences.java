package br.gov.sp.educacao.minhaescola.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private String PREF_NAME = "preferenciaMinhaEscola";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context ctx;

    //Chaves
    public static String PERFIL = "perfilSelec";
    public static String ID_DEVICE = "idDevice";
    public static String TOKEN = "token";

    public static String MOCK_ALUNO = "mock_aluno";
    public static String MOCK_RESPONSAVEL = "mock_responsavel";

    //Valores
    public static int PERFIL_ALUNO = 6;
    public static int PERFIL_RESPONSAVEL = 7;

    public MyPreferences(Context ctx) {

        this.ctx = ctx;
        preferences = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    // Método que retorna o valor do último registro salvo
    public String getPreferenceString(String key)
    {
        return this.preferences.getString(key, "");
    }

    // Método que salva uma nova preferência (RA) na memória.
    public void savePreferenceString(String key, String value){

        this.editor.putString(key, value);
        this.editor.commit();
    }

    public void perfilResponsavel() {

        this.editor.putInt(PERFIL, PERFIL_RESPONSAVEL);
        this.editor.commit();
    }

    public void perfilAluno() {

        this.editor.putInt(PERFIL, PERFIL_ALUNO);
        this.editor.commit();
    }

    public int getPerfilSelecionado() {

        return this.preferences.getInt(PERFIL, 0);
    }

    public void mockAluno() {

        this.editor.putBoolean(MOCK_ALUNO, true);
        this.editor.commit();
    }

    public void mockResponsavel() {

        this.editor.putBoolean(MOCK_RESPONSAVEL, true);
        this.editor.commit();
    }

    public boolean isPerfilMockAluno(){

        return this.preferences.getBoolean(MOCK_ALUNO, false);
    }

    public boolean isPerfilMockResponsavel(){

        return this.preferences.getBoolean(MOCK_RESPONSAVEL, false);
    }

    public void limparPreferencias() {

        this.editor.remove(PERFIL);
        this.editor.remove(ID_DEVICE);
        this.editor.remove(TOKEN);
        this.editor.remove(MOCK_ALUNO);
        this.editor.remove(MOCK_RESPONSAVEL);

        this.editor.commit();
    }
}
