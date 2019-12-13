package br.gov.sp.educacao.minhaescola.task;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import br.gov.sp.educacao.minhaescola.view.MapaActivity;

public class GoogleMapsAsynsTask
        extends AsyncTask<String, Void, List<Address>> {

    private Geocoder geocoder;

    private final String TAG = "GeocoderAsyncTask";

    private WeakReference<MapaActivity> mapaActivityWeakReference;

    public GoogleMapsAsynsTask(MapaActivity mapaActivity) {

        geocoder = new Geocoder(mapaActivity);

        mapaActivityWeakReference = new WeakReference<>(mapaActivity);
    }

    @Override
    protected List<Address> doInBackground(String... strings) {

        List<Address> addresses = null;

        try {

            addresses = geocoder.getFromLocationName(strings[0], 1);
        }
        catch (IOException e) {

            //Log.e(TAG, e.toString());
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {

        super.onPostExecute(addresses);

        MapaActivity mapaActivity = mapaActivityWeakReference.get();

        if (mapaActivity != null) {

            if (addresses == null || addresses.size() == 0) {

                mapaActivity.repostaEndereco(null);
            }
            else if (addresses.size() == 1) {

                mapaActivity.repostaEndereco(addresses.get(0));
            }
        }
    }
}
