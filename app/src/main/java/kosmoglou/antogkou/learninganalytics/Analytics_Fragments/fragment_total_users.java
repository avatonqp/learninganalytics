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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_total_users extends Fragment {
    // vars
    public int countUsers = 0;
    // widgets
    public BarChart barChartUsers;
    public PieChart pieChart;
    // Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_total_users, container, false);

        // Graphs
        barChartUsers = v.findViewById(R.id.barChartUsers);
        pieChart = v.findViewById(R.id.piegraph);

        // initGraphs
        initGraphs();

        return v;
    }

    public void initGraphs(){
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int teachers = 0; // Placeholder for sum of teachers.
                        int students = 0; // Placeholder for sum of students.
                        if (task.isSuccessful()) {
                            countUsers = task.getResult().size(); // Total count of Users.

                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getString("usertype").equals("Teacher")){
                                    teachers += 1;
                                }else if(document.getString("usertype").equals("Student")){
                                    students += 1;
                                }
                            }
                        } else {
                            //
                        }

                        // init bar graph with queried values.
                        List<BarEntry> entriesUsers = new ArrayList<>();
                        entriesUsers.add(new BarEntry(0f, countUsers));
                        BarDataSet barSet = new BarDataSet(entriesUsers,"Users");
                        barSet.setColors(R.color.khaki); // Sets the color of the bar.
                        BarData barData = new BarData(barSet);
                        barChartUsers.setData(barData);
                        barChartUsers.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                        barChartUsers.getAxisRight().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getXAxis().setDrawGridLines(false); // Hides background grid.
                        barChartUsers.getXAxis().setEnabled(false); // Disables X Axis.
                        barChartUsers.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        barChartUsers.animateXY(2500,2500); // Animates the Bar Graph to X + Y Axis.
                        barChartUsers.invalidate(); // Refreshes the Bar Graph.

                        // init pie graph with queried values.
                        List<PieEntry> pieEntries = new ArrayList<>();
                        pieEntries.add(new PieEntry(teachers, "Teachers"));
                        pieEntries.add(new PieEntry(students, "Students"));
                        PieDataSet pieSet = new PieDataSet(pieEntries, "");
                        pieSet.setColors(new int[] {Color.parseColor("#FF32DA64"), Color.parseColor("#FF32DAD4")} ); // Sets the colors for each pie part.
                        PieData pieData = new PieData(pieSet);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                        pieChart.animateXY(3000,3000); // Animates the Pie Graph to X + Y Axis.
                        pieChart.setDrawCenterText(true);
                        pieChart.setCenterText("Community");
                        pieChart.invalidate(); // Refreshes the Pie Graph.
                    }
                });
    }
}
