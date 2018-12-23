package kosmoglou.antogkou.learninganalytics.Analytics_Fragments;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class fragment_user_yearly_posts extends Fragment {
    // firestore
    CollectionReference analytics;
    // views
    public LineChart lineChart;
    public Spinner spinnerYear;
    // vars
    public ArrayList<Integer> year = new ArrayList<>();
    public Integer selectedYear;
    public int mOne = 0;
    public int mTwo = 0;
    public int mThree = 0;
    public int mFour = 0;
    public int mFive = 0;
    public int mSix = 0;
    public int mSeven = 0;
    public int mEight = 0;
    public int mNine = 0;
    public int mTen = 0;
    public int mEleven = 0;
    public int mTwelve = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analytics_user_yearly_posts, container, false);

        // Graphs
        lineChart = v.findViewById(R.id.lineChartPosts);
        spinnerYear = v.findViewById(R.id.spinnerYear);

        // initGraphs
        initSpinners();

        return v;
    }
    // inits spinner
    public void initSpinners(){
        Calendar calendar = Calendar.getInstance();
        Integer y = calendar.get(Calendar.YEAR); // gets current year as Integer
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
                Toast.makeText(getActivity(),selectedYear+"",Toast.LENGTH_SHORT).show();
                // initGraphs
                initGraphs();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //empty
            }
        });
    }
    // inits graph
    public void initGraphs(){
        monthsZero();
        // get the key value from parent fragment
        String student_id = getArguments().getString("student_id");
        // the path to current classroom
        analytics = FirebaseFirestore.getInstance().collection("Users").document(student_id).collection("Analytics");
        // firestore is in beta so we can't run our prefered query exactly the way we want it from back-end side, so here what we do is to
        // run the query from the client side
        analytics
                .whereEqualTo("typeOf", "post_written")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //quiz_taken = task.getResult().size(); // all time
                            for (DocumentSnapshot document : task.getResult()) {
                                Analytic analytic = document.toObject(Analytic.class);
                                if (analytic.getYear() == selectedYear) {
                                    if(analytic.getMonth() == 1){
                                        mOne += 1; // January
                                    }else if(analytic.getMonth() == 2){
                                        mTwo += 1; // February
                                    }else if(analytic.getMonth() == 3){
                                        mThree += 1; // March
                                    }else if(analytic.getMonth() == 4){
                                        mFour += 1; // April
                                    }else if(analytic.getMonth() == 5){
                                        mFive += 1; // May
                                    }else if(analytic.getMonth() == 6){
                                        mSix += 1; // June
                                    }else if(analytic.getMonth() == 7){
                                        mSeven += 1; // July
                                    }else if(analytic.getMonth() == 8){
                                        mEight += 1; // August
                                    }else if(analytic.getMonth() == 9){
                                        mNine += 1; // September
                                    }else if(analytic.getMonth() == 10){
                                        mTen += 1; // October
                                    }else if(analytic.getMonth() == 11){
                                        mEleven += 1; // November
                                    }else if(analytic.getMonth() == 12){
                                        mTwelve += 1; // December
                                    }

                                }
                            }

                            List<Entry> lineEntries = new ArrayList<>(); // the ArrayList of entries that will populate the Line Chart
                            lineEntries.add(new Entry(1, mOne)); // entry 1 = January
                            lineEntries.add(new Entry(2, mTwo)); // entry 2 = February
                            lineEntries.add(new Entry(3, mThree)); // entry 3 = March
                            lineEntries.add(new Entry(4, mFour)); // entry 4 = April
                            lineEntries.add(new Entry(5, mFive)); // entry 5 = May
                            lineEntries.add(new Entry(6, mSix)); // entry 6 = June
                            lineEntries.add(new Entry(7, mSeven)); // entry 7 = July
                            lineEntries.add(new Entry(8, mEight)); // entry 8 = August
                            lineEntries.add(new Entry(9, mNine)); // entry 9 = September
                            lineEntries.add(new Entry(10, mTen)); // entry 10 = October
                            lineEntries.add(new Entry(11, mEleven)); // entry 11 = November
                            lineEntries.add(new Entry(12, mTwelve)); // entry 12 = December
                            // sets the ArrayList as the LineDataSet and labels it as "Quizes Taken" (Per Month)
                            LineDataSet lineDataSet = new LineDataSet(lineEntries, "Posts Written");
                            // some self-explain settings for the way we want the Line Chart to appear
                            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                            lineDataSet.setHighlightEnabled(true);
                            lineDataSet.setLineWidth(2);
                            lineDataSet.setColor(getResources().getColor(R.color.theLight));
                            lineDataSet.setCircleColor(getResources().getColor(R.color.theBrown));
                            lineDataSet.setCircleRadius(6);
                            lineDataSet.setCircleHoleRadius(3);
                            lineDataSet.setDrawHighlightIndicators(true);
                            lineDataSet.setHighLightColor(Color.RED);
                            lineDataSet.setValueTextSize(12);
                            lineDataSet.setValueTextColor(getResources().getColor(R.color.theDark));
                            //
                            LineData lineData = new LineData(lineDataSet);
                            // some more self-explain settings for the way we want the Line Chart to appear
                            lineChart.getAxisLeft().setDrawGridLines(false);
                            lineChart.getXAxis().setDrawGridLines(false);
                            lineChart.getDescription().setText("Posts written in " + selectedYear);
                            lineChart.getDescription().setTextSize(12);
                            lineChart.setDrawMarkers(true);
                            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                            lineChart.animateY(1000);
                            lineChart.getXAxis().setGranularityEnabled(true);
                            lineChart.getXAxis().setGranularity(1.0f);
                            lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
                            //
                            lineChart.setData(lineData);
                            lineChart.invalidate(); // Refreshes the Line Chart
                        }
                    }
                });
    }
    // sets months integers to zero
    public void monthsZero(){
        mOne = 0;
        mTwo = 0;
        mThree = 0;
        mFour = 0;
        mFive = 0;
        mSix = 0;
        mSeven = 0;
        mEight = 0;
        mNine = 0;
        mTen = 0;
        mEleven = 0;
        mTwelve = 0;
    }
}
