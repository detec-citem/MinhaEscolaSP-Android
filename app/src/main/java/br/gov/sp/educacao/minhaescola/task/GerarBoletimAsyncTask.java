package br.gov.sp.educacao.minhaescola.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.interfaces.OnFinishGerarBoletimAsynTaskListener;
import br.gov.sp.educacao.minhaescola.model.DadosBoletim;
import br.gov.sp.educacao.minhaescola.requests.BoletimRequest;

public class GerarBoletimAsyncTask
        extends AsyncTask<DadosBoletim, Void, String> {

    private WeakReference<OnFinishGerarBoletimAsynTaskListener> weakReferenceListener;

    private BoletimRequest boletimRequest;

    public GerarBoletimAsyncTask(OnFinishGerarBoletimAsynTaskListener listener) {

        super();

        weakReferenceListener = new WeakReference<>(listener);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        boletimRequest = new BoletimRequest();
    }

    @Override
    protected String doInBackground(DadosBoletim... dadosBoletims) {

        String url = boletimRequest.executeBoletimRequest(dadosBoletims[0]);

        if(url == null){

            url = "Erro";
        }

        return url;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        OnFinishGerarBoletimAsynTaskListener listener = weakReferenceListener.get();

        if (listener != null) {

            listener.finishGeneratingBoletim(s);
        }
    }
}
