package kosmoglou.antogkou.learninganalytics.Forum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import kosmoglou.antogkou.learninganalytics.R;

public class CommentEditActivity extends AppCompatActivity {
    private static final String TAG = "CommentEditActivity";
    // widgets
    public Button buttonSubmit;
    public EditText etComment;
    public Toolbar mToolbar;
    // firestore
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();;
    public CollectionReference postsCollection;
    public DocumentReference commentsCollection;
    // vars
    public String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comment_edit);

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initViews();

        currentUserID = mAuth.getCurrentUser().getUid();

        String comment = getIntent().getStringExtra("comment_text");
        etComment.setText(comment);
        String document_id = getIntent().getStringExtra("post_doc_id");
        String comment_id = getIntent().getStringExtra("commentID");
        String classroom_id = getIntent().getStringExtra("classroom_id");

        postsCollection = db.collection("Classes").document(classroom_id).collection("Posts");
        commentsCollection = postsCollection.document(document_id);
    }

    private void initViews() {
        etComment = findViewById(R.id.etComment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editComment();
            }
        });
    }

    private void editComment() {

        String creatoruserid = mAuth.getCurrentUser().getUid();
        String comment = etComment.getText().toString();
        String document_id = getIntent().getStringExtra("post_doc_id");
        String comment_id = getIntent().getStringExtra("commentID");
        commentsCollection = postsCollection.document(document_id);

        if(comment.isEmpty()){
            Toast.makeText(CommentEditActivity.this, "Text is required", Toast.LENGTH_SHORT).show();
        }else {

            commentsCollection.collection("Comments").document(comment_id)
                    .update("comment", comment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CommentEditActivity.this, "Edit was successful!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error editing comment", e);
                        }
                    });


        }

    }

    private void DeleteComment() {
        String comment_id = getIntent().getStringExtra("commentID");
        String document_id = getIntent().getStringExtra("post_doc_id");
        String classroom_id = getIntent().getStringExtra("classroom_id");
        CollectionReference itemsRef = db.collection("Classes").document(classroom_id).collection("Posts");
        CollectionReference commentsRef = itemsRef.document(document_id).collection("Comments");

        Query qid = commentsRef.whereEqualTo("commentID", comment_id);
        qid.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        commentsCollection.collection("Comments").document(comment_id).delete();
                        finish();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.commentedit_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_comment:
                new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DeleteComment();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
