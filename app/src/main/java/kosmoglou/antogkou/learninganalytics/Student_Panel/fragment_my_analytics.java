package kosmoglou.antogkou.learninganalytics.Student_Panel;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_total_quizes;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_total_topics;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_class_users;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_classes;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_posts;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_outcome;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_quiz_taken;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_topics;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_posts;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_quiz_taken;
import kosmoglou.antogkou.learninganalytics.Analytics_Fragments.fragment_user_yearly_topics;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_my_analytics extends Fragment {
    // widgets
    public Spinner spinnerTypeAnalytics;
    public FragmentManager childFragMan;
    public FragmentTransaction childFragTrans;
    // vars
    public String student_id;
    public String[] types = new String[] {"Total quizes taken", "Quizes taken (Yearly)", "Quizes scores", "Total discussions started", "Total discussions started (Yearly)", "Total comments written", "Total comments written (Yearly)", "Total classes enrolled"};
    public Bundle args;
    //firebase
    public FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_analytics, container, false);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        student_id = mAuth.getUid();

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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
