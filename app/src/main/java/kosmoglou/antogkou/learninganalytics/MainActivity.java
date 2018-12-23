package kosmoglou.antogkou.learninganalytics;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import kosmoglou.antogkou.learninganalytics.Analytics.Analytic;
import kosmoglou.antogkou.learninganalytics.Student_Panel.StudentPanelActivity;
import kosmoglou.antogkou.learninganalytics.Teacher_Panel.TeacherPanelActivity;

public class MainActivity extends AppCompatActivity {
    //widgets
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public View headerView;
    public TextView drawerUsername;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public Toolbar mToolbar;
    //vars
    public String currentUserID;
    //firebase
    public FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_main);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getUid();
        try {
            currentUserPath = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
        }catch(Exception e){}

        //Toolbar call
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //navigation menu
        drawerLayout = findViewById(R.id.drawable_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.openDrawer(Gravity.LEFT);
        toggle.syncState();

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        headerView = navigationView.getHeaderView(0);
        drawerUsername = headerView.findViewById(R.id.nav_user_full_name);


        //navigation drawer listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //NavigationMenuSelect
    private boolean UserMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                isAdmin();
                break;
            case R.id.nav_profile:
                SendUserToSetupActivity();
                break;
            case R.id.all_classes:
                Intent mainIntent = new Intent (MainActivity.this, ClassroomFeedActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                break;
            case R.id.nav_Logout:
                mAuth.signOut(); // signs out the user
                SendUserToLoginActivity();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawable_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //
    private void CheckUserExistence() {
        currentUserPath.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            drawerUsername.setText(documentSnapshot.getString("firstname") + " " + documentSnapshot.getString("lastname") + "\n" + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        } else {
                            SendUserToSetupActivity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Exception: ", e.getMessage());
                    }
                });
    }
    //
    private void isAdmin() {
        currentUserPath.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("usertype").equals("Admin") || documentSnapshot.getString("usertype").equals("Teacher")){
                            SendUserToTeacherActivity();
                        }else{
                            SendUserToStudentActivity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    // sends to setup activity
    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
        startActivity(setupIntent);
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
    }
    // sends to login activity
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
        finish();
    }
    // sends to teacher activity
    private void SendUserToTeacherActivity() {
        Intent mainIntent = new Intent (MainActivity.this, TeacherPanelActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
    }
    // sends to student activity
    private void SendUserToStudentActivity() {
        Intent mainIntent = new Intent (MainActivity.this, StudentPanelActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
    }
    //
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawable_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
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
