package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener{
    RadioButton answerButtonA, answerButtonB, answerButtonC, answerButtonD, answerButtonE;
    Button save, cancel;
    EditText questionText, answerA, answerB, answerC, answerD, answerE;
    int correctAnswer;
    private String currentUsername;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
        cleanEverything();
        defineListeners();
        questionText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private void defineVariables() {
        answerButtonA = (RadioButton) findViewById(R.id.radioButtonAAddQuestion);
        answerButtonB = (RadioButton) findViewById(R.id.radioButtonBAddQuestion);
        answerButtonC = (RadioButton) findViewById(R.id.radioButtonCAddQuestion);
        answerButtonD = (RadioButton) findViewById(R.id.radioButtonDAddQuestion);
        answerButtonE = (RadioButton) findViewById(R.id.radioButtonEAddQuestion);
        questionText = (EditText) findViewById(R.id.editTextQuestionAddQuestion);
        answerA = (EditText) findViewById(R.id.editTextAAddQuestion);
        answerB = (EditText) findViewById(R.id.editTextBAddQuestion);
        answerC = (EditText) findViewById(R.id.editTextCAddQuestion);
        answerD = (EditText) findViewById(R.id.editTextDAddQuestion);
        answerE = (EditText) findViewById(R.id.editTextEAddQuestion);
        save = (Button) findViewById(R.id.saveButtonAddQuestion);
        cancel = (Button) findViewById(R.id.cancelButtonAddQuestion);
        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            user = (User) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddQuestionActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
        correctAnswer = -1;
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
                if(correctAnswer == -1) {
                    Toast.makeText(AddQuestionActivity.this, "Lütfen Doğru Cevabı Seçin.", Toast.LENGTH_SHORT).show();
                }
                else {
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
                    ArrayList<Question> questions = new ArrayList<>();
                    if (user.getQuestions() != null) {
                        questions = user.getQuestions();
                    }
                    Question question = new Question(questionText.getText().toString(), answers, correctAnswer);
                    questions.add(question);
                    user.setQuestions(questions);

                    try {
                        FileOutputStream fos = openFileOutput(currentUsername, Context.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(user);
                        os.close();
                        fos.close();

                        Toast.makeText(AddQuestionActivity.this, "Soru Başarıyla Kayıdedildi", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    catch (Exception e) {
                        Toast.makeText(AddQuestionActivity.this, "Beklenemdik Bir Hata Oluştu. Lütfen Tekrar Deneyin..", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


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

    private void cleanEverything() {
        questionText.setText("");
        answerA.setText("");
        answerB.setText("");
        answerC.setText("");
        answerD.setText("");
        answerE.setText("");
        answerButtonA.setChecked(false);
        answerButtonB.setChecked(false);
        answerButtonC.setChecked(false);
        answerButtonD.setChecked(false);
        answerButtonE.setChecked(false);
        correctAnswer = -1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButtonAAddQuestion:
                answerButtonA.setChecked(true);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 0;
                break;
            case R.id.radioButtonBAddQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(true);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 1;
                break;
            case R.id.radioButtonCAddQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(true);
                answerButtonD.setChecked(false);
                answerButtonE.setChecked(false);
                correctAnswer = 2;
                break;
            case R.id.radioButtonDAddQuestion:
                answerButtonA.setChecked(false);
                answerButtonB.setChecked(false);
                answerButtonC.setChecked(false);
                answerButtonD.setChecked(true);
                answerButtonE.setChecked(false);
                correctAnswer = 3;
                break;
            case R.id.radioButtonEAddQuestion:
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