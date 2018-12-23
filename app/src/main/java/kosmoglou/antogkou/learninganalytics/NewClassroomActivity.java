package kosmoglou.antogkou.learninganalytics;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import kosmoglou.antogkou.learninganalytics.Classes.Classroom;

public class NewClassroomActivity extends AppCompatActivity {
  //widgets
  private EditText editTextTitle;
  private EditText editTextDescription;
  private EditText editTextSyllabus;
  private EditText editTextClassCode;
  private Toolbar mToolbar;
  //firebase
  public String createdby = FirebaseAuth.getInstance().getUid();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // hides notification bar
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_new_classroom);

    //Toolbar call
    mToolbar = findViewById(R.id.main_page_toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle("Create New Class");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    //init widgets
    editTextTitle = findViewById(R.id.edit_text_classroom_title);
    editTextDescription = findViewById(R.id.edit_text_classroom_description);
    editTextSyllabus = findViewById(R.id.edit_text_classroom_syllabus);
    editTextClassCode = findViewById(R.id.edit_text_classroom_class_code);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.new_class_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()){
      case android.R.id.home:
        finish();
        return true;
      case R.id.save_classroom:
        saveClass();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void saveClass() {
    String title = editTextTitle.getText().toString();
    String description = editTextDescription.getText().toString();
    String syllabus = editTextSyllabus.getText().toString();
    String classCode = editTextClassCode.getText().toString();

    if (title.trim().isEmpty() || description.trim().isEmpty() || syllabus.trim().isEmpty() || classCode.trim().isEmpty()) {
      Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
      return;
    }
    //TODO WHATIF CLASSROOM TITLE ALREADY EXISTS?
    Classroom classroom = new Classroom(title, description, classCode, syllabus, createdby);
    db.collection("Classes").document(title).set(classroom)
            .addOnSuccessListener(new OnSuccessListener() {
              @Override
              public void onSuccess(Object o) {
                Toast.makeText(NewClassroomActivity.this, "New classroom created.", Toast.LENGTH_SHORT).show();
                finish();
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewClassroomActivity.this, "Error occured while trying to create new classroom.", Toast.LENGTH_SHORT).show();
              }
            });
  }
}
