package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExamSettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    SharedPreferences pref;
    Context context;
    SharedPreferences.Editor editor;
    SeekBar seekBarDuration, seekBarPoint;
    TextView textViewDuration, textViewPoint;
    RadioButton level2, level3, level4, level5;
    Button save, cancel;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_settings);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
        getSharedPreferences();
        defineListeners();
    }

    private void defineListeners() {
        seekBarDuration.setOnSeekBarChangeListener(this);
        seekBarPoint.setOnSeekBarChangeListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("duration", String.valueOf(seekBarDuration.getProgress()));
                editor.putString("point", String.valueOf(seekBarPoint.getProgress()));
                if(level2.isChecked()) {
                    editor.putString("level", "2");
                }else if (level3.isChecked()) {
                    editor.putString("level", "3");
                }else if (level4.isChecked()) {
                    editor.putString("level", "4");
                }else if (level5.isChecked()) {
                    editor.putString("level", "5");
                }
                editor.commit();

                Toast.makeText(ExamSettingsActivity.this, "Ayarlar Başarıyla Kayıdedildi", Toast.LENGTH_SHORT).show();

                finish();
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
        seekBarDuration = (SeekBar) findViewById(R.id.DurationSeekBarExamSettings);
        textViewDuration = (TextView) findViewById(R.id.DurationTextViewExamSettings);
        seekBarPoint = (SeekBar) findViewById(R.id.PointSeekBarExamSettings);
        textViewPoint = (TextView) findViewById(R.id.PointTextViewExamSettings);
        level2 = (RadioButton) findViewById(R.id.radioButton2ExamSettings);
        level3 = (RadioButton) findViewById(R.id.radioButton3ExamSettings);
        level4 = (RadioButton) findViewById(R.id.radioButton4ExamSettings);
        level5 = (RadioButton) findViewById(R.id.radioButton5ExamSettings);
        save = (Button) findViewById(R.id.saveButtonExamSettings);
        cancel = (Button) findViewById(R.id.cancelButtonExamSettings);
        seekBarDuration.setMax(150);
        seekBarPoint.setMax(100);
    }

    private void getSharedPreferences() {
        context = getApplicationContext();
        pref = context.getSharedPreferences(currentUsername, context.MODE_PRIVATE);
        editor = pref.edit();

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
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.DurationSeekBarExamSettings:
                textViewDuration.setText(String.valueOf(progress)+" dk");
                break;
            case R.id.PointSeekBarExamSettings:
                textViewPoint.setText(String.valueOf(progress)+" p");
                break;
            default:
                break;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}