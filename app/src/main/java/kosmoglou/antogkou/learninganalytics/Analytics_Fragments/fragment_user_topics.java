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

public class fragment_user_topics extends Fragment {
  // firestore
  CollectionReference analytics;
  // widgets
  public BarChart barChartTopics;
  // vars
  public int topics_opened = 0;

  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_analytics_user_topics, container, false);

    // Graphs
    barChartTopics = v.findViewById(R.id.barChartTopics);

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
            .whereEqualTo("typeOf", "topic_opened")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  topics_opened = task.getResult().size();
                }

                // init bar graph with queried values.
                List<BarEntry> entriesUsers = new ArrayList<>();
                entriesUsers.add(new BarEntry(0f, topics_opened));
                BarDataSet barSet = new BarDataSet(entriesUsers, "Topics Opened");
                barSet.setColors(R.color.khaki); // Sets the color of the bar.
                BarData barData = new BarData(barSet);
                barChartTopics.setData(barData);
                barChartTopics.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                barChartTopics.getAxisRight().setDrawGridLines(false); // Hides background grid.
                barChartTopics.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                barChartTopics.getXAxis().setDrawGridLines(false); // Hides background grid.
                barChartTopics.getXAxis().setEnabled(false); // Disables X Axis.
                barChartTopics.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                barChartTopics.animateXY(2500, 2500); // Animates the Bar Graph to X + Y Axis.
                barChartTopics.invalidate(); // Refreshes the Bar Graph.
              }
            });
  }
}
