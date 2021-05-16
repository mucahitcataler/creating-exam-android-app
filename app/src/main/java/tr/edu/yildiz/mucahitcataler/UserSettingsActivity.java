package tr.edu.yildiz.mucahitcataler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

public class UserSettingsActivity extends AppCompatActivity {
    TextInputLayout name, surname, email, pNumber, password, password2;
    ImageView profilePhoto;
    Button saveButton, cancelButton;
    MaterialButton editPP;
    private String currentUsername;
    private User user;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        this.getSupportActionBar().hide();
        currentUsername = getIntent().getStringExtra("username");

        defineVariables();
        defineListeners();

    }

    private void defineVariables() {
        name = (TextInputLayout) findViewById(R.id.editTextTextPersonNameUserSettings);
        surname = (TextInputLayout) findViewById(R.id.editTextTextPersonSurnameUserSettings);
        email = (TextInputLayout) findViewById(R.id.editTextTextEmailAddressUserSettings);
        pNumber = (TextInputLayout) findViewById(R.id.editTextPhoneUserSettings);
        password = (TextInputLayout) findViewById(R.id.editTextTextPasswordUserSettings);
        password2 = (TextInputLayout) findViewById(R.id.editTextTextPasswordUserSettings2);
        saveButton = (Button) findViewById(R.id.saveButtonUserSettings);
        cancelButton = (Button) findViewById(R.id.cancelButtonUserSettings);
        profilePhoto = (ImageView) findViewById(R.id.profilePhotoUserSettings);
        editPP = (MaterialButton) findViewById(R.id.editPPButtonUserSettings);

        try {
            FileInputStream fis = openFileInput(currentUsername);
            ObjectInputStream is = new ObjectInputStream(fis);
            user = (User) is.readObject();
            is.close();
            fis.close();
            FileInputStream fisPic = openFileInput(currentUsername + "_.png");
            Bitmap pPBit = BitmapFactory.decodeStream(fisPic);
            profilePhoto.setImageBitmap(pPBit);
            fisPic.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(UserSettingsActivity.this, "Beklenmedik Bir Hata Oluştu", Toast.LENGTH_SHORT).show();
            finish();
        }
        name.getEditText().setText(user.getName());
        surname.getEditText().setText(user.getSurname());
        email.getEditText().setText(user.getEmail());
        pNumber.getEditText().setText(user.getPhoneNumber());
        email.setEnabled(false);
    }

    private void defineListeners() {
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateName() | !validateSurname() | !validatePNumber() | !validatePassword() | !validatePasswordMatch()) {
                    return;
                }
                else {
                    Bitmap pP = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
                    user.setName(name.getEditText().getText().toString());
                    user.setSurname(surname.getEditText().getText().toString());
                    user.setPhoneNumber(pNumber.getEditText().getText().toString());
                    if(!password.getEditText().getText().toString().isEmpty()) {
                        user.setPassword(password.getEditText().getText().toString());
                    }
                    try {
                        FileOutputStream fos = openFileOutput(currentUsername, Context.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(user);
                        os.close();
                        fos.close();
                        FileOutputStream fosPic = openFileOutput(currentUsername + "_.png", Context.MODE_PRIVATE);
                        pP.compress(Bitmap.CompressFormat.PNG, 100, fosPic);
                        fosPic.close();
                        Toast.makeText(UserSettingsActivity.this, "Ayarlar Başarıyla Kayıdedildi", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        Toast.makeText(UserSettingsActivity.this, "Beklenemdik Bir Hata Oluştu. Lütfen Tekrar Deneyin..", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(v.getContext(), MenuActivity.class);
                    intent.putExtra("username", currentUsername);
                    startActivity(intent);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    Toast.makeText(UserSettingsActivity.this, "İzin Reddedildi", Toast.LENGTH_SHORT).show();
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
            password.setError(null);
            return true;
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
        if(!val.equals(val2)) {
            password2.setError("Şifreler Eşleşmedi");
            return false;
        }
        else{
            password2.setError(null);
            return true;
        }
    }
}