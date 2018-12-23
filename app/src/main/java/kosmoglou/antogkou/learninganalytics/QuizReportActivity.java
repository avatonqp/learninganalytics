package kosmoglou.antogkou.learninganalytics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import kosmoglou.antogkou.learninganalytics.Classes.Quiz;

public class QuizReportActivity extends AppCompatActivity {
    // views
    public Toolbar mToolbar;
    public ImageView iv;
    public TextView tvCorrect, tvWrong, tvGrade;
    // vars
    public Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_report);

        // get key values from previous activity
        Intent intent = getIntent();
        String quiz_id = intent.getExtras().getString("quiz_id");
        String classroom_id = intent.getExtras().getString("classroom_id");
        int correct = intent.getExtras().getInt("correct");
        int wrong = intent.getExtras().getInt("wrong");
        float grade = intent.getExtras().getFloat("grade");
        DocumentReference path = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes").document(quiz_id);

        // init views
        iv = findViewById(R.id.imageView);
        // checks the grade in order to init the imageview value
        if(grade >= 5){
            iv.setImageResource(R.drawable.pass);
        }else{
            iv.setImageResource(R.drawable.fail);
        }
        tvCorrect = findViewById(R.id.tvCorrect);
        tvCorrect.setText("Correct answers: " + correct);
        tvWrong = findViewById(R.id.tvWrong);
        tvWrong.setText("Wrong answers: " + wrong);
        tvGrade = findViewById(R.id.tvGrade);
        tvGrade.setText("Your grade: " + grade);

        // gets the quiz name from retrieved quiz object. it could be done simpler but in future updates we may be needing the object
        path.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        quiz = document.toObject(Quiz.class);

                        // init toolbar
                        mToolbar = findViewById(R.id.main_page_toolbar);
                        setSupportActionBar(mToolbar);
                        getSupportActionBar().setTitle("Report: " + quiz.getName());
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);

                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
