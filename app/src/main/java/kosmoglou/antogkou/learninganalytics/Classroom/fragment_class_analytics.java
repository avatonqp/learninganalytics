package kosmoglou.antogkou.learninganalytics.Classroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_total_quizes;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_total_topics;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_users;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_outcome;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_taken;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_class_analytics extends Fragment {
    // widgets
    public TextView buttonReport;
    public Spinner spinnerTypeAnalytics;
    public FragmentManager childFragMan;
    public FragmentTransaction childFragTrans;
    // vars
    public String[] types = new String[] {"Total users enrolled", "Total quizes", "Total topics"};
    public Bundle args;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_class_analytics, container, false);

        // gets the classroom_id from previous activity
        String classroom_id = getArguments().getString("classroom_id");

        buttonReport = rootView.findViewById(R.id.buttonReport);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO opens new activity and makes a complete report for specific classroom
            }
        });
        spinnerTypeAnalytics = rootView.findViewById(R.id.spinnerTypeAnalytics);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeAnalytics.setAdapter(adapterType);
        spinnerTypeAnalytics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragA = new fragment_class_users();
                        args = new Bundle();
                        args.putString("classroom_id", classroom_id);
                        fragA.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragA);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 1:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragB = new fragment_class_total_quizes();
                        args = new Bundle();
                        args.putString("classroom_id", classroom_id);
                        fragB.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragB);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 2:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragC = new fragment_class_total_topics();
                        args = new Bundle();
                        args.putString("classroom_id", classroom_id);
                        fragC.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragC);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}