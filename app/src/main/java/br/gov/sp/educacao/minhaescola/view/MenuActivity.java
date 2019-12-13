package br.gov.sp.educacao.minhaescola.view;


import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;


import android.graphics.Point;
import android.net.Uri;

import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.Display;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.adapter.DialogAlunoTurmaAdapter;
import br.gov.sp.educacao.minhaescola.banco.CalendarioQueries;
import br.gov.sp.educacao.minhaescola.banco.CarteirinhaQueries;
import br.gov.sp.educacao.minhaescola.banco.EscolaQueries;
import br.gov.sp.educacao.minhaescola.banco.FrequenciasQueries;
import br.gov.sp.educacao.minhaescola.banco.HorarioAulaQueries;
import br.gov.sp.educacao.minhaescola.banco.NotasQueries;
import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.interfaces.WSCallerVersionListener;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.Dispositivo;
import br.gov.sp.educacao.minhaescola.model.Frequencia;
import br.gov.sp.educacao.minhaescola.model.HorarioAula;
import br.gov.sp.educacao.minhaescola.model.Nota;
import br.gov.sp.educacao.minhaescola.model.Rematricula;
import br.gov.sp.educacao.minhaescola.model.Turma;

import br.gov.sp.educacao.minhaescola.modelTO.FrequenciasTO;
import br.gov.sp.educacao.minhaescola.modelTO.HorarioAulaTO;
import br.gov.sp.educacao.minhaescola.modelTO.NotasTO;
import br.gov.sp.educacao.minhaescola.receivers.AlimentacaoReceiver;

import br.gov.sp.educacao.minhaescola.task.BuscarCalendarioAsyncTask;
import br.gov.sp.educacao.minhaescola.task.BuscarCarteirinhaAsyncTask;
import br.gov.sp.educacao.minhaescola.task.BuscarFotoSobreMimAsyncTask;
import br.gov.sp.educacao.minhaescola.task.BuscarFrequenciaAsyncTask;
import br.gov.sp.educacao.minhaescola.task.BuscarNotasAsyncTask;
import br.gov.sp.educacao.minhaescola.task.EnviarIdAsyncTask;
import br.gov.sp.educacao.minhaescola.task.HorarioAulasAsyncTask;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.avaliar.RateDialogManager;

import br.gov.sp.educacao.minhaescola.view.new_alimentacao.CardapioActivity;
import butterknife.ButterKnife;
import butterknife.BindView;

import io.fabric.sdk.android.Fabric;

import static br.gov.sp.educacao.minhaescola.util.MyPreferences.ID_DEVICE;
import static br.gov.sp.educacao.minhaescola.util.UrlServidor.IS_DEBUG;
import static br.gov.sp.educacao.minhaescola.util.UrlServidor.URLSERVIDOR_TRACKER;
import static br.gov.sp.educacao.minhaescola.util.UrlServidor.URL_SERVIDOR;
import static br.gov.sp.educacao.minhaescola.util.Utils.isOnline;
import static br.gov.sp.educacao.minhaescola.util.Utils.lerArquivoEstatico;

