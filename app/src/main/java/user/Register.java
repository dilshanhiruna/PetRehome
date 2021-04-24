package user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;


public class Register extends AppCompatActivity {
    Button login_instead,createAccount;
    EditText  password,fname,lname,phone,email;
    Spinner district,city;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login_instead = findViewById(R.id.login_instead);
        password = findViewById(R.id.reg_password_txt);
        fname = findViewById(R.id.reg_fname_txt);
        lname = findViewById(R.id.reg_lname_txt);
        phone = findViewById(R.id.reg_phone_txt);
        email = findViewById(R.id.reg_email_txt);
        district = findViewById(R.id.district_spinner);
        city = findViewById(R.id.city_spinner);
        createAccount = findViewById(R.id.createAccountBtn);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        login_instead.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(mpassword)){
                    password.setError("Password is required");
                    return;
                }

                if(mpassword.length() < 5){
                    password.setError("Password must greater than 4 characters");
                }
                fAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



    }

}
