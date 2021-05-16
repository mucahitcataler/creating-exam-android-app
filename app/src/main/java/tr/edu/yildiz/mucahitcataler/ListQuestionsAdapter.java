package tr.edu.yildiz.mucahitcataler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ListQuestionsAdapter extends RecyclerView.Adapter<ListQuestionsAdapter.MyViewHolder>{

    Context context;
    ArrayList<Question> questions;
    private String currentUsername;
    private User user;

    public ListQuestionsAdapter(Context context, ArrayList<Question> questions, String currentUsername) {
        this.context = context;
        this.questions = questions;
        this.currentUsername = currentUsername;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question, answerA, answerB, answerC, answerD, answerE;
        Button edit, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.questionTextListQuestionsCard);
            answerA = itemView.findViewById(R.id.answerAListQuestionsCard);
            answerB = itemView.findViewById(R.id.answerBListQuestionsCard);
            answerC = itemView.findViewById(R.id.answerCListQuestionsCard);
            answerD = itemView.findViewById(R.id.answerDListQuestionsCard);
            answerE = itemView.findViewById(R.id.answerEListQuestionsCard);
            edit = itemView.findViewById(R.id.editListQuestionsCard);
            delete = itemView.findViewById(R.id.deleteListQuestionsCard);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_questions_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListQuestionsAdapter.MyViewHolder holder, int position) {
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
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditQuestionActivity.class);
                intent.putExtra("questionIndex", String.valueOf(position));
                intent.putExtra("username", currentUsername);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Sorunun silinmesini onaylıyor musunuz?")
                        .setNegativeButton("İptal", null)
                        .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                questions.remove(position);
                                try {
                                    FileInputStream fis = context.openFileInput(currentUsername);
                                    ObjectInputStream is = new ObjectInputStream(fis);
                                    user = (User) is.readObject();
                                    is.close();
                                    fis.close();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Beklenemdik Bir Hata Oluştu. Lütfen Tekrar Deneyin..", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                user.setQuestions(questions);
                                try {
                                    FileOutputStream fos = context.openFileOutput(currentUsername, Context.MODE_PRIVATE);
                                    ObjectOutputStream os = new ObjectOutputStream(fos);
                                    os.writeObject(user);
                                    os.close();
                                    fos.close();
                                    Toast.makeText(context, "Soru Silindi", Toast.LENGTH_SHORT).show();
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount() - position);
                                }
                                catch (Exception e) {
                                    Toast.makeText(context, "Beklenemdik Bir Hata Oluştu. Lütfen Tekrar Deneyin..", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
