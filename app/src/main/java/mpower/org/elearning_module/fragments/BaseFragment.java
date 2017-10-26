package mpower.org.elearning_module.fragments;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import mpower.org.elearning_module.BaseActivity;
import mpower.org.elearning_module.interfaces.AudioPlayerListener;
import mpower.org.elearning_module.interfaces.LastPageListener;

/**
 * Created by sabbir on 10/22/17.
 */

public abstract class BaseFragment extends Fragment implements LastPageListener {
    AudioPlayerListener audioPlayerListener;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public AudioPlayerListener getAudioPlayerListener() {
        return audioPlayerListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        mInflater=LayoutInflater.from(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AudioPlayerListener){
            audioPlayerListener= (AudioPlayerListener) context;
        }else {
            showToast("No player attached");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = mInflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewReady(view,savedInstanceState);
    }

    protected abstract int getFragmentLayout();


    protected abstract void onViewReady(View view, @Nullable Bundle savedInstanceState);



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    protected ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected Activity getBaseActivity() {
        return getActivity();
    }


}
