package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    TextView userText;
    ImageView image;
    CardView addQuestion, listQuestions, examSettings, createExam, userSettings, logout;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
        defineListeners();

        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            is.close();
            fis.close();
            userText.setText("Hoşgeldin "+user.getName()+" "+user.getSurname());
            FileInputStream fisPic = openFileInput(currentUsername + "_.png");
            Bitmap pPBit = BitmapFactory.decodeStream(fisPic);
            image.setImageBitmap(pPBit);
            fisPic.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MenuActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            is.close();
            fis.close();
            userText.setText("Hoşgeldin "+user.getName()+" "+user.getSurname());
            FileInputStream fisPic = openFileInput(currentUsername + "_.png");
            Bitmap pPBit = BitmapFactory.decodeStream(fisPic);
            image.setImageBitmap(pPBit);
            fisPic.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MenuActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void defineVariables() {
        image = (ImageView) findViewById(R.id.profilePhotoMenu);
        userText = (TextView) findViewById(R.id.welcomeUserMenu);
        addQuestion = (CardView) findViewById(R.id.addQuestionMenu);
        listQuestions = (CardView) findViewById(R.id.listQuestionsMenu);
        examSettings = (CardView) findViewById(R.id.examSettingsMenu);
        createExam = (CardView) findViewById(R.id.createExamMenu);
        userSettings = (CardView) findViewById(R.id.userSettingsMenu);
        logout = (CardView) findViewById(R.id.logoutMenu);
    }

    private void defineListeners() {
        addQuestion.setOnClickListener(this);
        listQuestions.setOnClickListener(this);
        examSettings.setOnClickListener(this);
        createExam.setOnClickListener(this);
        userSettings.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.addQuestionMenu :
                intent = new Intent(v.getContext(), AddQuestionActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
                break;
            case R.id.listQuestionsMenu :
                intent = new Intent(v.getContext(), ListQuestionsActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
                break;
            case R.id.examSettingsMenu :
                intent = new Intent(v.getContext(), ExamSettingsActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
                break;
            case R.id.createExamMenu :
                intent = new Intent(v.getContext(), CreateExamActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
                break;
            case R.id.userSettingsMenu:
                intent = new Intent(v.getContext(), UserSettingsActivity.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
                break;
            case R.id.logoutMenu :
                finish();
                break;
            default:
                break;
        }
    }
}