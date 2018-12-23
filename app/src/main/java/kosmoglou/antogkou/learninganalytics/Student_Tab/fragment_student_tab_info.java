package kosmoglou.antogkou.learninganalytics.Student_Tab;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kosmoglou.antogkou.learninganalytics.R;

public class fragment_student_tab_info extends Fragment {
    // firestore
    DocumentReference studentRef;

    // widgets
    TextView textViewFirstName, textViewLastName, textViewUsername, textViewEmail, textViewAM, textViewPhone, textViewFathersName, textViewBornDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_tab_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // gets the student_id from the TeacherClassroomActivity
        String student_id = getArguments().getString("student_id");
        // the path to current classroom
        studentRef = FirebaseFirestore.getInstance().collection("Users").document(student_id);

        // init widgets
        textViewFirstName = getView().findViewById(R.id.text_view_student_tab_first_name);
        textViewLastName = getView().findViewById(R.id.text_view_student_tab_last_name);
        textViewUsername = getView().findViewById(R.id.text_view_student_tab_username);
        textViewEmail = getView().findViewById(R.id.text_view_student_tab_email);
        textViewAM = getView().findViewById(R.id.text_view_student_tab_AM);
        textViewPhone = getView().findViewById(R.id.text_view_student_tab_phone);
        textViewFathersName = getView().findViewById(R.id.text_view_student_tab_fathersname);
        textViewBornDate = getView().findViewById(R.id.text_view_student_tab_born_date);

        // get from db values of current classroom
        studentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String firstname = documentSnapshot.getString("firstname");
                            String lastname = documentSnapshot.getString("lastname");
                            String username = documentSnapshot.getString("username");
                            String usertype = documentSnapshot.getString("usertype");
                            String AM = documentSnapshot.getString("AM");
                            String fathersname = documentSnapshot.getString("fathersname");
                            String borndate = documentSnapshot.getString("borndate");
                            String email = documentSnapshot.getString("email");
                            String phone = documentSnapshot.getString("phone");
                            textViewFirstName.setText("Firstname: " + firstname);
                            textViewLastName.setText("Lastname: " + lastname);
                            textViewUsername.setText("Username: " + username);
                            textViewEmail.setText("E-mail: " + email);
                            textViewAM.setText("AM: " + AM);
                            textViewPhone.setText("Phone: " + phone);
                            textViewFathersName.setText("Father's Name: " + fathersname);
                            textViewBornDate.setText("Borndate: " + borndate);
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

