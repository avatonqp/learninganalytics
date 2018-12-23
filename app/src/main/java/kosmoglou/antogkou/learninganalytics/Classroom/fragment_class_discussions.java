package kosmoglou.antogkou.learninganalytics.Classroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import kosmoglou.antogkou.learninganalytics.Forum.CommentsActivity;
import kosmoglou.antogkou.learninganalytics.Forum.PostEditActivity;
import kosmoglou.antogkou.learninganalytics.Forum.PostsActivity;
import kosmoglou.antogkou.learninganalytics.Models.ForumModel;
import kosmoglou.antogkou.learninganalytics.R;

public class fragment_class_discussions extends Fragment {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.forumList)
    RecyclerView forumList;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirestoreRecyclerAdapter adapter;
    public CollectionReference forumPath;
    public LinearLayoutManager linearLayoutManager;
    public FloatingActionButton floatingAdd;
    public String currentUserID = mAuth.getUid();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_discussions, container, false);
        ButterKnife.bind(this, view);
        floatingAdd =  view.findViewById(R.id.floatingAdd);

        init();
        getForumList();

        return view;
    }

    private void init(){
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postsIntent = new Intent(getContext(), PostsActivity.class);
                postsIntent.putExtra("classroom_id", getArguments().getString("classroom_id"));
                startActivity(postsIntent);
            }
        });
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        forumList.setLayoutManager(linearLayoutManager);
    }

    private void getForumList(){
        String classroom_id = getArguments().getString("classroom_id"); // gets the classroom id from parent activity to child fragment
        forumPath = db.collection("Classes").document(classroom_id).collection("Posts");

        Query query = forumPath
                .orderBy("postdate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ForumModel> response = new FirestoreRecyclerOptions.Builder<ForumModel>()
                .setQuery(query, ForumModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ForumModel, ForumHolder>(response) {
            @Override
            public void onBindViewHolder(ForumHolder holder, int position, ForumModel model) {
                final ForumModel final_model = model;
                progressBar.setVisibility(View.GONE);
                holder.textTitle.setText(model.getTitle());
                holder.textDescription.setText(model.getDescription());
                //holder.textPostDate.setText(model.getDate());

                Date date = model.getPostdate();
                if (date != null) {
                    DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                    String creationDate = dateFormat.format(date);
                    holder.textPostDate.setText(creationDate);
                    Log.d("TAG", creationDate);
                }

                holder.itemView.setOnClickListener(v -> {
                    //added code to move strings to new activity
                    Intent commentsIntent = new Intent(getContext(), CommentsActivity.class);
                    commentsIntent.putExtra("textTitle", model.getTitle());
                    commentsIntent.putExtra("textDescription", model.getDescription());

                    if (date != null) {
                        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                        String creationDate = dateFormat.format(date);
                        commentsIntent.putExtra("textPostDate", creationDate);
                    }

                    commentsIntent.putExtra("textDocumentId", model.getDocumentId());
                    commentsIntent.putExtra("textUserID", model.getCurrentUserID());
                    commentsIntent.putExtra("textCreator_UserID", model.getCreator_userid());
                    commentsIntent.putExtra("classroom_id", getArguments().getString("classroom_id"));
                    startActivity(commentsIntent);

                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //allow only the user that created the post to edit it
                        if(currentUserID.equals(final_model.getCreator_userid())) {
                            //carry strings to editPostActivity
                            Intent PostEditIntent = new Intent(getContext(), PostEditActivity.class);
                            PostEditIntent.putExtra("textTitle", final_model.getTitle());
                            PostEditIntent.putExtra("textDescription", final_model.getDescription());
                            PostEditIntent.putExtra("textDocumentId", final_model.getDocumentId());
                            PostEditIntent.putExtra("textCreator_UserID", final_model.getCreator_userid());
                            PostEditIntent.putExtra("classroom_id", getArguments().getString("classroom_id"));
                            startActivity(PostEditIntent);
                        }

                        return true;
                    }
                });


            }
            @Override
            public ForumHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_forumitems, group, false);

                return new ForumHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        forumList.setAdapter(adapter);
    }

    public class ForumHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textTitle)
        TextView textTitle;
        @BindView(R.id.textDescription)
        TextView textDescription;
        @BindView(R.id.textPostDate)
        TextView textPostDate;

        public ForumHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
