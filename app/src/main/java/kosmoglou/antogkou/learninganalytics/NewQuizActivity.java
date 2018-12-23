package kosmoglou.antogkou.learninganalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import kosmoglou.antogkou.learninganalytics.Classes.Question;
import kosmoglou.antogkou.learninganalytics.Classes.Quiz;

public class NewQuizActivity extends AppCompatActivity {
  // widgets
  public Toolbar mToolbar;
  public Button buttonNextQuestion;
  public Button buttonSet;
  public EditText etName, etTotalQuestions, etDuration, etDescription;
  public EditText etQuestion, etCorrectAnswer, etAnswerTwo, etAnswerThree, etAnswerFour;
  public TextView tvQuestion;
  // vars
  public List<Question> questions = new ArrayList<Question>();
  public int total = 1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // hides notification bar
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_new_quiz);

    // init toolbar
    mToolbar = findViewById(R.id.main_page_toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("Create New Quiz");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    // get classroom_id from previous activity
    Intent intent = getIntent();
    String classroom_id = intent.getExtras().getString("classroom_id");

    // init widgets
    etName = findViewById(R.id.etName);
    etDescription = findViewById(R.id.etDescription);
    etTotalQuestions = findViewById(R.id.etTotalQuestions);
    etDuration = findViewById(R.id.etDuration);
    buttonSet = findViewById(R.id.buttonSet);
    tvQuestion = findViewById(R.id.tvQuestion);

    buttonSet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name = etName.getText().toString();
        String stotalQuestions = etTotalQuestions.getText().toString();
        String sduration = etDuration.getText().toString();
        String description = etDescription.getText().toString();
        // if checks for empty fields
        if(TextUtils.isEmpty(name)){
          Toast.makeText(NewQuizActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(stotalQuestions)){
          Toast.makeText(NewQuizActivity.this, "Total questions field has incorrect value", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(sduration)){
          Toast.makeText(NewQuizActivity.this, "Duration field has incorrect value", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(description)){
          Toast.makeText(NewQuizActivity.this, "Description field has incorrect value", Toast.LENGTH_SHORT).show();
        }else{
          tvQuestion.setText("Question " + 1);
          int totalQuestions = Integer.parseInt(etTotalQuestions.getText().toString());
          int duration = Integer.parseInt(etDuration.getText().toString());

          // init widgets & visibility & enabled
          etQuestion = findViewById(R.id.etQuestion);
          etQuestion.setEnabled(true);
          etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
          etCorrectAnswer.setEnabled(true);
          etAnswerTwo = findViewById(R.id.etAnswerTwo);
          etAnswerTwo.setEnabled(true);
          etAnswerThree = findViewById(R.id.etAnswerThree);
          etAnswerThree.setEnabled(true);
          etAnswerFour = findViewById(R.id.etAnswerFour);
          etAnswerFour.setEnabled(true);
          buttonNextQuestion = findViewById(R.id.buttonNextQuestion);
          buttonNextQuestion.setEnabled(true);

          etName.setEnabled(false);
          etDescription.setEnabled(false);
          etTotalQuestions.setEnabled(false);
          etDuration.setEnabled(false);
          buttonSet.setEnabled(false);

          // init listeners
          buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              tvQuestion.setText("Question " + total);
              if (total <= totalQuestions) {
                total += 1;

                String question = etQuestion.getText().toString();
                String correct = etCorrectAnswer.getText().toString();
                String answer_two = etAnswerTwo.getText().toString();
                String answer_three = etAnswerThree.getText().toString();
                String answer_four = etAnswerFour.getText().toString();

                // if checks for empty fields
                if (TextUtils.isEmpty(question)) {
                  Toast.makeText(NewQuizActivity.this, "Question field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(correct)) {
                  Toast.makeText(NewQuizActivity.this, "Correct Answer field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(answer_two)) {
                  Toast.makeText(NewQuizActivity.this, "Answer 2 field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(answer_three)) {
                  Toast.makeText(NewQuizActivity.this, "Answer 3 field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(answer_four)) {
                  Toast.makeText(NewQuizActivity.this, "Answer 4 field is empty", Toast.LENGTH_SHORT).show();
                } else {
                  tvQuestion.setText("Question " + total);
                  // creates the new question and adds it to the ListArray
                  questions.add(new Question(question, correct, answer_two, answer_three, answer_four));
                  // resets the values of views, for the next question
                  etQuestion.setText("");
                  etCorrectAnswer.setText("");
                  etAnswerTwo.setText("");
                  etAnswerThree.setText("");
                  etAnswerFour.setText("");
                  if(total > totalQuestions){
                    // if last question was last question: set question as complete, change the arrow icon to save icon, disable all views
                    tvQuestion.setText("COMPLETE");
                    buttonNextQuestion.setBackground(getResources().getDrawable(R.drawable.ic_save_gray));
                    etQuestion.setEnabled(false);
                    etCorrectAnswer.setEnabled(false);
                    etAnswerTwo.setEnabled(false);
                    etAnswerThree.setEnabled(false);
                    etAnswerFour.setEnabled(false);
                  }
                }
              }else{
                // if save icon was clicked, initialize the new Quiz object and save it to firestore
                CollectionReference path = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes");
                Quiz quiz = new Quiz();
                quiz.setDuration(duration);
                quiz.setDescription(description);
                quiz.setName(name);
                quiz.setTotalQuestions(totalQuestions);
                quiz.setQuestions(questions);
                try{
                  path.document(name).set(quiz);
                }catch(Exception e){}
                finish();
              }
            }
          });
        }
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
