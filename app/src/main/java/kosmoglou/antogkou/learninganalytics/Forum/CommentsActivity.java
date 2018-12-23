package kosmoglou.antogkou.learninganalytics.Forum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.Models.CommentsModel;
import kosmoglou.antogkou.learninganalytics.R;

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = "CommentsActivity";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.commentsList)
    RecyclerView commentsList;

    // widgets
    public LinearLayoutManager linearLayoutManager;
    public Toolbar mToolbar;
    public EditText  etComment;
    public Button  buttonAdd;
    public ProgressDialog loadingBar;
    // firestore
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirestoreRecyclerAdapter adapter;
    public CollectionReference CurrentPost, postsCollection, usersCollection, profileInfo;
    public DocumentReference commentsCollection, currentPostDoc, currentUserPath, currentComment;
    public FirebaseAuth.AuthStateListener mAuthListener;
    // vars
    public String currentUserID = mAuth.getUid();
    public String userId, fullname, classroom_id;
    // =============== ANALYTIC TOOLSET =============== \\
    private static final String app_opened = "app_opnened";
    private static final String clicks = "clicks";
    private static final String quiz_taken = "quiz_taken";
    private static final String quiz_grade = "quiz_grade";
    private static final String class_grade = "class_grade";
    private static final String topic_opened = "topic_opened";
    private static final String post_written = "post_written";
    public int clicks_counter = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // init widgets & listeners
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        commentsList.setLayoutManager(linearLayoutManager);
        loadingBar = new ProgressDialog(this);




        etComment = findViewById(R.id.etComment);
        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (event.getRawX() >= (etComment.getRight() - etComment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        AddComment();

                        return true;
                    }
                }
                return false;
            }
        });

        classroom_id = getIntent().getExtras().getString("classroom_id"); // get the classroom_id from previous fragment to CommentsActivity
        CurrentPost = FirebaseFirestore.getInstance().collection("Posts");
        currentUserPath = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);

        getCommentsList();

        String title = getIntent().getStringExtra("textTitle");
        String description = getIntent().getStringExtra("textDescription");
        String postdate = getIntent().getStringExtra("textPostDate");
        String id = getIntent().getStringExtra("textDocumentId");
        //String userId = getIntent().getStringExtra(("textUserID"));
        //String creator_userID = getIntent().getStringExtra(" textCreator_UserID");

        profileInfo = db.collection("Classes").document(classroom_id).collection("Posts").document(id).collection("Comments");


        // Capture the layout's TextView and set the string as its text
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvDescription = findViewById(R.id.tvDescription);
        tvDescription.setText(description);

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(postdate);

        TextView tvCreator = findViewById(R.id.tvCreatorName);
        tvCreator.setText(id);


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void getCommentsList(){
        String id = getIntent().getStringExtra("textDocumentId");

        postsCollection = db.collection("Classes").document(classroom_id).collection("Posts");
        commentsCollection = postsCollection.document(id);

        Query query = commentsCollection.collection("Comments")
                .orderBy("comment_date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CommentsModel> response = new FirestoreRecyclerOptions.Builder<CommentsModel>()
                .setQuery(query, CommentsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CommentsModel, CommentsActivity.CommentsHolder>(response) {
            @Override
            public void onBindViewHolder(CommentsActivity.CommentsHolder holder, int position, CommentsModel model) {
                progressBar.setVisibility(View.GONE);

                holder.textComment.setText(model.getComment());

                try {
                    Date date = model.getComment_date();
                    if (date != null) {
                        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                        String creationDate = dateFormat.format(date);
                        holder.textTime.setText(creationDate);
                        Log.d("TAG", creationDate);
                    }
                    //holder.textTime.setText(model.getComment_date().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.post_commenter_fullname.setText(model.getLastname() + " " + model.getFirstname());
                holder.itemView.setTag(model.getCommentID());


                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //allow only the user that created the comment to edit it
                        if( currentUserID.equals(model.getUser_id())  ) {
                            //carry strings to editcommentactivity
                            Intent commentEditIntent = new Intent(getApplicationContext(), CommentEditActivity.class);
                            commentEditIntent.putExtra("comment_text", model.getComment());
                            commentEditIntent.putExtra("post_doc_id", id);
                            commentEditIntent.putExtra("commentID", model.getCommentID());
                            commentEditIntent.putExtra("classroom_id", classroom_id);
                            startActivity(commentEditIntent);
                        }

                        return true;
                    }
                });



                //query comments
                postsCollection = db.collection("Classes").document(classroom_id).collection("Posts");
                usersCollection = db.collection("Users");
                currentPostDoc = postsCollection.document(id);

            }

            @Override
            public CommentsActivity.CommentsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_commentsitems, group, false);

                return new CommentsActivity.CommentsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        //swipe to delete comments
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                // check if user is allowed to delete comments
                currentUserPath.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.getString("usertype").equals("Admin") || documentSnapshot.getString("usertype").equals("Teacher") ){
                                    final int position = viewHolder.getAdapterPosition();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this, R.style.AlertDialogStyle); // alert for confirm to delete
                                    builder.setMessage("Are you sure you want this comment to be deleted?");    //set message
                                    builder.setPositiveButton("remove", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            commentsList.getAdapter().notifyItemRemoved(position);
                                            //swipe to delete
                                            String commentID = (String) viewHolder.itemView.getTag();
                                            currentPostDoc.collection("Comments")
                                                    .document(commentID)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: Removed list item");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                        }
                                                    });
                                            adapter.notifyDataSetChanged();

                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            clearView(commentsList, viewHolder);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });


                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    alertDialog.setCanceledOnTouchOutside(false);

                                }else{
                                    //else
                                    adapter.notifyDataSetChanged();
                                    commentsList.setAdapter(adapter);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });




            }

        }).attachToRecyclerView(commentsList);
        //swipe to delete comments end
        adapter.notifyDataSetChanged();
        commentsList.setAdapter(adapter);
    }

    public class CommentsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textTime)
        TextView textTime;
        @BindView(R.id.textComment)
        TextView textComment;
        @BindView(R.id.post_commenter_fullname)
        TextView post_commenter_fullname;

        public CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void DeletePost() {
        String id = getIntent().getStringExtra("textDocumentId");
        CollectionReference itemsRef = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("Posts");

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

    private void AddComment() {
        String comment = etComment.getText().toString();
        String id = getIntent().getStringExtra("textDocumentId");

        final String current_user_id = mAuth.getCurrentUser().getUid();
        postsCollection = db.collection("Classes").document(classroom_id).collection("Posts");
        commentsCollection = postsCollection.document(id);

        if(comment.isEmpty()) {
            Toast.makeText(CommentsActivity.this, "You didn't input any text!", Toast.LENGTH_SHORT).show();
        }else {
            Map<String, Object> docData = new HashMap<>();
            docData.put("comment", comment);
            docData.put("user_id", currentUserID);
            docData.put("comment_date", FieldValue.serverTimestamp());

            postsCollection.document(id).collection("Comments")
                    .add(docData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            etComment.setText("");

                            String commentId = documentReference.getId();
                            Query queryusers = usersCollection.whereEqualTo("user_id", current_user_id);
                            queryusers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {

                                            WriteBatch batch = db.batch();
                                            DocumentReference sfRef = commentsCollection.collection("Comments").document(commentId);
                                            batch.update(sfRef, "firstname", document.getString("firstname"));
                                            batch.update(sfRef, "lastname", document.getString("lastname"));
                                            batch.update(sfRef, "commentID", commentId);

                                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "Commenter's firstname and lastname inserted successfully");
                                                    Log.d(TAG, "Comments ID inserted successfully!");
                                                }
                                            });
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                            Log.d(TAG, "Id inserted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
        analyticPostWritten();
    }

    private void EditPost() {
        String creator_userID = getIntent().getStringExtra("textCreator_UserID");
        String title = getIntent().getStringExtra("textTitle");
        String description = getIntent().getStringExtra("textDescription");
        String id = getIntent().getStringExtra("textDocumentId");


        if(currentUserID.equals(creator_userID)) {
            // carry strings to editPostActivity
            Intent postEditIntent = new Intent(getApplicationContext(), PostEditActivity.class);
            postEditIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            postEditIntent.putExtra("textTitle", title);
            postEditIntent.putExtra("textDescription",description);
            postEditIntent.putExtra("textDocumentId", id);
            postEditIntent.putExtra("classroom_id", classroom_id);
            startActivity(postEditIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String creator_userID = getIntent().getStringExtra("textCreator_UserID");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comments_menu, menu);
        //check if user is an admin/teacher and allow him to access the hidden menu
        MenuItem deletePost = menu.findItem(R.id.delete_post);
        MenuItem editPost = menu.findItem(R.id.edit_post);
        currentUserPath.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("usertype").equals("Admin") || documentSnapshot.getString("usertype").equals("Teacher") || creator_userID.equals(currentUserID)){
                            deletePost.setVisible(true);
                            editPost.setVisible(true);
                        }else{

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
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
                        .setTitle("Delete entry")
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
            case R.id.edit_post:
                EditPost();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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
                "1", "", "");// TODO value2 & classRoom
        currentUserPath.collection("Analytics").add(analytic);
    }
    // quiz grade
    private void analyticQuizGrade(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), quiz_grade,
                "", "", "");// TODO value1 & value2 & classRoom
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
                "1", "", getIntent().getExtras().getString("classroom_id"));
        currentUserPath.collection("Analytics").add(analytic);
    }
    // class grade
    private void analyticGrade(){
        Analytic analytic = new Analytic(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH) + 1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH), class_grade,
                "", "", ""); // TODO value1 & classRoom
        currentUserPath.collection("Analytics").add(analytic);
    }
}
