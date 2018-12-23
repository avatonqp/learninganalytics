package kosmoglou.antogkou.learninganalytics.Analytics_Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kosmoglou.antogkou.learninganalytics.R;

public class fragment_user_classes extends Fragment {
    // firestore
    CollectionReference enrolledClasses;
    // widgets
    public BarChart barChartClasses;
    // vars
    public int enrolled_total = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_user_classes, container, false);

        // Graphs
        barChartClasses = v.findViewById(R.id.barChartClasses);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs(){
        // get the key value from parent fragment
        String student_id = getArguments().getString("student_id");
        // the path to current classroom
        enrolledClasses = FirebaseFirestore.getInstance().collection("Users").document(student_id).collection("enrolled_classes");
        enrolledClasses.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            enrolled_total = task.getResult().size();
                        }

                        // init bar graph with queried values.
                        List<BarEntry> entriesUsers = new ArrayList<>();
                        entriesUsers.add(new BarEntry(0f, enrolled_total));
                        BarDataSet barSet = new BarDataSet(entriesUsers,"Enrolled Classrooms");
                        barSet.setColors(R.color.khaki); // Sets the color of the bar.
                        BarData barData = new BarData(barSet);
                        barChartClasses.setData(barData);
                        barChartClasses.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                        barChartClasses.getAxisRight().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getXAxis().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getXAxis().setEnabled(false); // Disables X Axis.
                        barChartClasses.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        barChartClasses.animateXY(2500,2500); // Animates the Bar Graph to X + Y Axis.
                        barChartClasses.invalidate(); // Refreshes the Bar Graph.
                    }
                });
    }
}
