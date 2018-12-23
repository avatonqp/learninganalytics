package kosmoglou.antogkou.learninganalytics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import kosmoglou.antogkou.learninganalytics.Classes.Quiz;

public class QuizActivity extends AppCompatActivity {
    // views
    public TextView tvDescription, tvDuration, tvTotalQuestions;
    public Toolbar mToolbar;
    public Button buttonTake;
    // vars
    public Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz);

        // get key values from previous activity, in order to get the path for the selected quiz
        Intent intent = getIntent();
        String quiz_id = intent.getExtras().getString("quiz_id");
        String classroom_id = intent.getExtras().getString("classroom_id");
        DocumentReference path = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes").document(quiz_id);

        // retrieves the quiz object from firestore, gets the parameters, sets up the views etc
        path.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        quiz = document.toObject(Quiz.class);

                        // init views
                        tvDescription = findViewById(R.id.tvDescription);
                        tvDuration = findViewById(R.id.tvDuration);
                        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
                        buttonTake = findViewById(R.id.buttonTake);

                        tvDescription.setText(quiz.getDescription());
                        tvDuration.setText("Duration: " + quiz.getDuration());
                        tvTotalQuestions.setText("Questions: " + quiz.getTotalQuestions());

                        // init toolbar
                        mToolbar = findViewById(R.id.main_page_toolbar);
                        setSupportActionBar(mToolbar);
                        getSupportActionBar().setTitle(quiz.getName());
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);

                        // listeners
                        buttonTake.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent quizTestIntent = new Intent(QuizActivity.this, QuizTestActivity.class);
                                quizTestIntent.putExtra("classroom_id", classroom_id);
                                quizTestIntent.putExtra("quiz_id", quiz_id);
                                startActivity(quizTestIntent);
                                finish();
                            }
                        });
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
