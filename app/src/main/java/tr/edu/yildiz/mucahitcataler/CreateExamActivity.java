package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

public class CreateExamActivity extends AppCompatActivity {
    Button createButton;
    MaterialButton shareButton;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    CreateExamAdapter adp;
    ArrayList<Question> questions;
    User currentUser;
    private String currentUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
        defineListeners();
    }

    private void defineVariables() {
        createButton = (Button) findViewById(R.id.createExamButtonCreateExam);
        shareButton = (MaterialButton) findViewById(R.id.shareExamButtonCreateExam);
        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            currentUser = (User) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CreateExamActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewCreateExam);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (currentUser.getQuestions() != null) {
            questions = new ArrayList<>();
            questions = currentUser.getQuestions();
            adp = new CreateExamAdapter(this, questions, currentUsername);
            recyclerView.setAdapter(adp);
        }
        else {
            Toast.makeText(CreateExamActivity.this, "Listelenecek Soru Bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void defineListeners() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Question> selectedQuestions = adp.getSelectedQuestions();
                if (selectedQuestions != null && selectedQuestions.size() > 0) {
                    showExamSettingsDialog();
                } else {
                    Toast.makeText(CreateExamActivity.this, "Lütfen Sınava Eklenecek Soru Seçin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("data/data/tr.edu.yildiz.mucahitcataler/files/exam.txt");
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/*");
                intentShare.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(CreateExamActivity.this, BuildConfig.APPLICATION_ID + ".provider", file));
                startActivity(Intent.createChooser(intentShare, "Paylaş..."));
            }
        });
    }

    private void showExamSettingsDialog() {
        final Dialog dialog = new Dialog(CreateExamActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.create_exam_dialog);

        Button createButton = dialog.findViewById(R.id.createCreateExamDialog);
        Button cancelButton = dialog.findViewById(R.id.cancelCreateExamDialog);
        final SeekBar seekBarDuration = dialog.findViewById(R.id.DurationSeekBarCreateExamDialog);
        final TextView textViewDuration = dialog.findViewById(R.id.DurationTextViewCreateExamDialog);
        final SeekBar seekBarPoint = dialog.findViewById(R.id.PointSeekBarCreateExamDialog);
        final TextView textViewPoint = dialog.findViewById(R.id.PointTextViewCreateExamDialog);
        final RadioButton level2 = dialog.findViewById(R.id.radioButton2CreateExamDialog);
        final RadioButton level3 = dialog.findViewById(R.id.radioButton3CreateExamDialog);
        final RadioButton level4 = dialog.findViewById(R.id.radioButton4CreateExamDialog);
        final RadioButton level5 = dialog.findViewById(R.id.radioButton5CreateExamDialog);
        final CheckBox saveAsDefault = dialog.findViewById(R.id.saveAsDefaultCreateExamDialog);
        seekBarDuration.setMax(150);
        seekBarPoint.setMax(100);

        Context context = getApplicationContext();
        SharedPreferences pref = context.getSharedPreferences(currentUsername, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String duration = pref.getString("duration", "0");
        String point = pref.getString("point", "0");
        String level = pref.getString("level", "none");

        if(level.equals("2")) {
            level2.setChecked(true);
        }else if (level.equals("3")) {
            level3.setChecked(true);
        }else if (level.equals("4")) {
            level4.setChecked(true);
        }else if (level.equals("5")) {
            level5.setChecked(true);
        }
        seekBarDuration.setProgress(Integer.valueOf(duration));
        textViewDuration.setText(duration+" dk");
        seekBarPoint.setProgress(Integer.valueOf(point));
        textViewPoint.setText(point+" p");

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int level = -1;
                if(saveAsDefault.isChecked()) {
                    editor.putString("duration", String.valueOf(seekBarDuration.getProgress()));
                    editor.putString("point", String.valueOf(seekBarPoint.getProgress()));
                    if(level2.isChecked()) {
                        level = 2;
                        editor.putString("level", "2");
                    }else if (level3.isChecked()) {
                        level = 3;
                        editor.putString("level", "3");
                    }else if (level4.isChecked()) {
                        level = 4;
                        editor.putString("level", "4");
                    }else if (level5.isChecked()) {
                        level = 5;
                        editor.putString("level", "5");
                    }
                    editor.commit();
                } else {
                    if(level2.isChecked()) {
                        level = 2;
                    }else if (level3.isChecked()) {
                        level = 3;
                    }else if (level4.isChecked()) {
                        level = 4;
                    }else if (level5.isChecked()) {
                        level = 5;
                    }
                }
                if (level == -1) {
                    Toast.makeText(CreateExamActivity.this, "Lütfen Kullanılabilir Değerler Seçin", Toast.LENGTH_SHORT).show();
                } else {
                    createExam(String.valueOf(seekBarDuration.getProgress()) , String.valueOf(seekBarPoint.getProgress()), level);
                    dialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void createExam(String duration, String point, int level) {
        Random rand = new Random();
        ArrayList<Question> selectedQuestions = adp.getSelectedQuestions();
        ArrayList<String> options = new ArrayList<>();
        options.add("A) ");
        options.add("B) ");
        options.add("C) ");
        options.add("D) ");
        options.add("E) ");
        String metin = "Süre: " + duration + "dk\n\n";
        for (Question aQuestion : selectedQuestions) {
            metin += aQuestion.getQuestion() + " (" + point + "p)" + "\n";

            ArrayList<Integer> questionIds = new ArrayList<Integer>();
            while (questionIds.size() < level) {
                int id = rand.nextInt(5);
                if (!questionIds.contains(id)) {
                    questionIds.add(id);
                }
            }
            if (!questionIds.contains(aQuestion.getCorrectAnswerIndex())) {
                int index = rand.nextInt(level);
                questionIds.remove(index);
                questionIds.add(index, aQuestion.getCorrectAnswerIndex());
            }

            for (int i=0; i<level; i++) {
                if (questionIds.get(i) == aQuestion.getCorrectAnswerIndex()) {
                    metin += options.get(i) + aQuestion.getAnswers().get(questionIds.get(i)) + "***" + "\n";
                } else {
                    metin += options.get(i) + aQuestion.getAnswers().get(questionIds.get(i)) + "\n";
                }
            }
            metin += "\n";

        }
        try {
            FileOutputStream fos = openFileOutput("exam.txt", MODE_PRIVATE);
            fos.write(metin.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createButton.setText("YENİ SINAV OLUŞTUR");
        shareButton.setVisibility(View.VISIBLE);
    }
}