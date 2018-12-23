package kosmoglou.antogkou.learninganalytics.Student_Panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import kosmoglou.antogkou.learninganalytics.Classes.Classroom;
import kosmoglou.antogkou.learninganalytics.Classroom.StudentClassroomActivity;
import kosmoglou.antogkou.learninganalytics.Classroom.TeacherClassroomActivity;
import kosmoglou.antogkou.learninganalytics.ClassroomAdapter;
import kosmoglou.antogkou.learninganalytics.NewClassroomActivity;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_my_classes extends Fragment {
    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserID = FirebaseAuth.getInstance().getUid();
    private CollectionReference enrolledClassesRef = db.collection("Users").document(currentUserID).collection("enrolled_classes");
    //
    private ClassroomAdapter adapter;
    //widgets


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = enrolledClassesRef;

        FirestoreRecyclerOptions<Classroom> options = new FirestoreRecyclerOptions.Builder<Classroom>()
                .setQuery(query, Classroom.class)
                .build();

        adapter = new ClassroomAdapter(options);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ClassroomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Classroom classroom = documentSnapshot.toObject(Classroom.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Intent mIntent = new Intent(getActivity(), StudentClassroomActivity.class);
                mIntent.putExtra("classroom_id",id);
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
