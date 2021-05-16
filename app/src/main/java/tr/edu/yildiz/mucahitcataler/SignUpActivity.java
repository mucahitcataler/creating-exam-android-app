package tr.edu.yildiz.mucahitcataler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout name, surname, email, pNumber, password, password2;
    ImageView profilePhoto;
    Button signUpButton;
    MaterialButton editPP;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        defineVariables();
        defineListeners();

    }

    private void defineListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateSurname() | !validateEmail() | !validatePNumber() | !validatePassword() | !validatePasswordMatch()) {
                    return;
                }
                else{
                    Bitmap pP = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
                    User user = new User(name.getEditText().getText().toString(), surname.getEditText().getText().toString(), email.getEditText().getText().toString(),
                            pNumber.getEditText().getText().toString(), password.getEditText().getText().toString());
                    String username = email.getEditText().getText().toString().split("@")[0];
                    try {
                        FileOutputStream fos = openFileOutput(username, Context.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(user);
                        os.close();
                        fos.close();
                        FileOutputStream fosPic = openFileOutput(username + "_.png", Context.MODE_PRIVATE);
                        pP.compress(Bitmap.CompressFormat.PNG, 100, fosPic);
                        fosPic.close();
                        Toast.makeText(SignUpActivity.this, "Başarıyla Kayıt Oldunuz", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Kullanıcı Kaydedilemedi", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(v.getContext(), MenuActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

        editPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE} ;
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "İzin Reddedildi", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            profilePhoto.setImageURI(data.getData());
        }
    }

    private void defineVariables() {
        name = (TextInputLayout) findViewById(R.id.editTextTextPersonNameSignUp);
        surname = (TextInputLayout) findViewById(R.id.editTextTextPersonSurnameSignUp);
        email = (TextInputLayout) findViewById(R.id.editTextTextEmailAddressSignUp);
        pNumber = (TextInputLayout) findViewById(R.id.editTextPhoneSignUp);
        password = (TextInputLayout) findViewById(R.id.editTextTextPasswordSignUp);
        password2 = (TextInputLayout) findViewById(R.id.editTextTextPasswordSignUp2);
        signUpButton = (Button) findViewById(R.id.signUpButtonSignUp);
        profilePhoto = (ImageView) findViewById(R.id.profilePhotoSignUp);
        editPP = (MaterialButton) findViewById(R.id.editPPButtonSignUp);
    }

    private Boolean validateName() {
        String val = name.getEditText().getText().toString();
        if(val.isEmpty()) {
            name.setError("Gerekli");
            return false;
        }
        else{
            name.setError(null);
            return true;
        }
    }

    private Boolean validateSurname() {
        String val = surname.getEditText().getText().toString();
        if(val.isEmpty()) {
            surname.setError("Gerekli");
            return false;
        }
        else{
            surname.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        String username = val.split("@")[0];
        if(val.isEmpty()) {
            email.setError("Gerekli");
            return false;
        }
        else if(!pattern.matcher(val).matches()){
            email.setError("Geçersiz E-mail Adresi");
            return  false;
        }
        else {
            try {
                FileInputStream fis = openFileInput(username);
                fis.close();
                email.setError("E-mail adresi kullanılıyor.");
                return false;
            }
            catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
                email.setError(null);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SignUpActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }
        }
    }

    private Boolean validatePNumber() {
        String val = pNumber.getEditText().getText().toString();
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        if(val.isEmpty()) {
            pNumber.setError("Gerekli");
            return false;
        }
        else if(!pattern.matcher(val).matches()){
            pNumber.setError("Geçersiz Telefon Numarası");
            return false;
        }
        else{
            pNumber.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String regex = "^.{6,}$";
        Pattern pattern = Pattern.compile(regex);
        if(val.isEmpty()) {
            password.setError("Gerekli");
            return false;
        }
        else if(!pattern.matcher(val).matches()){
            password.setError("Şifre en az 6 karakter içermeli");
            return  false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    private Boolean validatePasswordMatch() {
        String val = password.getEditText().getText().toString();
        String val2 = password2.getEditText().getText().toString();
        if(val2.isEmpty()) {
            password2.setError("Gerekli");
            return false;
        }
        else if(!val.equals(val2)) {
            password2.setError("Şifreler Eşleşmedi");
            return false;
        }
        else{
            password2.setError(null);
            return true;
        }
    }

}