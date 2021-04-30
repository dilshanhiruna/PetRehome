package PostAd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.oop.petrehome.R;

public class DisplayDogAd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_dog_ad);

        String USERID = getIntent().getExtras().getString("USERID");
        String IMGNUMBER = getIntent().getExtras().getString("IMGNUMBER");
        Toast.makeText(this, USERID+" "+IMGNUMBER,Toast.LENGTH_SHORT).show();
    }
}