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

public class fragment_user_posts extends Fragment {
    // firestore
    CollectionReference analytics;
    // widgets
    public BarChart barChartPosts;
    // vars
    public int posts_written = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_user_posts, container, false);

        // Graphs
        barChartPosts = v.findViewById(R.id.barChartPosts);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs() {
        // get the key value from parent fragment
        String student_id = getArguments().getString("student_id");
        // the path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Users").document(student_id).collection("Analytics");
        analytics
                .whereEqualTo("typeOf", "post_written")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            posts_written = task.getResult().size();
                        }

                        // init bar graph with queried values.
                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0f, posts_written));
                        BarDataSet barSet = new BarDataSet(entries, "Posts Written");
                        barSet.setColors(R.color.khaki); // Sets the color of the bar.
                        BarData barData = new BarData(barSet);
                        barChartPosts.setData(barData);
                        barChartPosts.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                        barChartPosts.getAxisRight().setDrawGridLines(false); // Hides background grid.
                        barChartPosts.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                        barChartPosts.getXAxis().setDrawGridLines(false); // Hides background grid.
                        barChartPosts.getXAxis().setEnabled(false); // Disables X Axis.
                        barChartPosts.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        barChartPosts.animateXY(2500, 2500); // Animates the Bar Graph to X + Y Axis.
                        barChartPosts.invalidate(); // Refreshes the Bar Graph.
                    }
                });
    }
}
