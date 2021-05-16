package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ListQuestionsActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ListQuestionsAdapter adp;
    ArrayList<Question> questions;
    User currentUser;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_questions);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        defineVariables();
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
            Toast.makeText(ListQuestionsActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewListQuestions);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (currentUser.getQuestions() != null) {
            questions = new ArrayList<>();
            questions = currentUser.getQuestions();
            adp = new ListQuestionsAdapter(this, questions, currentUsername);
            recyclerView.setAdapter(adp);
        }
        else {
            Toast.makeText(ListQuestionsActivity.this, "Listelenecek Soru Bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

}