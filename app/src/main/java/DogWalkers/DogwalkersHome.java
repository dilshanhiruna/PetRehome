package DogWalkers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oop.petrehome.R;

public class DogwalkersHome extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ListView listview;
    String[] dogwalkers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogwalkers_home);

    }
}