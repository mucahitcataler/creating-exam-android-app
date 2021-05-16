package tr.edu.yildiz.mucahitcataler;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity {
    TextInputLayout email;
    TextInputLayout password;
    Button loginButton;
    Button signUpButton;
    int attempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        defineVariables();
        defineListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginButton.setEnabled(true);
    }

    private void defineVariables() {
        loginButton = (Button) findViewById(R.id.loginButtonLogin);
        signUpButton = (Button) findViewById(R.id.signUpButtonLogin);
        email = (TextInputLayout) findViewById(R.id.editTextTextEmailAddressLogin);
        password = (TextInputLayout) findViewById(R.id.editTextTextPasswordLogin);
        attempt = 0;
    }

    private void defineListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(checkUser()) {
                    intent = new Intent(v.getContext(), MenuActivity.class);
                    intent.putExtra("username", email.getEditText().getText().toString().split("@")[0]);
                    cleanTexts();
                    startActivity(intent);
                }
                else{
                    attempt += 1;
                    if(attempt >= 3) {
                        Toast.makeText(MainActivity.this, "3 Yanlış Giriş Yapıldı. Lütfen Üye Olun.", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(false);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "E-mail veya Şifre Hatalı", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), SignUpActivity.class);
                cleanTexts();
                startActivity(intent);
            }
        });
    }

    private boolean checkUser() {
        String username = email.getEditText().getText().toString().split("@")[0];
        try {
            FileInputStream fis = openFileInput(username);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            is.close();
            fis.close();
            if(email.getEditText().getText().toString().equals(user.getEmail()) && user.checkPassword(password.getEditText().getText().toString())){
                return true;
            }
            else{
                return false;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
    }

    private void cleanTexts() {
        email.getEditText().setText("");
        password.getEditText().setText("");
    }
}