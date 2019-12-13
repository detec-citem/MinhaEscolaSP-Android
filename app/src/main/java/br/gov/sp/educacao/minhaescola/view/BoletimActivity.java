package br.gov.sp.educacao.minhaescola.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import android.net.Uri;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.interfaces.OnFinishGerarBoletimAsynTaskListener;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.DadosBoletim;
import br.gov.sp.educacao.minhaescola.model.Turma;

import br.gov.sp.educacao.minhaescola.task.GerarBoletimAsyncTask;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import br.gov.sp.educacao.minhaescola.util.UrlServidor;
import br.gov.sp.educacao.minhaescola.webViewClient.BoletimWebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class BoletimActivity
        extends AppCompatActivity
        implements OnFinishGerarBoletimAsynTaskListener, Runnable {

    private int anoSelecionado;
    private boolean boletimCadastrado;

    private Aluno aluno;
    private Turma turmaEnvio;

    private UsuarioQueries usuarioQueries;
    private TurmaQueries turmaQueries;

    private GerarBoletimAsyncTask gerarBoletimAsyncTask;

    private ArrayList<Turma> turmasAluno;
    private ArrayList<Turma> turmasSelecAluno;

    private File documentFile;
    private MyPreferences mPref;

    public @BindView(R.id.btnBoletimConfirmar) Button buttonBoletimConfirmar;

    public @BindView(R.id.layoutBoletimAno) ConstraintLayout layoutBoletimAno;
    public @BindView(R.id.layoutBoletimTurma) ConstraintLayout layoutBoletimTurma;
    public @BindView(R.id.layoutBoletimBotao) ConstraintLayout layoutBoletimBotao;
    public @BindView(R.id.layoutLoading) ConstraintLayout layoutLoading;

    public @BindView(R.id.progressBar) ProgressBar progressBar;

    public @BindView(R.id.spnBoletimTurma) Spinner spinnerTurma;
    public @BindView(R.id.spnBoletimAno) Spinner spinnerAno;

    public @BindView(R.id.tv_loading) TextView tvLoading;

    public @BindView(R.id.webViewBoletim) WebView webView;

    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_boletim);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_boletim));
        }

        mPref = new MyPreferences(getApplicationContext());

        turmasSelecAluno = new ArrayList<>();

        usuarioQueries = new UsuarioQueries(getApplicationContext());
        turmaQueries = new TurmaQueries(getApplicationContext());

        Drawable progressDrawable = progressBar.getIndeterminateDrawable().mutate();

        progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        progressBar.setProgressDrawable(progressDrawable);

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO) {

            aluno = usuarioQueries.getAluno();
        }
        else {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }

        if(aluno != null){

            turmasAluno = turmaQueries.getTurmas(aluno.getCd_aluno());
        }
        else{

            refreshAluno();

            turmasAluno = turmaQueries.getTurmas(aluno.getCd_aluno());
        }


        ArrayList<Integer> listaAnosLetivos = new ArrayList<>();

        for(Turma turma : turmasAluno) {

            if(!listaAnosLetivos.contains(turma.getAno_letivo())) {

                listaAnosLetivos.add(turma.getAno_letivo());
            }
        }

        Collections.sort(listaAnosLetivos);
        Collections.reverse(listaAnosLetivos);

        int anterior = 0, atual = 0;

        for (int w = 0; w < listaAnosLetivos.size(); w++) {

            atual = listaAnosLetivos.get(w);

            if(atual == anterior) {

                listaAnosLetivos.remove(w);
            }
            anterior = listaAnosLetivos.get(w);
        }

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.layout_spinner_item_boletim, listaAnosLetivos);

        spinnerAno.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPause() {

        super.onPause();

        cancelAsyncTask();

        onBackPressed();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        cancelAsyncTask();
    }

    @Override
    public void onBackPressed() {

        if (layoutBoletimAno.getVisibility() == View.INVISIBLE) {

            layoutBoletimAno.setVisibility(View.VISIBLE);
            layoutBoletimBotao.setVisibility(View.VISIBLE);
            layoutBoletimTurma.setVisibility(View.VISIBLE);
            layoutLoading.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else {

            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return super.onSupportNavigateUp();
    }

    //Actions
    public void returnMenu(View v) {

        onBackPressed();
    }

    public void visualizacaoBoletim(View v) {

        DadosBoletim dadosBoletim = new DadosBoletim();

        try {

            dadosBoletim.setAno(String.valueOf(turmaEnvio.getAno_letivo()));
            dadosBoletim.setRa(aluno.getNumero_ra());
            dadosBoletim.setDig(aluno.getDigito_ra());
            dadosBoletim.setUf(aluno.getUf_ra());
            dadosBoletim.setDataNascimento(aluno.getData_nascimento());

            layoutBoletimAno.setVisibility(View.INVISIBLE);
            layoutBoletimBotao.setVisibility(View.INVISIBLE);
            layoutBoletimTurma.setVisibility(View.INVISIBLE);
            layoutLoading.setVisibility(View.VISIBLE);

            gerarBoletimAsyncTask = new GerarBoletimAsyncTask(this);

            gerarBoletimAsyncTask.execute(dadosBoletim);

        }
        catch (NullPointerException e) {

            Toast.makeText(this, "Selecione turma e ano", Toast.LENGTH_SHORT).show();
        }

    }

    @OnItemSelected(R.id.spnBoletimAno)
    public void onAnoSelected(Spinner spinner, int position) {

        int i;

        turmasSelecAluno.clear();

        anoSelecionado = (int) (spinner.getAdapter().getItem(position));

        int size = turmasAluno.size();

        for (i = 0; i < size; i++) {

            Turma turma = turmasAluno.get(i);

            if (anoSelecionado == turma.getAno_letivo()) {

                turmasSelecAluno.add(turma);
            }
        }

        List<String> nomesTurmas = new ArrayList<>();

        size = turmasSelecAluno.size();

        for (i = 0; i < size; i++) {

            nomesTurmas.add(turmasSelecAluno.get(i).getNome_turma());
        }

        ArrayAdapter<String> turmaArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item_boletim, nomesTurmas);

        spinnerTurma.setAdapter(turmaArrayAdapter);
    }

    @OnItemSelected(R.id.spnBoletimTurma)
    public void onTurmaSelected(Spinner spinner, int position) {

        buttonBoletimConfirmar.setEnabled(true);

        String nomeTurma = (String) spinner.getAdapter().getItem(position);

        int size = turmasSelecAluno.size();

        for (int i = 0; i < size; i++) {

            Turma turma = turmasSelecAluno.get(i);

            if (nomeTurma.equals(turma.getNome_turma()) || nomeTurma.equals(turmasSelecAluno.get(i).getNome_turma())) {

                turmaEnvio = turma;

                break;
            }
        }
    }

    //Javascript interface
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void boletimPdfData(byte[] pdfData) {
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!downloadsDirectory.exists()) {

            downloadsDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Download");

            downloadsDirectory.mkdir();
        }

        String appFolderPath = downloadsDirectory.getPath() + File.separator + "MinhaEscola";

        File appFolder = new File(appFolderPath);

        if (!appFolder.exists()) {

            appFolder.mkdir();
        }

        documentFile = new File(appFolderPath + File.separator + "Boletim_Escolar_Aluno_" + anoSelecionado + ".pdf");

        try {

            if (!documentFile.exists()) {

                documentFile.createNewFile();
            }

            documentFile.setWritable(true, true);

            FileOutputStream fileOutputStream = new FileOutputStream(documentFile);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(pdfData);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            fileOutputStream.close();
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        runOnUiThread(this);
    }

    //OnFinishGerarBoletimAsynTaskListener
    @Override
    public void finishGeneratingBoletim(String result) {

        try {

            if (!result.equalsIgnoreCase("Erro")) {
                if (result.contains("oBoletim")) {
                    String[] componentes = result.split("<script type=\"text/javascript\">");
                    String oBoletim = componentes[1].replace("</script>", "");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new BoletimWebViewClient(oBoletim));
                    webView.addJavascriptInterface(BoletimActivity.this, "BoletimActivity");
                    webView.loadUrl(UrlServidor.URL_GERAR_BOLETIM);
                }
                else {

                    if(!isFinishing()){

                        progressBar.setVisibility(View.INVISIBLE);

                        tvLoading.setText("");

                        onBackPressed();

                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle("Atenção")
                                .setMessage("Você não possui um boletim cadastrado para esse ano e turma")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                }).create();
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);
                        alertDialog.show();
                    }
                }
            }
            else {

                Toast.makeText(BoletimActivity.this, "Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        }
        catch (Exception e) {

            Toast.makeText(BoletimActivity.this, "Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    //Runnable
    @Override
    public void run() {

        if (!isFinishing()) {

            progressBar.setVisibility(View.INVISIBLE);

            tvLoading.setText("");

            onBackPressed();

            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            String fileProvider = getPackageName() + ".fileprovider";

            Uri uri = FileProvider.getUriForFile(this, fileProvider, documentFile);

            grantUriPermission(fileProvider, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            grantUriPermission(fileProvider, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.setDataAndType(uri, "application/pdf");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = new NotificationChannel("BoletimActivity", "BoletimActivity", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            PendingIntent p = PendingIntent.getActivity(getApplication(), 0, intent, 0);

            Builder notificationBuilder = new Builder(this, "BoletimActivity")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_minha_escola)
                    .setColor(ContextCompat.getColor(this, R.color.azul_boletim))
                    .setPriority(Notification.PRIORITY_DEFAULT).setContentTitle("Boletim")
                    .setContentText("Clique aqui para abrir o boletim")
                    .setContentIntent(p)
                    .addAction(R.drawable.ic_notas, "Abrir boletim", PendingIntent.getActivity(this, 0, intent, 0));

            notificationManager.notify(0, notificationBuilder.build());

            Toast.makeText(this, "Salvo em: " + documentFile, Toast.LENGTH_LONG).show();
        }
    }

    //Methods
    private void cancelAsyncTask() {

        if(gerarBoletimAsyncTask != null
                && gerarBoletimAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {

            gerarBoletimAsyncTask.cancel(true);
        }
    }

    private void refreshAluno(){

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO) {

            aluno = usuarioQueries.getAluno();
        }
        else {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
    }
}