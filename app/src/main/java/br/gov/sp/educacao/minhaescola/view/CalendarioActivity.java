package br.gov.sp.educacao.minhaescola.view;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.banco.CalendarioQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.DiasEventos;

import br.gov.sp.educacao.minhaescola.util.CaldroidFragmentListener;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

public class CalendarioActivity
        extends AppCompatActivity {

    private Bundle mSavedInstanceState;

    private CaldroidFragment mDialogCaldroidFragment;

    private CalendarioQueries calendarioQueries;

    private UsuarioQueries usuarioQueries;

    private MyPreferences mPref;

    private Aluno aluno;

    private final String CALDROID_DIALOG_FRAGMENT = "CALDROID_DIALOG_FRAGMENT";

    private final String DIALOG_CALDROID_SAVED_STATE = "DIALOG_CALDROID_SAVED_STATE";

    private final String FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendario);

        this.mSavedInstanceState = savedInstanceState;

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_calendario));
        }

        usuarioQueries = new UsuarioQueries(this);

        aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));

        mPref = new MyPreferences(this);

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        setupCalendar();
    }

    private void setupCalendar() {

        mDialogCaldroidFragment = new CaldroidFragment();

        showCalendar(mSavedInstanceState);

        mDialogCaldroidFragment.setCaldroidListener(getListenerCaldroid());
    }

    private void showCalendar(Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            mDialogCaldroidFragment.restoreDialogStatesFromKey(getSupportFragmentManager(),
                    savedInstanceState,
                    DIALOG_CALDROID_SAVED_STATE,
                    CALDROID_DIALOG_FRAGMENT);

            Bundle args = mDialogCaldroidFragment.getArguments();

            if(args == null) {

                args = new Bundle();

                args.putInt(CaldroidFragment.THEME_RESOURCE,
                        R.style.CalDroidTheme);

                mDialogCaldroidFragment.setArguments(args);
            }
        }
        else {

            Bundle bundle = new Bundle();

            bundle.putInt(CaldroidFragment.THEME_RESOURCE,
                    R.style.CalDroidTheme);

            mDialogCaldroidFragment.setArguments(bundle);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.calendario, mDialogCaldroidFragment);

        ft.commit();
    }

    public List<Date> getDiasMes() {

        List<Date> mDiasMes = new ArrayList<>();

        Date date1 = new Date();

        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(date1);

        cal1.set(Calendar.DAY_OF_MONTH, 1);

        cal1.set(Calendar.MONTH, 0);

        cal1.get(Calendar.MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DDMMYYYY);

        for(int i = 0; i <= 12; i++) {

            while(i == Integer.valueOf(cal1.get(Calendar.MONTH))) {

                try {

                    String strDataLetivo = sdf.format(cal1.getTime());

                    Date dataLetivo = sdf.parse(strDataLetivo);

                    mDiasMes.add(dataLetivo);
                }
                catch (ParseException | NullPointerException e) {

                    e.printStackTrace();
                }
                cal1.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return mDiasMes;
    }

    public CaldroidListener getListenerCaldroid() {

        List<Date> mDiasMes = getDiasMes();

        List<Integer> mDiasSemana = new ArrayList<>();

        calendarioQueries = new CalendarioQueries(this);

        List<String> mDiasFerias = calendarioQueries.getDiasFerias(aluno.getCd_aluno());

        if (mDiasFerias.isEmpty()){

            Toast.makeText(this, "Não possui calendário", Toast.LENGTH_LONG).show();
        }

        Map<String, List<DiasEventos>> mDiasEventos = calendarioQueries.getDiasEventos(aluno.getCd_aluno());

        for(int i = 1; i <= 5; i++) {

            mDiasSemana.add(i);
        }

        CaldroidFragmentListener listener = new CaldroidFragmentListener(mDialogCaldroidFragment,
                mDiasSemana,
                mDiasMes,
                mDiasEventos,
                mDiasFerias,
                this) {
        };
        return listener;
    }

    public void voltarMenu(View v) {

        onBackPressed();
    }
}
