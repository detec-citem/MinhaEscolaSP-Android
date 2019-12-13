package br.gov.sp.educacao.minhaescola.util.avaliar;

import android.app.Dialog;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.Button;
import android.widget.RatingBar;

import br.gov.sp.educacao.minhaescola.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public class RateDialogFrag
        extends DialogFragment
        implements RatingBar.OnRatingBarChangeListener {
    //, View.OnClickListener {

    public static final String KEY = "fragment_rate";

    @Nullable public @BindView(R.id.rb_stars) RatingBar rbStars;
    @Nullable public @BindView(R.id.bt_later) Button btLater;
    @Nullable public @BindView(R.id.bt_never) Button btNever;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.fragment_rate_dialog, container);

        ButterKnife.bind(this, view1);

        //RatingBar rbStars = (RatingBar) view1.findViewById(R.id.rb_stars);

        rbStars.setOnRatingBarChangeListener(this);

        //View bt = view.findViewById(R.id.bt_never);
        //bt.setOnClickListener( this );

        //bt = view.findViewById(R.id.bt_later);
        //bt.setOnClickListener( this );

        return view1;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        if(rating >= 4) {

            RateDialogManager.showRateDialogPlayStore( getActivity() );

            RateSPManager.neverAskAgain( getActivity() );

            dismiss();
        }
        else if(rating > 0) {

            RateDialogManager.showRateDialogFeedback( getActivity(), rating );

            RateSPManager.updateTime( getActivity() );

            RateSPManager.updateLaunchTimes( getActivity() );

            dismiss();
        }
    }

    @Optional
    @OnClick({R.id.bt_later, R.id.bt_never})
    public void btLaterOnClick(View view) {

        switch (view.getId()) {

            case R.id.bt_later:

                RateSPManager.updateTime(getActivity());

                RateSPManager.updateLaunchTimes(getActivity());

                break;

            case R.id.bt_never:

                RateSPManager.neverAskAgain( getActivity() );

                break;
        }
        dismiss();
    }

    /*@Override
    public void onClick(View view) {

        if( view.getId() == R.id.bt_later ){

            RateSPManager.updateTime( getActivity() );
            RateSPManager.updateLaunchTimes( getActivity() );
        }
        else{

            RateSPManager.neverAskAgain( getActivity() );
        }
        dismiss();
    }*/
}
