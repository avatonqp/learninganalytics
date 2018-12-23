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
import kosmoglou.antogkou.learninganalytics.Classes.Classroom;

public class ClassroomAdapter extends FirestoreRecyclerAdapter<Classroom, ClassroomAdapter.ClassroomHolder> {
  private OnItemClickListener listener;

  public ClassroomAdapter(@NonNull FirestoreRecyclerOptions<Classroom> options) {
    super(options);
  }

  @Override
  protected void onBindViewHolder(@NonNull ClassroomHolder holder, int position, @NonNull Classroom model) {
    holder.textViewClassname.setText(model.getClassname());
    holder.textViewDescription.setText(model.getDescription());
  }

  @NonNull
  @Override
  public ClassroomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.classroom_item,
            parent, false);
    return new ClassroomHolder(v);
  }

  class ClassroomHolder extends RecyclerView.ViewHolder{
    //widgets
    TextView textViewClassname;
    TextView textViewDescription;

    public ClassroomHolder(View itemView){
      super(itemView);
      textViewClassname = itemView.findViewById(R.id.text_view_classname);
      textViewDescription = itemView.findViewById(R.id.text_view_description);

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
