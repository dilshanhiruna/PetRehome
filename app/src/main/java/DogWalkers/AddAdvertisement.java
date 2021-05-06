package DogWalkers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import PostAd.DogListing;
import user.Login;
import user.UserProfile;

public class AddAdvertisement extends AppCompatActivity {

    //Initialize variables
    ImageView imageView_dogwalkers_profile;
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    Button button;
    Uri FilePathUri;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    String userID;
    Uri  img1URI1 = Uri.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        //Assign variable
        imageView_dogwalkers_profile=findViewById(R.id.imageView_dogwalkers_profile);
        e1=findViewById(R.id.name);
        e2=findViewById(R.id.contact);
        e3=findViewById(R.id.email);
        e4=findViewById(R.id.description);
        button=findViewById(R.id.submit);
        progressDialog = new ProgressDialog(getApplicationContext());

        imageView_dogwalkers_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_Gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_Gallery,7676);
            }
        });


        if (fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }else  userID = fAuth.getCurrentUser().getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadImage();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==7676){
            if (resultCode == Activity.RESULT_OK ){

                img1URI1 = data.getData();
                imageView_dogwalkers_profile.setImageURI(img1URI1);
                imageView_dogwalkers_profile.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

    }

//    public String GetFileExtension(Uri uri) {
//
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
//
//    }

    public void UploadImage() {

        final String name = e1.getText().toString().trim();
        final String contact = e2.getText().toString().trim();
        final String email = e3.getText().toString().trim();
        final String description = e4.getText().toString().trim();

        if(name.isEmpty()){
            e1.setError("Name is required");
        }else if(contact.isEmpty()){
            e2.setError("Contact number is required");
        }else if(email.isEmpty()){
            e3.setError("Email is required");
        }else if(description.isEmpty()){
            e4.setError("Description is required");
        }
        else if((img1URI1.equals(Uri.EMPTY))){
            Toast.makeText(AddAdvertisement.this, "Main Image Required", Toast.LENGTH_SHORT).show();
        }else  {


            databaseReference = FirebaseDatabase.getInstance().getReference().child("DogWalkers").child(userID).child("profile");
            Map<String,Object> DogWalkers = new HashMap<>();
            DogWalkers.put("name",name);
            DogWalkers.put("contact",contact);
            DogWalkers.put("email",email);
            DogWalkers.put("description",description);

            databaseReference.setValue(DogWalkers).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    StorageReference fileRef1 = storageReference.child("dogwalkers/"+fAuth.getCurrentUser().getUid()+"/"+"img.jpg");
                    fileRef1.putFile(img1URI1);
                    startActivity(new Intent(getApplicationContext(), DogwalkersHome.class));
                    Toast.makeText(AddAdvertisement.this, "Profile Created", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }




}
