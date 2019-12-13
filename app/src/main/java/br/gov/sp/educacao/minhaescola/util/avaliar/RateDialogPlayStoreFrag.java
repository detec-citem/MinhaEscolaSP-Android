package br.gov.sp.educacao.minhaescola.util.avaliar;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.gov.sp.educacao.minhaescola.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RateDialogPlayStoreFrag
        extends RateDialogFrag {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_rate_dialog_play_store, container);

        ButterKnife.bind(this, view);

        /*View btNever = view.findViewById(R.id.bt_no);
        btNever.setOnClickListener( this );
        View btLater = view.findViewById(R.id.bt_yes);
        btLater.setOnClickListener( this );*/

        return view;
    }

    @OnClick({R.id.bt_yes, R.id.bt_no})
    public void btOnClick(View view) {

        if (view.getId() == R.id.bt_yes) {

            String packageName = getActivity().getPackageName();

            Intent it;

            try {

                it = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));

                startActivity(it);
            }
            catch (android.content.ActivityNotFoundException anfe) {

                it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));

                startActivity(it);
            }
        }
        dismiss();
    }

    /*@Override
    public void onClick(View view) {

        if( view.getId() == R.id.bt_yes ){

            String packageName = getActivity().getPackageName();
            Intent it;

            try {

                it = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                startActivity( it );
            }
            catch (android.content.ActivityNotFoundException anfe) {

                it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                startActivity( it );
            }
        }
        dismiss();
    }*/
}
