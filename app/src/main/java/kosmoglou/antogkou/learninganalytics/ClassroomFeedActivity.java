package kosmoglou.antogkou.learninganalytics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import java.util.HashMap;
import kosmoglou.antogkou.learninganalytics.Classes.Classroom;

public class ClassroomFeedActivity extends AppCompatActivity {
    //Firestore
    private String currentUserID = FirebaseAuth.getInstance().getUid();
    private CollectionReference classesRef = FirebaseFirestore.getInstance().collection("Classes");
    private CollectionReference enrolledClassesRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserID).collection("enrolled_classes");
    private DocumentReference usersRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
    private DocumentReference classesUserPath;
    // recycler adapter
    private ClassroomAdapter adapter;
    //vars
    public boolean isAdmin = false;
    //widgets
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_classroom_feed);

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // checks if user is admin
        checkAdmin();
        // sets up the recycler view
        setUpRecyclerView();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //On pressing back arrow on ActionBar
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public void checkAdmin(){
        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("usertype").equals("Admin") || documentSnapshot.getString("usertype").equals("Teacher")) {
                            Truifier(); // calls truifier method which sets value as true, to variable isAdmin
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {@Override public void onFailure(@NonNull Exception e) { }});
    }

    private void setUpRecyclerView(){
        Query query = classesRef;

        FirestoreRecyclerOptions<Classroom> options = new FirestoreRecyclerOptions.Builder<Classroom>()
                .setQuery(query, Classroom.class)
                .build();

        adapter = new ClassroomAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ClassroomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Classroom classroom = documentSnapshot.toObject(Classroom.class);
                String id = documentSnapshot.getId();
                try {
                    classesUserPath = FirebaseFirestore.getInstance().collection("Classes").document(id).collection("enrolled_students").document(currentUserID);
                }catch(Exception e){}

                // enrolls the user to the classroom
                if(isAdmin()) {
                    return; // if the user is Admin/Teacher do nothing, return
                }else if(!isAdmin()){
                    // if user is student, create alert dialog
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(ClassroomFeedActivity.this);
                    builder.setTitle("Enroll")
                            .setMessage("Are you sure you want to enroll in " + classroom.getClassname() + " ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    usersRef.get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        // registers the user to the classroom
                                                        String username = documentSnapshot.getString("username");
                                                        String firstname = documentSnapshot.getString("firstname");
                                                        String lastname = documentSnapshot.getString("lastname");
                                                        String usertype = documentSnapshot.getString("usertype");

                                                        HashMap userInfo = new HashMap();
                                                        userInfo.put("user_id", currentUserID);
                                                        userInfo.put("username", username);
                                                        userInfo.put("firstname", firstname);
                                                        userInfo.put("lastname", lastname);
                                                        userInfo.put("usertype", usertype);

                                                        classesUserPath.set(userInfo)
                                                                .addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        if (task.isSuccessful()) { } else { } }});
                                                    } // if (documentSnapshot.exists())
                                                } // onSuccess
                                            }); // Listener
                                    // adds the classroom to user's enrolled classes
                                    enrolledClassesRef.document(id).set(classroom).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){ }else{ } }});
                                    Toast.makeText(ClassroomFeedActivity.this,
                                            "Enrolled at: " + id, Toast.LENGTH_SHORT).show();
                                }
                            }) // positiveButton
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close the alert dialog
                                }
                            })
                            .setIcon(R.drawable.enroll)
                            .show();
                } // if isAdmin
            } // on Item Click Listener
        });
    } // Recycler

    public void Truifier(){
        isAdmin = true;
    }

    public boolean isAdmin(){
        return isAdmin;
    }

    // start listening
    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    // stop listening
    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

}
