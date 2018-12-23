package kosmoglou.antogkou.learninganalytics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    //widgets
    public Button buttonSaveInformation;
    public Button buttonCancel;
    public Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextFathersname;
    private EditText editTextAM;
    private EditText editTextBorndate;
    private EditText editTextPhone;

    //vars
    private String currentUserID;

    //Firebase
    private DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setup);

        //Firebase
        currentUserID = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);

        //init widgets
        editTextUsername = findViewById(R.id.setup_username);
        editTextEmail = findViewById(R.id.setup_email);
        editTextEmail.setHintTextColor(getResources().getColor(R.color.black));
        editTextEmail.setEnabled(false);
        editTextEmail.setHint("E-mail: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        editTextFirstName = findViewById(R.id.setup_first_name);
        editTextLastName = findViewById(R.id.setup_last_name);
        editTextFathersname = findViewById(R.id.setup_fathersname);
        editTextAM = findViewById(R.id.setup_AM);
        editTextBorndate = findViewById(R.id.setup_born_date);
        editTextPhone = findViewById(R.id.setup_phone);
        buttonSaveInformation = findViewById(R.id.setup_information_button);
        buttonCancel = findViewById(R.id.setup_cancel_button);

        loadingBar = new ProgressDialog(this);

        // init toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Personal Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Getting the personal info from db if exist
        GetAccountInformation();

        // listeners
        buttonSaveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToMainActivity();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // on pressing back arrow on toolbar
                SendUserToMainActivity();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void GetAccountInformation(){
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            // Saving the fields from documentsnapshot to local strings
                            String username = documentSnapshot.getString("username");
                            String firstname = documentSnapshot.getString("firstname");
                            String lastname = documentSnapshot.getString("lastname");
                            String fathersname = documentSnapshot.getString("fathersname");
                            String AM = documentSnapshot.getString("AM");
                            String borndate = documentSnapshot.getString("borndate");
                            String phone = documentSnapshot.getString("phone");

                            // Setting all the edittexts that can't be updated, to color black and to enabled false
                            editTextUsername.setHintTextColor(getResources().getColor(R.color.black));
                            editTextUsername.setEnabled(false);
                            editTextFirstName.setHintTextColor(getResources().getColor(R.color.black));
                            editTextFirstName.setEnabled(false);
                            editTextLastName.setHintTextColor(getResources().getColor(R.color.black));
                            editTextLastName.setEnabled(false);
                            editTextFathersname.setHintTextColor(getResources().getColor(R.color.black));
                            editTextFathersname.setEnabled(false);
                            editTextAM.setHintTextColor(getResources().getColor(R.color.black));
                            editTextAM.setEnabled(false);
                            // Setting the Hints
                            editTextUsername.setHint("Username: " + username);
                            editTextFirstName.setHint("First Name: " + firstname);
                            editTextLastName.setHint("Last Name: " + lastname);
                            editTextFathersname.setHint("Father's Name: " + fathersname);
                            editTextAM.setHint("Registration Number: " + AM);
                            editTextBorndate.setHint("Born Date: " + borndate);
                            editTextPhone.setHint("Phone Number: " + phone);
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

    private void SaveAccountSetupInformation() {
        String username = editTextUsername.getText().toString();
        String firstname = editTextFirstName.getText().toString();
        String lastname = editTextLastName.getText().toString();
        String fathersname = editTextFathersname.getText().toString();
        String AM = editTextAM.getText().toString();
        String borndate = editTextBorndate.getText().toString();
        String phone = editTextPhone.getText().toString();
        String usertype = "Student";

            //TODO IF editTextUsername/FULLNAME EXISTS
            HashMap userInfo = new HashMap();
            if(editTextUsername.isEnabled() && editTextFirstName.isEnabled() && editTextFathersname.isEnabled() && editTextAM.isEnabled()){
                // FIRST TIME SAVING INFORMATION TO SERVER
                // check for empty fields
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(this, "Username field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(this, "First name field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(this, "Last name field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(fathersname)) {
                    Toast.makeText(this, "Father's Name field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(AM)) {
                    Toast.makeText(this, "Registration Number field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(borndate)) {
                    Toast.makeText(this, "Born Date field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "Phone field is empty", Toast.LENGTH_SHORT).show();
                }else {
                    userInfo.put("username", username);
                    userInfo.put("firstname", firstname);
                    userInfo.put("lastname", lastname);
                    userInfo.put("fathersname", fathersname);
                    userInfo.put("AM", AM);
                    userInfo.put("borndate", borndate);
                    userInfo.put("phone", phone);

                    userInfo.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()); //AUTO
                    userInfo.put("usertype", usertype); //AUTO AS STUDENT
                    userInfo.put("user_id", currentUserID); //AUTO

                    // calling a loading bar
                    loadingBar.setTitle("Saving Information");
                    loadingBar.setMessage("Please wait, your information is being saved on the server...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    userRef.set(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(SetupActivity.this, "All information is saved successfuly", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message =  task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }else {
                // UPDATING INFORMATION TO SERVER
                // check for empty fields
                if (TextUtils.isEmpty(borndate)) {
                    Toast.makeText(this, "Born Date field is empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "Phone field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    // calling a loading bar
                    loadingBar.setTitle("Updating Information");
                    loadingBar.setMessage("Please wait, your information is being updated on the server...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    userInfo.put("borndate", borndate);
                    userInfo.put("phone", phone);

                    userRef.update(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(SetupActivity.this, "Personal Information updated successfuly", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }
    }

    private void SendUserToMainActivity() {
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
    }
}
