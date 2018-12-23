package kosmoglou.antogkou.learninganalytics.Student_Tab;

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

import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_classes;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_posts;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_outcome;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_taken;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_topics;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_posts;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_quiz_taken;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_topics;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_student_tab_analytics extends Fragment {
    // widgets
    public TextView buttonReport;
    public Spinner spinnerTypeAnalytics;
    public FragmentManager childFragMan;
    public FragmentTransaction childFragTrans;
    // vars
    public String[] types = new String[] {"Quizes taken by Student", "Quizes taken by Student (Yearly)", "Quizes scores by Student", "Total discussions started by Student", "Discussions started by Student (Yearly)", "Comments by Student", "Comments by Student (Yearly)", "Classes enrolled by Student"};
    public Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_student_tab_analytics, container, false);

        // gets the student_id from the StudentTabActivity
        String student_id = getArguments().getString("student_id");


        buttonReport = rootView.findViewById(R.id.buttonReport);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO opens new activity and makes a complete report for specific student
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
                        Fragment fragA = new fragment_user_quiz_taken();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragA.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragA);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 1:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragB = new fragment_user_yearly_quiz_taken();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragB.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragB);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 2:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragC = new fragment_user_quiz_outcome();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragC.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragC);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 3:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragD = new fragment_user_topics();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragD.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragD);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 4:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragE = new fragment_user_yearly_topics();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragE.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragE);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 5:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragF = new fragment_user_posts();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragF.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragF);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 6:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragG = new fragment_user_yearly_posts();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragG.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragG);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                    case 7:
                        childFragMan = getChildFragmentManager();
                        childFragTrans = childFragMan.beginTransaction();
                        Fragment fragH = new fragment_user_classes();
                        args = new Bundle();
                        args.putString("student_id", student_id);
                        fragH.setArguments(args);
                        childFragTrans.replace(R.id.fragment_container, fragH);
                        childFragTrans.addToBackStack("B");
                        childFragTrans.commit();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return  rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
