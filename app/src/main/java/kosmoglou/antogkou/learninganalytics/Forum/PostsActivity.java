package kosmoglou.antogkou.learninganalytics.Forum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.Models.PostsModel;
import kosmoglou.antogkou.learninganalytics.R;

public class PostsActivity extends AppCompatActivity {
    private static final String TAG = "PostsActivity";
    // widgets
    public Button buttonSubmit;
    public EditText etTitle, etDescription;
    public Toolbar mToolbar;
    // firestore
    public FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public DocumentReference profileInfo;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // vars
    public String classroom_id;
    public String currentUserID = mAuth.getCurrentUser().getUid();
    public String documentId;
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
        setContentView(R.layout.activity_posts);

        profileInfo = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
        classroom_id = getIntent().getExtras().getString("classroom_id"); // get the classroom_id from previous fragment to PostsActivity

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    // toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    // init views & listeners
    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // listeners
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });

    }
    // create new post
    private void createPost() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();


        if(title.isEmpty()|| description.isEmpty()){
            Toast.makeText(PostsActivity.this, "Both fields Required", Toast.LENGTH_SHORT).show();
        }else {
            PostsModel postsModel = new PostsModel(title, description, currentUserID, documentId);

            mFirestore.collection("Classes").document(classroom_id).collection("Posts")
                    .add(postsModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            etTitle.setText("");
                            etDescription.setText("");
                            String docId = documentReference.getId();
                            mFirestore.collection("Classes").document(classroom_id).collection("Posts").document(docId)
                                    .update("documentId", docId)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "id inserted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating documentid", e);
                                        }
                                    });
                            //end activity after posting
                            analyticTopicOpened();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });


        }

    }

    // =============== ANALYTIC METHODS =============== \\
    // app opened counter
    private void analyticAppOpened(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), app_opened,
                "1", "", "");
        profileInfo.collection("Analytics").add(analytic);
    }
    // clicks counter
    private void analyticAppClicks(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), clicks,
                "" + clicks_counter, "", "");
        profileInfo.collection("Analytics").document(clicks).set(analytic);
    }
    // quiz taken counter
    private void analyticQuizTaken(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), quiz_taken,
                "1", getIntent().getExtras().getString("quiz_id"), getIntent().getExtras().getString("classroom_id"));
        profileInfo.collection("Analytics").add(analytic);
    }
    // quiz grade
    private void analyticQuizGrade(float grade){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), quiz_grade,
                ""+grade, getIntent().getExtras().getString("quiz_id"), getIntent().getExtras().getString("classroom_id"));
        profileInfo.collection("Analytics").add(analytic);
    }
    // topic opened counter
    private void analyticTopicOpened(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), topic_opened,
                "1", "", getIntent().getExtras().getString("classroom_id"));
        profileInfo.collection("Analytics").add(analytic);
    }
    // post written counter
    private void analyticPostWritten(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), post_written,
                "1", "", "");
        profileInfo.collection("Analytics").add(analytic);
    }
    // class grade
    private void analyticGrade(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), class_grade,
                "", "", ""); // TODO value1 & classRoom
        profileInfo.collection("Analytics").add(analytic);
    }
}
