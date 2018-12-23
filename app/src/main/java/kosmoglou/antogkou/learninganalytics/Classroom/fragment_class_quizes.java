package kosmoglou.antogkou.learninganalytics.Classroom;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import kosmoglou.antogkou.learninganalytics.Classes.Quiz;
import kosmoglou.antogkou.learninganalytics.Classes.Student;
import kosmoglou.antogkou.learninganalytics.NewClassroomActivity;
import kosmoglou.antogkou.learninganalytics.NewQuizActivity;
import kosmoglou.antogkou.learninganalytics.QuizActivity;
import kosmoglou.antogkou.learninganalytics.QuizesAdapter;
import kosmoglou.antogkou.learninganalytics.R;
import kosmoglou.antogkou.learninganalytics.Student_Tab.StudentTabActivity;
import kosmoglou.antogkou.learninganalytics.StudentsAdapter;

public class fragment_class_quizes extends Fragment {
    // widgets
    public FloatingActionButton buttonAddQuiz;
    // vars
    public String currentUserID = FirebaseAuth.getInstance().getUid();
    Boolean isAdmin = false;
    // firestore
    public DocumentReference currentUserPath = FirebaseFirestore.getInstance().collection("Users").document(currentUserID);
    public CollectionReference quizes;
    //
    public QuizesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_quizes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init widgets
        buttonAddQuiz = getView().findViewById(R.id.buttonAddQuiz);
        isAdmin = getArguments().getBoolean("isAdmin");
        String classroom_id = getArguments().getString("classroom_id");
        quizes = FirebaseFirestore.getInstance().collection("Classes").document(classroom_id).collection("quizes");
        if(isAdmin){
            buttonAddQuiz.setVisibility(View.VISIBLE); // widget listeners
            buttonAddQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newQuizIntent = new Intent(getActivity(), NewQuizActivity.class);
                    newQuizIntent.putExtra("classroom_id", classroom_id);
                    startActivity(newQuizIntent);
                }
            });
        }else if(!isAdmin){
            buttonAddQuiz.setVisibility(View.GONE);
        }
        setupRecyclerView();
    }
    private void setupRecyclerView(){
        Query query = quizes;

        FirestoreRecyclerOptions<Quiz> options = new FirestoreRecyclerOptions.Builder<Quiz>()
                .setQuery(query, Quiz.class)
                .build();

        adapter = new QuizesAdapter(options);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewQuizes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new QuizesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Quiz quiz = documentSnapshot.toObject(Quiz.class);
                if(!isAdmin) {
                    String id = documentSnapshot.getId();
                    Intent quizIntent = new Intent(getActivity(), QuizActivity.class);
                    quizIntent.putExtra("classroom_id", getArguments().getString("classroom_id"));
                    quizIntent.putExtra("quiz_id", id);
                    startActivity(quizIntent);
                }
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