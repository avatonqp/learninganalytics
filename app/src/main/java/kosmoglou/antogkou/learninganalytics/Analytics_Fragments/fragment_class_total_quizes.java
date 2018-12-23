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

public class fragment_class_total_quizes extends Fragment {
    // firestore
    public CollectionReference analytics;
    // widgets
    public BarChart barChartClasses;
    // vars
    public int total_quizes = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_class_total_quizes, container, false);

        // Graphs
        barChartClasses = v.findViewById(R.id.barChartClasses);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs() {
        // get the key value from parent fragment
        String classroom_id = getArguments().getString("classroom_id");
        // the path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes");
        analytics
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            total_quizes = task.getResult().size();
                        }

                        // init bar graph with queried values.
                        List<BarEntry> entriesClasses = new ArrayList<>();
                        entriesClasses.add(new BarEntry(0f, total_quizes));
                        BarDataSet barSet = new BarDataSet(entriesClasses, "Total quizes");
                        barSet.setColors(R.color.khaki); // Sets the color of the bar.
                        BarData barData = new BarData(barSet);
                        barChartClasses.setData(barData);
                        barChartClasses.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                        barChartClasses.getAxisRight().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getXAxis().setDrawGridLines(false); // Hides background grid.
                        barChartClasses.getXAxis().setEnabled(false); // Disables X Axis.
                        barChartClasses.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        barChartClasses.animateXY(2500, 2500); // Animates the Bar Graph to X + Y Axis.
                        barChartClasses.invalidate(); // Refreshes the Bar Graph.
                    }
                });
    }
}
