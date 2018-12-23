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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_total_classes extends Fragment {
    //vars
    public int countClasses = 0;
    //widgets
    public BarChart barChartClasses;
    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_total_classes, container, false);

        // Graphs
        barChartClasses = v.findViewById(R.id.barChartClasses);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs(){
        db.collection("Classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            countClasses = task.getResult().size(); //Total count of Classes.

                            List<BarEntry> entriesClasses = new ArrayList<>();
                            entriesClasses.add(new BarEntry(0f, countClasses));

                            BarDataSet barSetClasses = new BarDataSet(entriesClasses,"Classes");
                            BarData barData = new BarData(barSetClasses);
                            barChartClasses.setData(barData);

                            barChartClasses.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                            barChartClasses.getAxisRight().setDrawGridLines(false); // Hides background grid.
                            barChartClasses.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                            barChartClasses.getXAxis().setDrawGridLines(false); // Hides background grid.
                            barChartClasses.getXAxis().setEnabled(false); // Disables X Axis.
                            barChartClasses.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                            barChartClasses.animateXY(3000,3000); // Animates the Bar Graph to X + Y Axis.
                            barChartClasses.invalidate(); // Refreshes the Bar Graph.

                        } else {
                            //
                        }
                    }
                });
    }
}
