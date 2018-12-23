package kosmoglou.antogkou.learninganalytics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    //widgets
    private EditText editTextUserEmail, editTextUserPassword, editTextUserConfirmPassword;
    private Button buttonCreateAccount;
    private ProgressDialog loadingBar;
    private Toolbar mToolbar;

    //Firebasedb
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        //firebase db
        mAuth = FirebaseAuth.getInstance();

        //init widgets
        editTextUserEmail = findViewById(R.id.register_email);
        editTextUserPassword = findViewById(R.id.register_password);
        editTextUserConfirmPassword = findViewById(R.id.register_confirm_password);
        buttonCreateAccount = findViewById(R.id.register_create_account);
        loadingBar = new ProgressDialog(this);

        //Toolbar call
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //button listener
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent (RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
        finish();
    }

    private void CreateNewAccount() {
        String email = editTextUserEmail.getText().toString();
        String password = editTextUserPassword.getText().toString();
        String confirmpassword = editTextUserConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmpassword)) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "Your password does not match, please try again", Toast.LENGTH_SHORT).show();
        } else{

            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait, your account is being created...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToSetupActivity();
                                Toast.makeText(RegisterActivity.this, "User account created successfully!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(setupIntent);
        finish();
    }
}