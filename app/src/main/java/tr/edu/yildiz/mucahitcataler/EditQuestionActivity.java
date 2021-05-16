package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EditQuestionActivity extends AppCompatActivity implements View.OnClickListener{
    User currentUser;
    Question currentQuestion;
    RadioButton answerButtonA, answerButtonB, answerButtonC, answerButtonD, answerButtonE;
    Button save, cancel;
    EditText question, answerA, answerB, answerC, answerD, answerE;
    int correctAnswer;
    private String currentUsername;
    private Integer questionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");
        questionIndex = Integer.valueOf(getIntent().getStringExtra("questionIndex"));

        defineVariables();
        defineListeners();
        question.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private void defineListeners() {
        answerButtonA.setOnClickListener(this);
        answerButtonB.setOnClickListener(this);
        answerButtonC.setOnClickListener(this);
        answerButtonD.setOnClickListener(this);
        answerButtonE.setOnClickListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> answers = new ArrayList<String>();
                answers.add(answerA.getText().toString());
                answers.add(answerB.getText().toString());
                answers.add(answerC.getText().toString());
                answers.add(answerD.getText().toString());
                answers.add(answerE.getText().toString());
                if(answerButtonA.isChecked()) {
                    correctAnswer = 0;
                } else if(answerButtonB.isChecked()) {
                    correctAnswer = 1;
                } else if(answerButtonC.isChecked()) {
                    correctAnswer = 2;
                } else if(answerButtonD.isChecked()) {
                    correctAnswer = 3;
                } else if(answerButtonE.isChecked()) {
                    correctAnswer = 4;
                }
                currentUser.getQuestions().get(questionIndex).setQuestion(question.getText().toString());
                currentUser.getQuestions().get(questionIndex).setAnswers(answers);
                currentUser.getQuestions().get(questionIndex).setCorrectAnswerIndex(correctAnswer);
                try {
                    FileOutputStream fos = openFileOutput(currentUsername, Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(currentUser);
                    os.close();
                    fos.close();

                    Toast.makeText(EditQuestionActivity.this, "Soru Başarıyla Kayıdedildi", Toast.LENGTH_SHORT).show();

                    finish();
                }
                catch (Exception e) {
                    Toast.makeText(EditQuestionActivity.this, "Beklenemdik Bir Hata Oluştu. Lütfen Tekrar Deneyin..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void defineVariables() {
        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            currentUser = (User) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(EditQuestionActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
        currentQuestion = currentUser.getQuestions().get(questionIndex);
        answerButtonA = (RadioButton) findViewById(R.id.radioButtonAEditQuestion);
        answerButtonB = (RadioButton) findViewById(R.id.radioButtonBEditQuestion);
        answerButtonC = (RadioButton) findViewById(R.id.radioButtonCEditQuestion);
        answerButtonD = (RadioButton) findViewById(R.id.radioButtonDEditQuestion);
        answerButtonE = (RadioButton) findViewById(R.id.radioButtonEEditQuestion);
        question = (EditText) findViewById(R.id.editTextQuestionEditQuestion);
        answerA = (EditText) findViewById(R.id.editTextAEditQuestion);
        answerB = (EditText) findViewById(R.id.editTextBEditQuestion);
        answerC = (EditText) findViewById(R.id.editTextCEditQuestion);
        answerD = (EditText) findViewById(R.id.editTextDEditQuestion);
        answerE = (EditText) findViewById(R.id.editTextEEditQuestion);
        save = (Button) findViewById(R.id.saveButtonEditQuestion);
        cancel = (Button) findViewById(R.id.cancelButtonEditQuestion);
        correctAnswer = currentQuestion.getCorrectAnswerIndex();

        question.setText(currentQuestion.getQuestion());
        answerA.setText(currentQuestion.getAnswers().get(0));
        answerB.setText(currentQuestion.getAnswers().get(1));
        answerC.setText(currentQuestion.getAnswers().get(2));
        answerD.setText(currentQuestion.getAnswers().get(3));
        answerE.setText(currentQuestion.getAnswers().get(4));
        if(correctAnswer == 0) {
            answerButtonA.setChecked(true);
            answerButtonB.setChecked(false);
            answerButtonC.setChecked(false);
            answerButtonD.setChecked(false);
            answerButtonE.setChecked(false);
        } else if(correctAnswer == 1) {
            answerButtonA.setChecked(false);
            answerButtonB.setChecked(true);
            answerButtonC.setChecked(false);
            answerButtonD.setChecked(false);
            answerButtonE.setChecked(false);
        } else if(correctAnswer == 2) {
            answerButtonA.setChecked(false);
            answerButtonB.setChecked(false);
            answerButtonC.setChecked(true);
            answerButtonD.setChecked(false);
            answerButtonE.setChecked(false);
        } else if(correctAnswer == 3) {
            answerButtonA.setChecked(false);
            answerButtonB.setChecked(false);
            answerButtonC.setChecked(false);
            answerButtonD.setChecked(true);
            answerButtonE.setChecked(false);
        } else if(correctAnswer == 4) {
            answerButtonA.setChecked(false);
            answerButtonB.setChecked(false);
            answerButtonC.setChecked(false);
            answerButtonD.setChecked(false);
            answerButtonE.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButtonAEditQuestion:
                answerButtonA.setChecked(true);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 0;
                break;
            case R.id.radioButtonBEditQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(true);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 1;
                break;
            case R.id.radioButtonCEditQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(true);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 2;
                break;
            case R.id.radioButtonDEditQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(true);
                answerButtonE.setChecked(false);
                correctAnswer = 3;
                break;
            case R.id.radioButtonEEditQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(true);
                correctAnswer = 4;
                break;
            default:
                break;
        }
    }
}