package user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oop.petrehome.R;

import java.util.ArrayList;
import java.util.List;


public class Register extends AppCompatActivity {
    Button login_instead_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login_instead_btn = findViewById(R.id.login_instead_btn);

        login_instead_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);

            }
        });




    }

}