public class MenuActivity
        extends AppCompatActivity
        implements WSCallerVersionListener {

    public String DeviceId;
    public FirebaseAnalytics mFirebaseAnalytics;
    public ProgressDialog progressDialog;

    private MyPreferences mPref;

    public @BindView(R.id.menu_txtMinhaEscola) TextView txtMinhaEscola;
    public @BindView(R.id.menu_alimentacao) RelativeLayout menu_alimentacao;
    public @BindView(R.id.menu_txtSobreMim) TextView txtSobreMim;

    public @BindView(R.id.menu_txt_nome_aluno) TextView txtNomeAluno;
    public @BindView(R.id.menu_txt_nome_turma) TextView txtNomeTurma;

    public @BindView(R.id.menu_relative_sombra) RelativeLayout relativeSombra;

    private AlimentacaoReceiver mAlimentacaoReceiver;
    private Aluno aluno;
    private List<String> alunos;

    private Turma turma;

    boolean isFirstOnStart = true;

    private Turma turmaRegular;

    private EnviarIdAsyncTask enviarIdAsyncTask;

    private UsuarioQueries usuarioQueries;
    private TurmaQueries turmaQueries;
    private CalendarioQueries calendarioQueries;
    private CarteirinhaQueries carteirinhaQueries;
    private EscolaQueries escolaQueries;

    private HorarioAulaQueries horarioAulaQueries;

    private boolean retorno = false;

    private int sizeAlunos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        //new GooglePlayStoreAppVersion(getApplicationContext(), this).execute();

        ButterKnife.bind(this);

        contentOnCreate();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando");
        progressDialog.setTitle("Aguarde...");
        progressDialog.setCancelable(false);

        carteirinhaQueries = new CarteirinhaQueries(this);

        horarioAulaQueries = new HorarioAulaQueries(this);

        RateDialogManager.showRateDialog(this, savedInstanceState);
    }

    private void contentOnCreate() {

        mPref = new MyPreferences(getApplicationContext());

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        escolaQueries = new EscolaQueries(getApplicationContext());

        Fabric.with(this, new Crashlytics());

        String loginCrashlytics = "";

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && usuarioQueries.getResponsavel() != null){

            loginCrashlytics = usuarioQueries.getResponsavel().getLogin();
        }
        else if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && usuarioQueries.getAluno() != null){

            loginCrashlytics = usuarioQueries.getAluno().getLogin();
        }

        Crashlytics.setUserIdentifier(loginCrashlytics);
        Crashlytics.setString("Login ", loginCrashlytics);

        turmaQueries = new TurmaQueries(getApplicationContext());

        calendarioQueries = new CalendarioQueries(getApplicationContext());

        if(!mPref.isPerfilMockResponsavel() || !mPref.isPerfilMockAluno()){

            enviarIdAsyncTask = new EnviarIdAsyncTask(this);
        }

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            //region Tracker Responsavel
            if (URL_SERVIDOR.equals(URLSERVIDOR_TRACKER)) {

                Bundle params = new Bundle();

                mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

                params.putInt("PerfilResponsável", MyPreferences.PERFIL_RESPONSAVEL);
                params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PerfilResponsável");

                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                mFirebaseAnalytics.logEvent("PerfilResponsável", params);
            }
            //endregion

            txtMinhaEscola.setText("escola");

            menu_alimentacao.setVisibility(View.GONE);

            alunos = usuarioQueries.getNomesAlunos();

            sizeAlunos = alunos.size();

            aluno = usuarioQueries.getAlunoSelecionado(alunos.get(0));

            txtNomeAluno.setText(aluno.getNome());

            turmaRegular = turmaQueries.getTurmaAtivaRegular(aluno.getCd_aluno());

            ArrayList<Turma> turmas = turmaQueries.getTurmasAtivas(aluno.getCd_aluno());

            if(turmas.size() > 0){

                turma = turmas.get(0);

                txtNomeTurma.setText(turma.getNome_turma() + "-" + turma.getNome_escola());
            }
            else if(turmas == null){

                txtNomeTurma.setText("");
            }

        }
        else {

            //region Tracker Aluno
            if (URL_SERVIDOR.equals(URLSERVIDOR_TRACKER)) {

                Bundle param = new Bundle();

                mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

                param.putInt("PerfilAluno", MyPreferences.PERFIL_ALUNO);
                param.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PerfilAluno");

                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, param);
                mFirebaseAnalytics.logEvent("PerfilAluno", param);
            }
            //endregion

            aluno = usuarioQueries.getAluno();

            if(!aluno.isEscola_centralizada()) {

                menu_alimentacao.setVisibility(View.GONE);
            }

            txtNomeAluno.setText(aluno.getNome());

            ArrayList<Turma> turmas = turmaQueries.getTurmasAtivas(aluno.getCd_aluno());

            if(turmas == null){

                turma = null;

                txtNomeTurma.setText("");
            }
            else if(turmas.size() > 0){

                turma = turmas.get(0);

                turmaRegular = turmaQueries.getTurmaAtivaRegular(aluno.getCd_aluno());

                txtNomeTurma.setText(turma.getNome_turma() + "-" + turma.getNome_escola());
            }
        }
    }

    public void onClickMudarAlunoTurma(View view){

        AlertDialog alertDialogSelecao;

        List<String> turmasList = new ArrayList<>();

        List<String> alunosList = new ArrayList<>();

        //region changeAluno
        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO){

            final ArrayList<Turma> turmas = turmaQueries.getTurmasAtivas(aluno.getCd_aluno());

            if(turmas.size() == 0){

                return;
            }
            else if(turmas.size() == 1){

                //Toast.makeText(this, "Você só possui uma turma ativa!", Toast.LENGTH_LONG).show();

                return;
            }
            else{

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_selecao_turma_aluno, null);

                mDialogBuilder.setView(viewDialog);

                final Spinner spnTurma = (Spinner) viewDialog.findViewById(R.id.dialog_spn_turma);

                //spnTurma.setDropDownWidth(width - 40);

                LinearLayout linearAluno = (LinearLayout) viewDialog.findViewById(R.id.dialog_layout_aluno);

                linearAluno.setVisibility(View.GONE);

                for (Turma turmaS: turmas) {

                    turmasList.add(turmaS.getNome_turma() + " - " + turmaS.getNome_escola());
                }

                final DialogAlunoTurmaAdapter adapterTurma = new DialogAlunoTurmaAdapter(this, turmas);

                spnTurma.setAdapter(adapterTurma);

                spnTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String nomeTurmaSelecionada = ((TextView) view.findViewById(R.id.dialog_item_turma)).getText().toString();
                        String nomeEscolaSelecionada = ((TextView) view.findViewById(R.id.dialog_item_escola)).getText().toString();

                        for(Turma turmaS : turmas){

                            if(nomeTurmaSelecionada.contains(turmaS.getNome_turma()) &&
                                    nomeEscolaSelecionada.contains(turmaS.getNome_escola())){

                                turma = turmaS;

                                txtNomeTurma.setText(turma.getNome_turma());

                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                mDialogBuilder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        Rematricula rematricula = usuarioQueries.getRematricula(aluno.getCd_aluno());

                        if(aluno.getIdade() >= 18 &&
                                mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO &&
                                aluno.isResponde_rematricula() && rematricula == null &&
                                turmaRegular != null){

                            montarDialogSobreMim();
                        }

                        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && rematricula == null){

                            montarDialogSobreMim();
                        }
                    }
                });

                alertDialogSelecao = mDialogBuilder.create();

                alertDialogSelecao.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                alertDialogSelecao.show();
            }
        }
        //endregion

        //region chanceResponsavel
        else{

            final ArrayList<Aluno> alunos = usuarioQueries.getAlunos();

            if(true){

                List<String> listaNomesAlunos = new ArrayList<>();

                for (Aluno alunoS : alunos) {

                    listaNomesAlunos.add(alunoS.getNome());
                }

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_selecao_turma_aluno, null);

                mDialogBuilder.setView(viewDialog);

                final Spinner spnTurma = (Spinner) viewDialog.findViewById(R.id.dialog_spn_turma);

                //spnTurma.setDropDownWidth(width - 40);

                final Spinner spnAluno = (Spinner) viewDialog.findViewById(R.id.dialog_spn_aluno);

                 //spnAluno.setDropDownWidth(width - 40);

                final ArrayAdapter<String> spnAlunoAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, listaNomesAlunos);

                spnAluno.setAdapter(spnAlunoAdapter);

                spnAluno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String nomeAlunoSeleciado = (String) parent.getItemAtPosition(position);

                        aluno = usuarioQueries.getAlunoSelecionado(nomeAlunoSeleciado);

                        txtNomeAluno.setText(aluno.getNome());

                        final ArrayList<Turma> turmas = turmaQueries.getTurmasAtivas(aluno.getCd_aluno());

                        turmaRegular = turmaQueries.getTurmaAtivaRegular(aluno.getCd_aluno());

                        DialogAlunoTurmaAdapter spnTurmaAdapter = new DialogAlunoTurmaAdapter(MenuActivity.this, turmas);

                        spnTurma.setAdapter(spnTurmaAdapter);


                        spnTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                                String nomeTurmaSelecionada = ((TextView) view.findViewById(R.id.dialog_item_turma)).getText().toString();
                                String nomeEscolaSelecionada = ((TextView) view.findViewById(R.id.dialog_item_escola)).getText().toString();

                                for(Turma turmaS : turmas){

                                    if(nomeTurmaSelecionada.contains(turmaS.getNome_turma()) &&
                                            nomeEscolaSelecionada.contains(turmaS.getNome_escola())){

                                        turma = turmaS;

                                        txtNomeTurma.setText(turma.getNome_turma());

                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                mDialogBuilder.setPositiveButton("Selecionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                txtNomeAluno.setText(aluno.getNome());

                                txtNomeTurma.setText(turma.getNome_turma());

                                alterarTxtSobreMim(aluno.getNome());

                                Rematricula rematricula = usuarioQueries.getRematricula(aluno.getCd_aluno());

                                if(aluno.getIdade() >= 18 &&
                                        mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO &&
                                        aluno.isResponde_rematricula() && rematricula == null){

                                    montarDialogSobreMim();
                                }

                                if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && rematricula == null){

                                    montarDialogSobreMim();
                                }
                            }
                });

                alertDialogSelecao = mDialogBuilder.create();

                alertDialogSelecao.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                alertDialogSelecao.setCanceledOnTouchOutside(false);

                alertDialogSelecao.show();
            }
        }
        //endregion

    }

    private void montarDialogSobreMim() {

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MenuActivity.this).create();
        alertDialog.setMessage("Realize a rematrícula de 2020");

        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Iniciar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        Intent intent;

                        if(carteirinhaQueries.temCarteirinha(aluno.getCd_aluno())){

                            intent = new Intent(MenuActivity.this, SobreMimActivity.class);

                            if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                                intent.putExtra("cd_aluno", aluno.getCd_aluno());
                            }

                            startActivity(intent);
                        }
                        else{

                            if(isOnline(MenuActivity.this)){

                                CarteirinhaQueries carteirinhaQueries = new CarteirinhaQueries(MenuActivity.this);

                                BuscarFotoSobreMimAsyncTask buscarFotoSobreMimAsyncTask = new BuscarFotoSobreMimAsyncTask(MenuActivity.this);

                                buscarFotoSobreMimAsyncTask.execute(aluno.getCd_aluno());

                            }
                            else{

                                intent = new Intent(MenuActivity.this, SobreMimActivity.class);

                                if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                                    intent.putExtra("cd_aluno", aluno.getCd_aluno());
                                }

                                startActivity(intent);
                            }
                        }
                    }
                });

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        alertDialog.show();
    }

    public void enviarIdPush(String idPush) {

        try {

            Dispositivo disp = new Dispositivo(idPush);

            enviarIdAsyncTask.execute(disp);
        }
        catch (Exception e) {

            Log.e("E", "Enviar Push Catch: " + e.toString());
        }
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {

        if (isUpdateAvailable) {

            Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));

            if (isUpdateAvailable) {

                showUpdateDialog();
            }
        }
    }

    public void showUpdateDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuActivity.this);

        alertDialogBuilder.setTitle(MenuActivity.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(MenuActivity.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(R.string.update_now,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        MenuActivity.this.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }

    public void menuOnClickBtn(View view) {

        Intent intent;

        //Tracker Firebase Event
        Bundle params = new Bundle();

        params.putInt("ButtonID", view.getId());

        String btnName = null;

        switch (view.getId()) {

            //region MINHA ESCOLA
            case R.id.menu_escola:

                btnName = "Mód_MinhaEscola";

                if (turma == null) {

                    Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_LONG).show();
                }
                else{

                    intent = new Intent(this, MinhaEscolaActivity.class);

                    intent.putExtra("nome_escola", turma.getNome_escola());

                    if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intent.putExtra("cd_aluno", aluno.getCd_aluno());
                    }
                    startActivity(intent);
                }

                break;
            //endregion

            //region SOBRE_MIM

            case R.id.menu_sobre:

                btnName = "Mód_MenuSobre";

                if(carteirinhaQueries.temCarteirinha(aluno.getCd_aluno())){

                    intent = new Intent(this, SobreMimActivity.class);

                    if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intent.putExtra("cd_aluno", aluno.getCd_aluno());
                    }

                    startActivity(intent);
                }
                else{

                    if(isOnline(this)){

                        CarteirinhaQueries carteirinhaQueries = new CarteirinhaQueries(this);

                        BuscarFotoSobreMimAsyncTask buscarFotoSobreMimAsyncTask = new BuscarFotoSobreMimAsyncTask(this);

                        buscarFotoSobreMimAsyncTask.execute(aluno.getCd_aluno());

                    }
                    else{

                        intent = new Intent(this, SobreMimActivity.class);

                        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                            intent.putExtra("cd_aluno", aluno.getCd_aluno());
                        }


                        startActivity(intent);
                    }
                }

                break;
            //endregion

            //region HORARIO DE AULAS
            case R.id.menu_horario:

                btnName = "Mód_HorárioAula";

                HorarioAulaQueries horarioAulaQueries = new HorarioAulaQueries(this);

                if (isOnline(this)) {

                    if (turma == null) {

                        Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if(mPref.isPerfilMockAluno() || mPref.isPerfilMockResponsavel()){

                            try{

                                HorarioAulaTO horarioAulaTO =
                                        new HorarioAulaTO(new JSONObject(lerArquivoEstatico(R.raw.mock_horario_aula, this).toString()),
                                                aluno.getCd_aluno());

                                ArrayList<HorarioAula> horariosAulas = horarioAulaTO.getHorariosFromJson();

                                horarioAulaQueries.inserirHorariosAulas(horariosAulas);

                                Intent horarioIntent = new Intent(this, HorarioAulasActivity.class);

                                horarioIntent.putExtra("cd_aluno", 26111326);

                                startActivity(horarioIntent);
                            }
                            catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                        else{

                            HorarioAulasAsyncTask horarioAulasAsyncTask = new HorarioAulasAsyncTask(this);

                            horarioAulasAsyncTask.execute(turma.getCd_turma(), aluno.getCd_aluno());
                        }
                    }
                }
                else {

                    if(horarioAulaQueries.temHorarioBanco(aluno.getCd_aluno())) {

                        Intent intentHorario = new Intent(MenuActivity.this,
                                HorarioAulasActivity.class);

                        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                            intentHorario.putExtra("cd_aluno", aluno.getCd_aluno());
                        }

                        startActivity(intentHorario);
                    }
                    else {

                        Toast.makeText(this,
                                "Verifique sua conexão e tente novamente", Toast.LENGTH_LONG).show();

                        esconderSombraCarregamento();

                        break;
                    }
                }

                break;
            //endregion

            //region MINHAS NOTAS

            case R.id.menu_minhas_notas:

                NotasQueries notasQueries = new NotasQueries(getApplicationContext());

                btnName = "Mód_MinhasNotas";

                if (turma == null) {

                    Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_LONG).show();
                    break;
                }

                if (isOnline(getApplicationContext())) {

                    try{

                        if(mPref.isPerfilMockAluno() || mPref.isPerfilMockResponsavel()){

                            NotasTO notasTo = new NotasTO(new JSONArray(lerArquivoEstatico(R.raw.mock_notas_frequencia,this).toString()), 26111326);

                            ArrayList<Nota> notas = notasTo.getNotasFromJson();

                            notasQueries.inserirNotas(notas);

                            Intent notasIntent = new Intent(this, NotasActivity.class);

                            notasIntent.putExtra("cd_aluno", aluno.getCd_aluno());

                            startActivity(notasIntent);
                        }
                        else{

                            BuscarNotasAsyncTask buscarNotasAsyncTask = new BuscarNotasAsyncTask(this);

                            buscarNotasAsyncTask.execute(aluno.getCd_aluno(), turma.getCd_turma());
                        }

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }

                }
                else if (notasQueries.temNotaBanco(aluno.getCd_aluno())) {

                    Intent intentNota = new Intent(MenuActivity.this, NotasActivity.class);

                    if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intentNota.putExtra("cd_aluno", aluno.getCd_aluno());
                    }

                    startActivity(intentNota);
                }
                else {

                    Toast.makeText(this,
                            "Verifique sua conexão e tente novamente", Toast.LENGTH_LONG).show();

                    esconderSombraCarregamento();
                    break;
                }

                break;
            //endregion

            //region FREQUENCIA

            case R.id.menu_frequencia:

                FrequenciasQueries frequenciasQueries = new FrequenciasQueries(getApplicationContext());

                btnName = "Mód_Frequências";

                if (turma == null) {

                    Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_LONG).show();
                    break;
                }

                if(isOnline(getApplicationContext())) {

                    try{

                        if(mPref.isPerfilMockAluno() || mPref.isPerfilMockResponsavel()){

                            FrequenciasTO frequenciasTO =
                                    new FrequenciasTO(new JSONArray(lerArquivoEstatico(R.raw.mock_notas_frequencia,this).toString()), 26111326);

                            ArrayList<Frequencia> frequencias = frequenciasTO.getFrequenciaFromJson();

                            frequenciasQueries.inserirFreq(frequencias);

                            Intent frequenciaIntent = new Intent(this, FrequenciaActivity.class);

                            frequenciaIntent.putExtra("cd_aluno", aluno.getCd_aluno());

                            startActivity(frequenciaIntent);
                        }
                        else{

                            BuscarFrequenciaAsyncTask buscarFrequenciaAsyncTask = new BuscarFrequenciaAsyncTask(this);

                            buscarFrequenciaAsyncTask.execute(aluno.getCd_aluno(), turma.getCd_turma());
                        }

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                }
                else if(frequenciasQueries.temFrequenciaBanco(aluno.getCd_aluno())) {

                    Intent intentFrequencias = new Intent(MenuActivity.this, FrequenciaActivity.class);

                    if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intentFrequencias.putExtra("cd_aluno", aluno.getCd_aluno());
                    }
                    startActivity(intentFrequencias);
                }

                else {

                    Toast.makeText(this,
                            "Verifique sua conexão e tente novamente", Toast.LENGTH_LONG).show();

                    esconderSombraCarregamento();
                    break;
                }

                break;
            //endregion

            //region CALENDARIO

            case R.id.menu_calendario:

                btnName = "Mód_Calendário";

                Intent intentCalendario = new Intent(MenuActivity.this, CalendarioActivity.class);

                if (isOnline(this)){

                    if (turma == null) {

                        Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else if(mPref.isPerfilMockResponsavel() || mPref.isPerfilMockAluno()){

                        Intent calendario = new Intent(this, CalendarioActivity.class);

                        calendario.putExtra("cd_aluno", 26111326);

                        startActivity(calendario);
                    }
                    else {
                        BuscarCalendarioAsyncTask buscarCalendarioAsyncTask = new BuscarCalendarioAsyncTask(this);

                        buscarCalendarioAsyncTask.execute(turma.getCd_turma(), aluno.getCd_aluno());
                    }
                }
                else if (calendarioQueries.temCalendarioBanco(aluno.getCd_aluno())) {

                    intentCalendario.putExtra("cd_aluno", aluno.getCd_aluno());

                    if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intentCalendario.putExtra("cd_aluno", aluno.getCd_aluno());
                    }

                    startActivity(intentCalendario);
                }
                else{

                    Toast.makeText(this,
                            "Verifique sua conexão e tente novamente", Toast.LENGTH_LONG).show();

                    esconderSombraCarregamento();
                    break;
                }

                break;
            //endregion

            //region ALIMENTACAO

            case R.id.menu_alimentacao:

                btnName = "Mód_Alimentação";

                Intent intentAlimentacao = new Intent(this, CardapioActivity.class);

                if (turma == null) {

                    Toast.makeText(this, "Ops! É necessário ter uma matrícula ativa para acessar", Toast.LENGTH_LONG).show();
                    break;
                }
                else {

                    if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intentAlimentacao.putExtra("cd_aluno", aluno.getCd_aluno());
                    }

                    intentAlimentacao.putExtra("cd_turma", turma.getCd_turma());

                    startActivity(intentAlimentacao);
                }

                break;
            //endregion

            //region CARTEIRINHA

            case R.id.menu_carteirinha:

                btnName = "Mód_Carteirinha";

                CarteirinhaQueries carteirinhaQueries = new CarteirinhaQueries(this);

                Intent intentCarteirinha = new Intent(MenuActivity.this, CarteirinhaActivity.class);

                if(carteirinhaQueries.temCarteirinha(aluno.getCd_aluno())) {

                    if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                        intentCarteirinha.putExtra("cd_aluno", aluno.getCd_aluno());
                    }
                    startActivity(intentCarteirinha);
                }
                else {

                    if(isOnline(this)) {

                        BuscarCarteirinhaAsyncTask buscarCarteirinhaAsyncTask = new BuscarCarteirinhaAsyncTask(this);

                        buscarCarteirinhaAsyncTask.execute(aluno.getCd_aluno());
                    }

                    else {

                        Toast.makeText(this, "Verifique sua conexão e tente novamente", Toast.LENGTH_LONG).show();
                        esconderSombraCarregamento();
                    }
                }
                break;
            //endregion

            //region MENSAGENS

            case R.id.menu_mensagens:

                btnName = "Mód_Mensagens";

                break;
            //endregion

            //region BOLETIM

            case R.id.menu_boletim:

                btnName = "Mód_Boletim";

                Intent boletim = new Intent(this, BoletimActivity.class);

                if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

                    boletim.putExtra("cd_aluno", aluno.getCd_aluno());
                }
                startActivity(boletim);

                //Toast.makeText(this, "O boletim está temporariamente indisponível", Toast.LENGTH_LONG).show();

                break;
            //endregion

            //region COMPARTILHAR

            case R.id.menu_compartilhar:

                btnName = "Mód_Compartilhar";

                Intent share = new Intent(Intent.ACTION_SEND);

                share.setType("text/plain");

                share.putExtra(Intent.EXTRA_SUBJECT, "Compartilhar Minha Escola SP");

                share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.descricao_share));

                startActivity(share);

                break;
            //endregion

            //region SAIR

            case R.id.menu_sair:

                if (IS_DEBUG) {

                    Intent dbmanager = new Intent(this, AndroidDatabaseManagerME.class);

                    startActivity(dbmanager);
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setMessage("Tem certeza que deseja encerrar sua sessão? Será necessário fazer o login novamente da próxima vez que entrar no aplicativo.");

                    builder.setPositiveButton("SAIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            usuarioQueries.deleteTable("aluno");
                            usuarioQueries.deleteTable("endereco");
                            usuarioQueries.deleteTable("contato_aluno");
                            usuarioQueries.deleteTable("rematricula");
                            usuarioQueries.deleteTable("responsavel");
                            usuarioQueries.deleteTable("contato_responsavel");
                            usuarioQueries.deleteTable("turma");
                            usuarioQueries.deleteTable("horario_aula");
                            usuarioQueries.deleteTable("escola");
                            usuarioQueries.deleteTable("contato_escola");
                            usuarioQueries.deleteTable("bimestre");
                            usuarioQueries.deleteTable("calendario");
                            usuarioQueries.deleteTable("nota");
                            usuarioQueries.deleteTable("frequencia");
                            usuarioQueries.deleteTable("carteirinha");
                            usuarioQueries.deleteTable("composicao_nota");

                            mPref.limparPreferencias();

                            Intent selecionarPerfil = new Intent(MenuActivity.this, SelecaoPerfilActivity.class);

                            startActivity(selecionarPerfil);

                            finish();
                        }
                    });

                    builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialogSair = builder.create();

                    dialogSair.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                    dialogSair.show();
                }

                break;
            //endregion
        }

        //region Tracker Módulo
        if(URL_SERVIDOR.equals(URLSERVIDOR_TRACKER)
                && btnName != null) {

            params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, btnName);

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);

            mFirebaseAnalytics.logEvent(btnName, params);
        }
        //endregion
    }

    @Override
    protected void onStart() {

        super.onStart();

        //TODO DeviceId = FirebaseInstanceId.getInstance().getToken() - Obsoleto;

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                DeviceId = instanceIdResult.getToken();

                if (!DeviceId.equals(mPref.getPreferenceString(ID_DEVICE))
                        && (mPref.getPreferenceString(ID_DEVICE).isEmpty())) {

                    mPref.savePreferenceString(ID_DEVICE, DeviceId);

                    enviarIdPush(DeviceId);
                }
            }
        });

        if(isFirstOnStart){

            onClickMudarAlunoTurma(new View(this));
            isFirstOnStart = false;
        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (mAlimentacaoReceiver != null) {

            unregisterReceiver(mAlimentacaoReceiver);

            progressDialog.dismiss();

            mAlimentacaoReceiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {

        return;
    }

    public void alterarTxtSobreMim(String nomeAluno) {

        String[] nomeSeparado = nomeAluno.split(" ");

        String primeiroNome = nomeSeparado[0].toLowerCase();

        txtSobreMim.setText(primeiroNome);
    }

    public void mostrarSombraCarregamento(){

        relativeSombra.setVisibility(View.VISIBLE);
    }

    public void esconderSombraCarregamento(){

        relativeSombra.setVisibility(View.GONE);
    }

}
