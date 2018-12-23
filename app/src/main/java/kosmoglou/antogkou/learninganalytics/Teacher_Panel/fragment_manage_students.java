package kosmoglou.antogkou.learninganalytics.Teacher_Panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import kosmoglou.antogkou.learninganalytics.Classes.Student;
import kosmoglou.antogkou.learninganalytics.R;
import kosmoglou.antogkou.learninganalytics.Student_Tab.StudentTabActivity;
import kosmoglou.antogkou.learninganalytics.StudentsAdapter;

public class fragment_manage_students extends Fragment {
    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("Users");
    //
    private StudentsAdapter adapter;
    //widgets

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_students, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = usersRef.whereEqualTo("usertype", "Student");

        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class)
                .build();

        adapter = new StudentsAdapter(options);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StudentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Student student = documentSnapshot.toObject(Student.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                //TODO open new activity for the specific student
                Intent mIntent = new Intent(getActivity(), StudentTabActivity.class);
                mIntent.putExtra("student_id",id);
                mIntent.putExtra("lastname", student.getLastname());
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
