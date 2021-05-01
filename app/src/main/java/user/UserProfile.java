package user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.oop.petrehome.MainActivity;
import com.oop.petrehome.R;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    TextView edit_user_email,edit_user_fname,edit_user_lname,edit_user_phone,edit_user_district,edit_user_city;
    Button edit_user_btn,verify_now_btn;
    ImageView user_back_btn,verification_badge;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        edit_user_email =findViewById(R.id.edit_user_email_txt);
        edit_user_fname =findViewById(R.id.edit_user_fname_txt);
        edit_user_lname =findViewById(R.id.edit_user_lname_txt);
        edit_user_phone =findViewById(R.id.edit_user_phone_txt);
        edit_user_district =findViewById(R.id.edit_user_district_txt);
        edit_user_city =findViewById(R.id.edit_user_city_txt);
        edit_user_btn = findViewById(R.id.edit_user_btn);
        user_back_btn = findViewById(R.id.user_back_btn);

        verification_badge = findViewById(R.id.verification_badge);
        verify_now_btn = findViewById(R.id.verify_now_btn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() == null){
            Intent i = new Intent(getApplicationContext(), Login.class).putExtra("from", "profile");
            startActivity(i);
        }
        else{
            userID = fAuth.getCurrentUser().getUid();
            FirebaseUser user = fAuth.getCurrentUser();

            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    edit_user_email.setText(value.getString("email"));
                    edit_user_phone.setText(value.getString("phone"));
                    edit_user_fname.setText(value.getString("first_name"));
                    edit_user_lname.setText(value.getString("last_name"));
                    edit_user_district.setText(value.getString("district"));
                    edit_user_city.setText(value.getString("city"));

                }
            });

            DocumentReference documentReferenceCount = fStore.collection("users").document(userID);
            Map<String,Object> USER = new HashMap<>();
            if (user.isEmailVerified()){
                USER.put("verified","y");
            }
            else {
                USER.put("verified","n");
            }
            documentReferenceCount.update(USER);


            if (user.isEmailVerified()){
                verification_badge.setVisibility(View.VISIBLE);
            }else {
                verify_now_btn.setVisibility(View.VISIBLE);

                verify_now_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
            }



            edit_user_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), EditUserProfile.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });

        }

        user_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });




    }
}