package kosmoglou.antogkou.learninganalytics.Analytics_Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_user_quiz_taken extends Fragment{
    // firestore
    public CollectionReference analytics;
    // views
    public BarChart barChartQTaken;
    public Spinner spinnerYear, spinnerMonth;
    // vars
    public Integer quiz_taken = 0;
    public Integer selectedYear, selectedMonth;
    public ArrayList<Integer> year = new ArrayList<>();
    public ArrayList<Integer> month = new ArrayList<>();
    // onCreateView is the first method from this Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_user_quiz_taken, container, false);

        // init views
        barChartQTaken = v.findViewById(R.id.barChartQTaken);
        spinnerYear = v.findViewById(R.id.spinnerYear);
        spinnerMonth = v.findViewById(R.id.spinnerMonth);
        // init spinners
        initSpinners();

        return v;
    }
    // inits the spinners
    public void initSpinners(){
        Calendar calendar = Calendar.getInstance();
        Integer m = calendar.get(Calendar.MONTH)+1; // gets the current month as Integer, e.g.: 10 = Octomber
        Integer y = calendar.get(Calendar.YEAR); // gets current year as Integer
        for(int i=1; i <= m; i++){
            month.add(i); // populates the ArrayList with the months, from 1 till now
        }
        for(int x = 2018; x <= y; x++){
            year.add(x); // populates the ArrayList with the years, from 2018 till now
        }
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, year); // creates and inits the ArrayAdapter
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // sets the dropdown layout file for the spinner
        spinnerYear.setAdapter(adapterYear); // attaches the adapter to the spinner and populates it
        // listener for spinnerYear. When an item is selected inits the graphs
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedYear = Integer.parseInt(spinnerYear.getSelectedItem().toString());
                //Toast.makeText(getActivity(),selectedYear+"",Toast.LENGTH_SHORT).show();
                // initGraphs
                initGraphs();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, month); // creates and inits the ArrayAdapter
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // sets the dropdown layout file for the spinner
        spinnerMonth.setAdapter(adapterMonth); // attaches the adapter to the spinner and populates it
        // listener for spinnerMonth. When an item is selected inits the graphs
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = Integer.parseInt(spinnerMonth.getSelectedItem().toString());
                //Toast.makeText(getActivity(),selectedMonth+"",Toast.LENGTH_SHORT).show();
                // initGraphs
                initGraphs();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    // inits the bar graph
    public void initGraphs() {
        quiz_taken = 0;
        // get the key value from parent fragment
        String student_id = getArguments().getString("student_id");
        // the firestore path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Users").document(student_id).collection("Analytics");
        // firestore is in beta so we can't run our prefered query exactly the way we want it from back-end side, so here what we do is to
        // run the query from the client side
        analytics
                .whereEqualTo("typeOf", "quiz_taken")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Analytic analytic = document.toObject(Analytic.class);
                                    if (analytic.getMonth() == selectedMonth && analytic.getYear() == selectedYear) {
                                        quiz_taken += 1;
                                    }else{
                                        //
                                    }
                                }
                            // init bar graph with queried values.
                            List<BarEntry> entries = new ArrayList<>();
                            entries.add(new BarEntry(0f, quiz_taken));
                            BarDataSet barSet = new BarDataSet(entries, "Quizes Taken");
                            barSet.setColors(R.color.khaki); // Sets the color of the bar.
                            BarData barData = new BarData(barSet);
                            barChartQTaken.setData(barData);
                            barChartQTaken.setNoDataText("NOT ENOUGH DATA TO ANALYZE"); // Sets the message in case there is no data
                            barChartQTaken.setTouchEnabled(true); // Allows to enable/disable all possible touch-interactions with the chart.
                            barChartQTaken.getAxisRight().setDrawGridLines(false); // Hides background grid.
                            barChartQTaken.getAxisLeft().setDrawGridLines(false); // Hides background grid.
                            barChartQTaken.getXAxis().setDrawGridLines(false); // Hides background grid.
                            barChartQTaken.getXAxis().setEnabled(false); // Disables X Axis.
                            barChartQTaken.getDescription().setEnabled(false); // Turns off the description label on the bottom right corner.
                            barChartQTaken.animateXY(2500, 2500); // Animates the Bar Graph to X + Y Axis.
                            barChartQTaken.invalidate(); // Refreshes the Bar Graph.
                        }
                    }
                });
    }
}