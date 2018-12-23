package kosmoglou.antogkou.learninganalytics.Analytics_Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_user_quiz_outcome extends Fragment {
    // firestore
    CollectionReference analytics;
    // widgets
    public PieChart piechart;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_user_quiz_outcome, container, false);

        // Graphs
        piechart = v.findViewById(R.id.piegraph);

        // initGraphs
        initGraphs();

        return v;
    }
    public void initGraphs(){
        // get the key value from parent fragment
        String student_id = getArguments().getString("student_id");
        // the path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Users").document(student_id).collection("Analytics");

        analytics
                .whereEqualTo("typeOf", "quiz_grade")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int passed = 0; // Placeholder for sum of teachers.
                        int failed = 0; // Placeholder for sum of students.
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Analytic analytic = document.toObject(Analytic.class);
                                Float value = Float.parseFloat(analytic.getValue1());
                                if(value >= 5){
                                    passed += 1;
                                }else {
                                    failed += 1;
                                }
                            }
                        } else {
                            //
                        }
                        // init pie graph with queried values.
                        List<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(passed, "Passed"));
                        pieEntries.add(new PieEntry(failed, "Failed"));
                        PieDataSet pieSet = new PieDataSet(pieEntries, "");
                        pieSet.setColors(new int[] {getResources().getColor(R.color.theGreen), getResources().getColor(R.color.theRed)} ); // Sets the colors for each pie part.
                        PieData pieData = new PieData(pieSet);
                        piechart.setData(pieData);
                        piechart.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        piechart.animateXY(3000,3000); // Animates the Pie Graph to X + Y Axis.
                        piechart.setDrawCenterText(true);
                        piechart.setCenterText("Quizes");
                        piechart.invalidate(); // Refreshes the Pie Graph.
                    }
                });
    }
}
