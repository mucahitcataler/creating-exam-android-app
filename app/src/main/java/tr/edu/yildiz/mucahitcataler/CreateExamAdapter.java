package tr.edu.yildiz.mucahitcataler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CreateExamAdapter extends RecyclerView.Adapter<CreateExamAdapter.MyViewHolder>{
    Context context;
    ArrayList<Question> questions;
    private String currentUsername;
    private ArrayList<Question> selectedQuestions;

    public CreateExamAdapter(Context context, ArrayList<Question> questions, String currentUsername) {
        this.context = context;
        this.questions = questions;
        this.currentUsername = currentUsername;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question, answerA, answerB, answerC, answerD, answerE;
        ToggleButton add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.questionTextCreateExamCard);
            answerA = itemView.findViewById(R.id.answerACreateExamCard);
            answerB = itemView.findViewById(R.id.answerBCreateExamCard);
            answerC = itemView.findViewById(R.id.answerCCreateExamCard);
            answerD = itemView.findViewById(R.id.answerDCreateExamCard);
            answerE = itemView.findViewById(R.id.answerECreateExamCard);
            add = itemView.findViewById(R.id.addCreateExamCard);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_exam_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreateExamAdapter.MyViewHolder holder, int position) {
        holder.question.setText(questions.get(position).getQuestion());
        holder.answerA.setText(questions.get(position).getAnswers().get(0));
        holder.answerB.setText(questions.get(position).getAnswers().get(1));
        holder.answerC.setText(questions.get(position).getAnswers().get(2));
        holder.answerD.setText(questions.get(position).getAnswers().get(3));
        holder.answerE.setText(questions.get(position).getAnswers().get(4));
        if(questions.get(position).getCorrectAnswerIndex() == 0) {
            holder.answerA.setTextColor(Color.parseColor("#FF0000"));
        } else if(questions.get(position).getCorrectAnswerIndex() == 1) {
            holder.answerB.setTextColor(Color.parseColor("#FF0000"));
        } else if(questions.get(position).getCorrectAnswerIndex() == 2) {
            holder.answerC.setTextColor(Color.parseColor("#FF0000"));
        } else if(questions.get(position).getCorrectAnswerIndex() == 3) {
            holder.answerD.setTextColor(Color.parseColor("#FF0000"));
        } else if(questions.get(position).getCorrectAnswerIndex() == 4) {
            holder.answerE.setTextColor(Color.parseColor("#FF0000"));
        }
        selectedQuestions = new ArrayList<>();
        holder.add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedQuestions.add(questions.get(position));
                }else {
                    selectedQuestions.remove(questions.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public ArrayList<Question> getSelectedQuestions() {
        return selectedQuestions;
    }

}
