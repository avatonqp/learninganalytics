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

public class PostEditActivity extends AppCompatActivity {
    private static final String TAG = "PostEditActivity";
    // widgets
    public Button buttonSubmit;
    public EditText etTitle, etDescription;
    public Toolbar mToolbar;
    // firestore
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference postsCollection;
    public DocumentReference currentPost,currentUserPath;
    // vars
    public String currentUserID = mAuth.getUid();
    public String classroom_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_post_edit);

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUserPath = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
        currentUserID = mAuth.getCurrentUser().getUid();
        initViews();

        String title = getIntent().getStringExtra("textTitle");
        String description = getIntent().getStringExtra("textDescription");
        String postId = getIntent().getStringExtra("textDocumentId");
        classroom_id = getIntent().getExtras().getString("classroom_id"); // get the classroom_id from previous fragment to CommentsActivity
        etTitle.setText(title);
        etDescription.setText(description);

        postsCollection = db.collection("Classes").document(classroom_id).collection("Posts");
        currentPost = postsCollection.document(postId);
    }
    private void initViews() {
        // init widgets & listeners
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
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
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String postId = getIntent().getStringExtra("textDocumentId");

        //currentpost = postscollection.document(post_id);

        if(title.isEmpty() || description.isEmpty()){
            Toast.makeText(PostEditActivity.this, "Text is required", Toast.LENGTH_SHORT).show();
        }else {

            postsCollection.document(postId)
                    .update("title", title)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(PostEditActivity.this, "Edit was successful!", Toast.LENGTH_SHORT).show();
                            db.collection("Posts").document(postId)
                                    .update("description", description)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(PostEditActivity.this, "Edit was successful!", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error editing comment", e);
                                        }
                                    });
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

    private void DeletePost() {
        String id = getIntent().getStringExtra("textDocumentId");
        CollectionReference itemsRef = db.collection("Classes").document(classroom_id).collection("Posts");

        Query qid = itemsRef.whereEqualTo("documentId", id);
        qid.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        itemsRef.document(document.getId()).delete();
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
        inflater.inflate(R.menu.comments_menu, menu);
        String creator_userID = getIntent().getStringExtra("textCreator_UserID");
        //check if user is an admin/teacher and allow him to access the hidden menu
        MenuItem deletePost = menu.findItem(R.id.delete_post);
        currentUserPath.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("usertype").equals("Admin") || documentSnapshot.getString("usertype").equals("Teacher") || creator_userID==creator_userID){
                            deletePost.setVisible(true);
                        }else{

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_post:
                new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                        .setTitle("Delete Post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DeletePost();
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
