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
import kosmoglou.antogkou.learninganalytics.Classes.Quiz;

public class QuizesAdapter extends FirestoreRecyclerAdapter<Quiz, QuizesAdapter.QuizesHolder> {
  private QuizesAdapter.OnItemClickListener listener;

  public QuizesAdapter(@NonNull FirestoreRecyclerOptions<Quiz> options) {
    super(options);
  }

  @Override
  protected void onBindViewHolder(@NonNull QuizesAdapter.QuizesHolder holder, int position, @NonNull Quiz quiz) {
    holder.tvQuizName.setText(quiz.getName());
    holder.tvTotalQuestions.setText("Questions: " + quiz.getTotalQuestions());
    holder.tvDuration.setText("Duration: " + quiz.getDuration());
  }

  @NonNull
  @Override
  public QuizesAdapter.QuizesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item,
            parent, false);
    return new QuizesAdapter.QuizesHolder(v);
  }

  class QuizesHolder extends RecyclerView.ViewHolder {
    //widgets
    TextView tvQuizName;
    TextView tvTotalQuestions;
    TextView tvDuration;

    public QuizesHolder(View itemView){
      super(itemView);
      tvQuizName = itemView.findViewById(R.id.tvQuizName);
      tvTotalQuestions = itemView.findViewById(R.id.tvTotalQuestions);
      tvDuration = itemView.findViewById(R.id.tvDuration);

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

  public void setOnItemClickListener(QuizesAdapter.OnItemClickListener listener) {
    this.listener = listener;
  }
}
