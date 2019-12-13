
package br.gov.sp.educacao.minhaescola.util;


import android.app.Activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

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
import br.gov.sp.educacao.minhaescola.dialog.DialogCalendario;
import br.gov.sp.educacao.minhaescola.model.DiasEventos;

public class CaldroidFragmentListener
        extends CaldroidListener {

    private final String FORMAT_DATE_DMYYYY = "d/M/yyyy";

    private final String FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy";

    private CaldroidFragment caldroidFragment;

    private int diasMes;
    private int mesSelecionado;

    private List<Date> listaDiasLetivosStr;
    private List<Integer> listaDiasDaSemana;
    private Map<String, List<DiasEventos>> mapaDiasEventos;
    private List<String> listaDiasFerias;
    private Activity activity;
    private Date dataAtual;
    private DialogCalendario dialogCalendario;

    public CaldroidFragmentListener(CaldroidFragment caldroidFragment,
                                    List<Integer> listaDiasDaSemana,
                                    List<Date> diasLetivos,
                                    Map<String, List<DiasEventos>> mapaDiasEventos,
                                    List<String> listaDiasFerias,
                                    Activity activity1) {

        this.caldroidFragment = caldroidFragment;
        this.listaDiasDaSemana = listaDiasDaSemana;
        this.listaDiasLetivosStr = diasLetivos;
        this.mapaDiasEventos = mapaDiasEventos;
        this.listaDiasFerias = listaDiasFerias;
        this.activity = activity1;
    }

    @Override
    public void onSelectDate(Date date,
                             View view) {

        int dia = DateUtils.getCurrentDay(date);
        int mes = DateUtils.getCurrentMonth(date);
        int ano = DateUtils.getCurrentYear(date);

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DMYYYY);

        String strDataLetivo = sdf.format(date);

        if(mapaDiasEventos.containsKey(strDataLetivo)) {

            Bundle bundle = new Bundle();

            bundle.putParcelableArrayList("listaEventos", (ArrayList<? extends Parcelable>) mapaDiasEventos.get(strDataLetivo));

            dialogCalendario = new DialogCalendario();

            dialogCalendario.setArguments(bundle);

            dialogCalendario.show(activity.getFragmentManager(), DialogCalendario.TAG);
        }
    }

    @Override
    public void onChangeMonth(int month,
                              int year) {

        int diasMesAnterior = DateUtils.totalDiasMes(month - 2, year);

        for(int i = 0; i < diasMesAnterior; i++) {

            Calendar calendarAnterior = Calendar.getInstance();

            calendarAnterior.set(year,
                                 month - 2,
                                 i + 1);

            Date dataLetivoNext = calendarAnterior.getTime();

            caldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

            caldroidFragment.clearTextColorForDate(dataLetivoNext);
        }

        int diasMesNext = DateUtils.totalDiasMes(month,
                                                 year);

        for(int i = 0; i < diasMesNext; i++) {

            Calendar calendarNext = Calendar.getInstance();

            calendarNext.set(year,
                             month,
                             i + 1);

            Date dataLetivoNext = calendarNext.getTime();

            caldroidFragment.clearBackgroundResourceForDate(dataLetivoNext);

            caldroidFragment.clearTextColorForDate(dataLetivoNext);
        }
        diasMes = DateUtils.totalDiasMes(month - 1,
                                         year);

        ArrayList<Integer> totalDiasMes = new ArrayList<>();

        for(int i = 0; i < diasMes; i++) {

            totalDiasMes.add(i + 1);
        }

        ArrayList<Integer> diasLetivosConfirmados = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        int anoAtual = calendar.get(Calendar.YEAR);

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DMYYYY);

        String strDataAtual = sdf.format(calendar.getTime());

        try {

            dataAtual = sdf.parse(strDataAtual);
        }
        catch(ParseException e) {

            e.printStackTrace();
        }

        for(Date diaLetivoString : listaDiasLetivosStr) {

            Calendar cal = Calendar.getInstance();

            cal.setTime(diaLetivoString);

            int diaLetivoMes = cal.get(Calendar.DAY_OF_MONTH);

            if(month - 1 == cal.get(Calendar.MONTH)) {

                calendar.set(year,
                             month - 1,
                             diaLetivoMes);

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                try {

                    String inicioAulas = listaDiasFerias.get(0);

                    String meioAnoFimAulas = listaDiasFerias.get(3);

                    String meioAnoInicioAulas = listaDiasFerias.get(4);

                    String fimAulas = listaDiasFerias.get(7);

                    for(int j = 0; j < listaDiasDaSemana.size(); j++) {

                        String strDataLetivo = sdf.format(calendar.getTime());

                        Date dataLetivo = sdf.parse(strDataLetivo);

                        if(listaDiasDaSemana.get(j) + 1 == dayOfWeek
                                && dataLetivo.after(sdf.parse(inicioAulas))
                                && dataLetivo.before(sdf.parse(meioAnoFimAulas))
                                || listaDiasDaSemana.get(j) + 1 == dayOfWeek
                                && dataLetivo.after(sdf.parse(meioAnoInicioAulas))
                                && dataLetivo.before(sdf.parse(fimAulas))
                                || listaDiasDaSemana.get(j) + 1 == dayOfWeek
                                && dataLetivo.equals(sdf.parse(inicioAulas))
                                || dataLetivo.equals(sdf.parse(meioAnoFimAulas))
                                || listaDiasDaSemana.get(j) + 1 == dayOfWeek
                                && dataLetivo.equals(sdf.parse(meioAnoInicioAulas))
                                || dataLetivo.equals(sdf.parse(fimAulas))) {

                            if(mapaDiasEventos.containsKey(strDataLetivo)) {

                                if(!mapaDiasEventos.get(strDataLetivo).get(0).isLetivo()) {

                                    caldroidFragment.setBackgroundResourceForDate(R.color.rose_dia_nao_letivo,
                                                                                  dataLetivo);

                                    caldroidFragment.setTextColorForDate(R.color.vermelho_texto_dia_nao_letivo,
                                                                         dataLetivo);
                                }
                                else if(isAvaliacao(mapaDiasEventos.get(strDataLetivo).get(0).getNomeEvento())) {

                                    caldroidFragment.setBackgroundResourceForDate(R.color.roxo_dia_avaliacao,
                                                                                  dataLetivo);

                                    caldroidFragment.setTextColorForDate(R.color.preto_texto_dia_avaliacao,
                                                                         dataLetivo);
                                }
                                else {

                                    caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo2,
                                                                                  dataLetivo);

                                    caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo2,
                                                                         dataLetivo);
                                }
                            }
                            else {

                                caldroidFragment.setBackgroundResourceForDate(R.color.verde_dia_letivo,
                                                                              dataLetivo);

                                caldroidFragment.setTextColorForDate(R.color.verde_texto_dia_letivo,
                                                                     dataLetivo);
                            }
                            if(dataLetivo.equals(dataAtual)) {

                                //caldroidFragment.setBackgroundResourceForDate(R.color.amarelo_dia_letivo,
                                // dataLetivo);

                                caldroidFragment.setTextColorForDate(R.color.colorBlack,
                                        dataLetivo);
                            }
                            diasLetivosConfirmados.add(diaLetivoMes);
                        }
                    }
                }
                catch (IndexOutOfBoundsException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i = 0; i < totalDiasMes.size(); i++) {

            for(int j = 0; j < diasLetivosConfirmados.size(); j++) {

                if(totalDiasMes.get(i) == diasLetivosConfirmados.get(j)) {

                    totalDiasMes.remove(diasLetivosConfirmados.get(j));
                }
            }
        }
        ArrayList<String> disableDateStrings = new ArrayList<>();

        for(int i = 0; i < totalDiasMes.size(); i++) {

            disableDateStrings.add(DateUtils.parseDateNormal(totalDiasMes.get(i),
                                                             month,
                                                             year));
        }
        caldroidFragment.setDisableDatesFromString(disableDateStrings);

        mesSelecionado = month;

        if(month == 1
                && year > anoAtual) {

            caldroidFragment.prevMonth();
        }
        if(month == 12
                && year < anoAtual) {

            caldroidFragment.nextMonth();
        }
    }

    public static boolean isAvaliacao(String nome) {

        if(nome.equals("Avaliação")
                || nome.equals("Atividade")
                || nome.equals("Trabalho")
                || nome.equals("Outros")) {

            return true;
        }
        else {
            return false;
        }
    }

    public void onDateSelected(Date date) {

    }
}
