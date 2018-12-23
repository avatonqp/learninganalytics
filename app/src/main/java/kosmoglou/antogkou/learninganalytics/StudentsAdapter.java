package kosmoglou.antogkou.learninganalytics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import kosmoglou.antogkou.learninganalytics.Classes.Student;

public class StudentsAdapter extends FirestoreRecyclerAdapter<Student, StudentsAdapter.StudentsHolder> {
    private OnItemClickListener listener;

    public StudentsAdapter(@NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentsHolder holder, int position, @NonNull Student student) {
        holder.textViewFullname.setText(student.getFullname());
        holder.textViewUsername.setText(student.getUsername());
        holder.textViewUsertype.setText(student.getUsertype());
    }

    @NonNull
    @Override
    public StudentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,
                parent, false);
        return new StudentsHolder(v);
    }

    class StudentsHolder extends RecyclerView.ViewHolder {
        //widgets
        TextView textViewFullname;
        TextView textViewUsername;
        TextView textViewUsertype;

        public StudentsHolder(View itemView){
            super(itemView);
            textViewFullname = itemView.findViewById(R.id.text_view_fullname);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
            textViewUsertype = itemView.findViewById(R.id.text_view_usertype);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
