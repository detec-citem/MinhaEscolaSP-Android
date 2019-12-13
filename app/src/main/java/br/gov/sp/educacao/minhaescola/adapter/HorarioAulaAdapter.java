package br.gov.sp.educacao.minhaescola.adapter;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.banco.HorarioAulaQueries;

import br.gov.sp.educacao.minhaescola.model.HorarioAula;
import br.gov.sp.educacao.minhaescola.view.HorarioAulasActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorarioAulaAdapter
        extends BaseAdapter {

    private List<HorarioAula> listaAulas;

    private WeakReference<HorarioAulasActivity> horarioAulasActivityWeakReference;

    private HorarioAulaQueries horarioAulaQueries;

    public @BindView(R.id.horario_horaAula) TextView txtHorario;
    public @BindView(R.id.horario_nomeMateria) TextView txtMateria;
    public @BindView(R.id.horario_nomeEscola) TextView txtEscola;
    public @BindView(R.id.horario_nomeTurma) TextView txtTurma;
    public @BindView(R.id.horario_imgAula) ImageView imgAula;

    public HorarioAulaAdapter(List<HorarioAula> listaAulas, HorarioAulasActivity act) {

        this.listaAulas = listaAulas;

        horarioAulasActivityWeakReference = new WeakReference<HorarioAulasActivity>(act);

        horarioAulaQueries = new HorarioAulaQueries(act);

        ordenarListaPorHora();
    }

    @Override
    public int getCount() {

        return listaAulas.size();
    }

    @Override
    public Object getItem(int i) {

        return listaAulas.get(i);
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View mView = horarioAulasActivityWeakReference.get()
                .getLayoutInflater()
                .inflate(R.layout.item_horario_aula,
                        viewGroup,
                        false);

        final HorarioAula horarioAula = listaAulas.get(i);

        ButterKnife.bind(this, mView);

        String txtHorarioText = horarioAula.getData_hora_inicio() + " - " + horarioAula.getData_hora_fim();

        txtHorario.setText(txtHorarioText);
        txtEscola.setText(horarioAula.getNome_professor());
        txtTurma.setText(horarioAula.getNome_turma());

        String nomeFormatado = formatarNomeMateria(horarioAula.getCd_materia());

        if(!nomeFormatado.equals("")) {

            txtMateria.setText(nomeFormatado);
        }
        else {

            txtMateria.setText(horarioAula.getNome_materia());
        }

        imgAula.setImageResource(horarioAulaQueries.getIdImagemMateria(horarioAula.getCd_materia()));

        return mView;
    }

    private void ordenarListaPorHora() {

        Collections.sort(listaAulas, new Comparator<HorarioAula>() {

            private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            @Override
            public int compare(HorarioAula h1, HorarioAula h2) {

                int result = -1;

                try {

                    result = sdf.parse(h1.getData_hora_inicio()).compareTo(sdf.parse(h2.getData_hora_fim()));
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
                return result;
            }
        });
    }

    private String formatarNomeMateria(int codMateria){

        if(codMateria == 1000) {

            return "Matemática, Língua Portuguesa e Ciências";
        }
        else if(codMateria == 1100 || codMateria == 1118 || codMateria == 1131){

            return "Língua Portuguesa";
        }
        else if(codMateria == 1111 || codMateria == 1119 || codMateria == 1132){

            return "Língua Portuguesa e Literatura";
        }
        else if(codMateria == 1130){

            return "Sala de Leitura";
        }
        else if(codMateria == 1200 || codMateria == 1201 || codMateria == 1202){

            return "Língua Estrangeira: Espanhol";
        }
        else if(codMateria == 1300){

            return "Língua Estrangeira: Francês";
        }
        else if(codMateria == 1400 || codMateria == 1401 || codMateria == 1407|| codMateria == 1408){

            return "Língua Estrangeira: Inglês";
        }
        else if(codMateria == 1500){

            return "Língua Estrangeira: Alemão";
        }
        else if(codMateria == 1800){

            return "Educação Artística";
        }
        else if(codMateria == 1813 || codMateria == 1814 || codMateria == 1816){

            return "Artes";
        }
        else if(codMateria == 1900 || codMateria == 1903 || codMateria == 1908){

            return "Educação Física";
        }
        else if(codMateria == 1905){

            return "Atividades Currículares Desportivas";
        }
        else if(codMateria == 2000){

            return "Língua Estrangeira: Mandarim";
        }
        else if(codMateria == 2100 || codMateria == 2105 || codMateria == 2112){

            return "Geografia";
        }
        else if(codMateria == 2200 || codMateria == 2008){

            return "História";
        }
        else if(codMateria == 2300 || codMateria == 2306 || codMateria == 2309){

            return "Sociologia";
        }
        else if(codMateria == 2308){

            return "Sociologia do Trabalho";
        }
        else if(codMateria == 2400 || codMateria == 2413 || codMateria == 2426){

            return "Biologia";
        }
        else if(codMateria == 2500 || codMateria == 2504){

            return "Ciências Físicas e Biológicas";
        }
        else if(codMateria == 2600 || codMateria == 2605 || codMateria == 2607){

            return "Física";
        }
        else if(codMateria == 2700 || codMateria == 2707 || codMateria == 2713 || codMateria == 7235){

            return "Matemática";
        }
        else if(codMateria == 2800 || codMateria == 2812 || codMateria == 2831 || codMateria == 2832){

            return "Química";
        }
        else if(codMateria == 3100 || codMateria == 3105 || codMateria == 3107 || codMateria == 3108){

            return "Filosofia";
        }
        else if(codMateria == 3200){

            return "Estudos Sociais";
        }
        else if(codMateria == 5100){

            return "Organização Social e Política do Brasil";
        }
        else if(codMateria == 6900){

            return "Língua Estrangeira: Japônees";
        }
        else if(codMateria == 7000){

            return "Língua Estrangeira: Italiano";
        }
        else if(codMateria == 7240){

            return "Ciências da Natureza";
        }
        else if(codMateria == 7245){

            return "Ciências da Natureza";
        }
        else if(codMateria >= 8001 && codMateria <= 8015){

            return "Atletismo";
        }
        else if(codMateria >= 8016 && codMateria <= 8025){

            return "Basquete";
        }
        else if((codMateria >= 8026 && codMateria <= 8035) || (codMateria == 8218)){

            return "Capoeira";
        }
        else if(codMateria >= 8036 && codMateria <= 8050){

            return "Damas";
        }
        else if(codMateria >= 8051 && codMateria <= 8060){

            return "Futsal";
        }
        else if(codMateria >= 8061 && codMateria <= 8075){

            return "Ginástica Artística";
        }
        else if(codMateria >= 8076 && codMateria <= 8093){

            return "Ginástica Geral";
        }
        else if(codMateria >= 8091 && codMateria <= 8105){

            return "Ginástica Rítmica";
        }
        else if(codMateria >= 8106 && codMateria <= 8115){

            return "Handebol";
        }
        else if((codMateria >= 8116 && codMateria <= 8125) || (codMateria >= 8222 && codMateria <= 8225)){

            return "Judo";
        }
        else if(codMateria >= 8126 && codMateria <= 8140){

            return "Tênis de Mesa";
        }
        else if(codMateria >= 8141 && codMateria <= 8150){

            return "Voleibol";
        }
        else if(codMateria >= 8151 && codMateria <= 8165){

            return "Xadrez";
        }
        else if(codMateria >= 8166 && codMateria <= 8177){

            return "Badmington";
        }
        else if(codMateria >= 8178 && codMateria <= 8189){

            return "Natação";
        }
        else if(codMateria >= 8190 && codMateria <= 8197){

            return "Rugby";
        }
        else if(codMateria >= 8198 && codMateria <= 8205){

            return "Volei de Praia";
        }
        else if(codMateria >= 8206 && codMateria <= 8217){

            return "Karatê";
        }
        else{

            return "";
        }
    }
}
