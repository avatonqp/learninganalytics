package kosmoglou.antogkou.learninganalytics.Classroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import kosmoglou.antogkou.learninganalytics.R;

public class fragment_class_info extends Fragment {
  //widgets
  public TextView textViewDescription, textViewSyllabus;

  //firestore
  private DocumentReference classRef;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_class_info, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // gets the classrom_id from the TeacherClassroomActivity
    String classroom_id = getArguments().getString("classroom_id");
    // the path to current classroom
    classRef = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id);

    // init widgets
    textViewDescription = getView().findViewById(R.id.text_view_description_text);
    textViewSyllabus = getView().findViewById(R.id.text_view_syllabus_text);

    // get from db values of current classroom
    classRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                  String description = documentSnapshot.getString("description");
                  String syllabus = documentSnapshot.getString("syllabus");
                  textViewDescription.setText(description);
                  textViewSyllabus.setText(syllabus);
                }
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                //TODO
              }
            });
  }
}