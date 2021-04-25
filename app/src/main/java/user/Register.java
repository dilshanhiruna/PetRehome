package user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button login_instead,createAccount;
    EditText  password,fname,lname,phone,email;
    ImageView signing_back_btn;
    Spinner district,city;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
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
        signing_back_btn = findViewById(R.id.signing_back_btn);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

            finish();
        }

        signing_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });

        login_instead.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                String mfname = fname.getText().toString().trim();
                String mlname = lname.getText().toString().trim();
                String mphone = phone.getText().toString().trim();
                String mdistrict = district.getSelectedItem().toString();
                String mcity = city.getSelectedItem().toString();

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
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("email",memail);
                            user.put("first_name",mfname);
                            user.put("last_name",mlname);
                            user.put("phone",mphone);
                            user.put("district",mdistrict);
                            user.put("city",mcity);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"user profile is created for "+ userID);
                                }
                            });

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
