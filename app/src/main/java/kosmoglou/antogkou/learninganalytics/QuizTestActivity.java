package kosmoglou.antogkou.learninganalytics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.Classes.Question;
import kosmoglou.antogkou.learninganalytics.Classes.Quiz;

public class QuizTestActivity extends AppCompatActivity {
    // views
    public Toolbar mToolbar;
    public TextView tvQuestionNumber, tvQuestion, tvTime;
    public Button buttonNext;
    public RadioGroup radioGroup;
    public RadioButton rOne, rTwo, rThree, rFour;
    // vars
    public Quiz quiz;
    public List<Question> questions = new ArrayList<Question>();
    public int current = 1;
    public int correct = 0;
    public int wrong = 0;
    public String currentUserID = FirebaseAuth.getInstance().getUid();
    public CountDownTimer timer;
    // cons
    private static final String FORMAT = "%02d:%02d:%02d";
    // firestore
    public DocumentReference currentUserPath;
    // =============== ANALYTIC TOOLSET =============== \\
    private static final String app_opened = "app_opnened";
    private static final String clicks = "clicks";
    private static final String quiz_taken = "quiz_taken";
    private static final String quiz_grade = "quiz_grade";
    private static final String class_grade = "class_grade";
    private static final String topic_opened = "topic_opened";
    private static final String post_written = "post_written";
    public int clicks_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz_test);

        //firebase
        try {
            currentUserPath = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
        }catch(Exception e){}

        // get key values from previous activity, in order to get the path for the selected quiz
        Intent intent = getIntent();
        String quiz_id = intent.getExtras().getString("quiz_id");
        String classroom_id = intent.getExtras().getString("classroom_id");
        DocumentReference path = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes").document(quiz_id);
        // retrieves the quiz object from firestore, gets the questions, sets up the views etc
        path.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            quiz = document.toObject(Quiz.class);
                            questions = quiz.getQuestions();

                            // init toolbar
                            mToolbar = findViewById(R.id.main_page_toolbar);
                            setSupportActionBar(mToolbar);
                            getSupportActionBar().setTitle(quiz.getName());
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeButtonEnabled(true);

                            // ini widgets
                            tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
                            tvQuestionNumber.setText("Question " + current +"/" + quiz.getTotalQuestions());
                            tvTime = findViewById(R.id.tvTime);
                            timer();
                            tvQuestion = findViewById(R.id.tvQuestion);
                            tvQuestion.setText(questions.get(current - 1).getTheQuestion());
                            radioGroup = findViewById(R.id.radioGroup);
                            rOne = findViewById(R.id.rOne);
                            rTwo = findViewById(R.id.rTwo);
                            rThree = findViewById(R.id.rThree);
                            rFour = findViewById(R.id.rFour);
                            buttonNext = findViewById(R.id.buttonNext);
                            shuffle();
                            buttonNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    next();
                                }
                            });
                        }else{
                            // task is not successful
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // toast?
                    }
                });
    }
    // inits the next question or if it was the last question, deactivates all views and sends user to report activity
    public void next(){
        if(current == quiz.getTotalQuestions()){
            checkCorrect(); // checks if answered correctly
            buttonNext.setEnabled(false);
            goReport(); // sends to ReportActivity
        }else{
            checkCorrect(); // checks if answered correctly
            current += 1;
            tvQuestionNumber.setText("Question " + current +"/" + quiz.getTotalQuestions());
            tvQuestion.setText(questions.get(current - 1).getTheQuestion());
            shuffle(); // shuffles the answers
        }
    }
    // shuffles the answers
    public void shuffle(){
        // randomly picks which radiobutton will have the correct answer and sets all radiobuttons with answers
        int[] answers = {1, 2, 3, 4};
        int random = new Random().nextInt(answers.length);
        int correct_answer = answers[random];

        if(correct_answer == 1){
            rOne.setChecked(false);
            rTwo.setChecked(false);
            rThree.setChecked(false);
            rFour.setChecked(false);
            rOne.setText(questions.get(current - 1).answerCorrect);
            rTwo.setText(questions.get(current - 1).answerTwo);
            rThree.setText(questions.get(current - 1).answerThree);
            rFour.setText(questions.get(current - 1).answerFour);
        }else if(correct_answer == 2){
            rOne.setChecked(false);
            rTwo.setChecked(false);
            rThree.setChecked(false);
            rFour.setChecked(false);
            rOne.setText(questions.get(current - 1).answerTwo);
            rTwo.setText(questions.get(current - 1).answerCorrect);
            rThree.setText(questions.get(current - 1).answerThree);
            rFour.setText(questions.get(current - 1).answerFour);
        }else if(correct_answer == 3){
            rOne.setChecked(false);
            rTwo.setChecked(false);
            rThree.setChecked(false);
            rFour.setChecked(false);
            rOne.setText(questions.get(current - 1).answerTwo);
            rTwo.setText(questions.get(current - 1).answerThree);
            rThree.setText(questions.get(current - 1).answerCorrect);
            rFour.setText(questions.get(current - 1).answerFour);
        }else if(correct_answer == 4){
            rOne.setChecked(false);
            rTwo.setChecked(false);
            rThree.setChecked(false);
            rFour.setChecked(false);
            rOne.setText(questions.get(current - 1).answerTwo);
            rTwo.setText(questions.get(current - 1).answerThree);
            rThree.setText(questions.get(current - 1).answerFour);
            rFour.setText(questions.get(current - 1).answerCorrect);
        }else{
            rOne.setChecked(false);
            rTwo.setChecked(false);
            rThree.setChecked(false);
            rFour.setChecked(false);
            rOne.setText(questions.get(current - 1).answerCorrect);
            rTwo.setText(questions.get(current - 1).answerTwo);
            rThree.setText(questions.get(current - 1).answerThree);
            rFour.setText(questions.get(current - 1).answerFour);
        }
    }
    // checks if selected answer is correct
    public void checkCorrect(){
        if(rOne.getText().toString().equals(questions.get(current - 1).getAnswerCorrect()) && rOne.isChecked()){
            correct += 1;
        }else if(rTwo.getText().toString().equals(questions.get(current - 1).getAnswerCorrect()) && rTwo.isChecked()){
            correct += 1;
        }else if(rThree.getText().toString().equals(questions.get(current - 1).getAnswerCorrect()) && rThree.isChecked()){
            correct += 1;
        }else if(rFour.getText().toString().equals(questions.get(current - 1).getAnswerCorrect()) && rFour.isChecked()){
            correct += 1;
        }else{
            wrong += 1;
        }
    }
    // starts the countdown timer
    public void timer(){
        timer = new CountDownTimer(quiz.getDuration() * 60000, 1000) {
            public void onTick(long millisUntilFinished) {
                @SuppressLint("DefaultLocale") String x = "Time Remaining: "+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvTime.setText(x);
            }
            public void onFinish() {
                tvTime.setText("Time Remaining: 00:00:00");
                tvTime.setTextColor(getResources().getColor(R.color.red));
                // disables all views
                rOne.setEnabled(false);
                rTwo.setEnabled(false);
                rThree.setEnabled(false);
                rFour.setEnabled(false);
                buttonNext.setEnabled(false);
                //toast?
                goReport();
            }
        }.start();
    }
    // go to report activity
    public void goReport(){
        timer.cancel();
        float grade = ((float) correct/(float) quiz.getTotalQuestions()) * (float) 10; // correct answers / total questions multiplied by 10
        analyticQuizTaken();
        analyticQuizGrade(grade);

        Intent quizReportIntent = new Intent(QuizTestActivity.this, QuizReportActivity.class);
        quizReportIntent.putExtra("correct", correct);
        quizReportIntent.putExtra("wrong", wrong);
        quizReportIntent.putExtra("grade", grade);
        quizReportIntent.putExtra("classroom_id", getIntent().getExtras().getString("classroom_id"));
        quizReportIntent.putExtra("quiz_id", getIntent().getExtras().getString("quiz_id"));
        startActivity(quizReportIntent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    // =============== ANALYTIC METHODS =============== \\
    // app opened counter
    private void analyticAppOpened(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), app_opened,
                "1", "", "");
        currentUserPath.collection("Analytics").add(analytic);
    }
    // clicks counter
    private void analyticAppClicks(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), clicks,
                "" + clicks_counter, "", "");
        currentUserPath.collection("Analytics").document(clicks).set(analytic);
    }
    // quiz taken counter
    private void analyticQuizTaken(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), quiz_taken,
                "1", getIntent().getExtras().getString("quiz_id"), getIntent().getExtras().getString("classroom_id"));
        currentUserPath.collection("Analytics").add(analytic);
    }
    // quiz grade
    private void analyticQuizGrade(float grade){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), quiz_grade,
                ""+grade, getIntent().getExtras().getString("quiz_id"), getIntent().getExtras().getString("classroom_id"));
        currentUserPath.collection("Analytics").add(analytic);
    }
    // topic opened counter
    private void analyticTopicOpened(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), topic_opened,
                "1", "", "");
        currentUserPath.collection("Analytics").add(analytic);
    }
    // post written counter
    private void analyticPostWritten(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), post_written,
                "1", "", "");
        currentUserPath.collection("Analytics").add(analytic);
    }
    // class grade
    private void analyticGrade(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), class_grade,
                "", "", ""); // TODO value1 & classRoom
        currentUserPath.collection("Analytics").add(analytic);
    }
}

