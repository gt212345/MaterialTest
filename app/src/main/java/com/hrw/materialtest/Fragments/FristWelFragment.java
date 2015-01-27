package com.hrw.materialtest.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrw.materialtest.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FristWelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FristWelFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Handler handler;
    private HandlerThread handlerThread;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FristWelFragment newInstance(String param1, String param2) {
        FristWelFragment fragment = new FristWelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FristWelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firstwel, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView welcome = (TextView)getView().findViewById(R.id.welcome);
        welcome.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.welcome));
        RelativeLayout relativeLayout = (RelativeLayout)getView().findViewById(R.id.welcome_relat);
        relativeLayout.setBackgroundResource(R.drawable.beacon2);
        relativeLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.fade_in));
        final TextView slide = (TextView)getView().findViewById(R.id.slide);
        handlerThread = new HandlerThread("");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        slide.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                                R.anim.blink));
                        slide.setVisibility(View.VISIBLE);
                    }
                });
            }
        },2800);
    }
}
