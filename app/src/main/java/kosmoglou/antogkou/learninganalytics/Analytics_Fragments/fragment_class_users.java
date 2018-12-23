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

public class fragment_class_users extends Fragment {
    // firestore
    CollectionReference analytics;
    // widgets
    public BarChart barChartUsers;
    // vars
    public int total_enrolled = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_class_total_users, container, false);

        // Graphs
        barChartUsers = v.findViewById(R.id.barChartUsers);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs() {
        // get the key value from parent fragment
        String classroom_id = getArguments().getString("classroom_id");
        // the path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("enrolled_students");
        analytics
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            total_enrolled = task.getResult().size();
                        }

                        // init bar graph with queried values.
                        List<BarEntry> entriesUsers = new ArrayList<>();
                        entriesUsers.add(new BarEntry(0f, total_enrolled));
                        BarDataSet barSet = new BarDataSet(entriesUsers, "Students Enrolled");
                        barSet.setColors(R.color.khaki); // Sets the color of the bar.
                        BarData barData = new BarData(barSet);
                        barChartUsers.setData(barData);
                        barChartUsers.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                        barChartUsers.getAxisRight().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getXAxis().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getXAxis().setEnabled(false); // Disables X Axis.
                        barChartUsers.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        barChartUsers.animateXY(2500, 2500); // Animates the Bar Graph to X + Y Axis.
                        barChartUsers.invalidate(); // Refreshes the Bar Graph.
                    }
                });
    }
}
