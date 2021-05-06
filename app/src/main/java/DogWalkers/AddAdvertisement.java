package DogWalkers;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oop.petrehome.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AddAdvertisement extends AppCompatActivity {

    //Initialize variables
    ImageView imageView;
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    Button button;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        storageReference = FirebaseStorage.getInstance().getReference("DogWalking");
        databaseReference = FirebaseDatabase.getInstance().getReference("DogWalking");

        //Assign variable
        imageView=(ImageView)findViewById(R.id.imageView);
        e1=(EditText)findViewById(R.id.name);
        e2=(EditText)findViewById(R.id.contact);
        e3=(EditText)findViewById(R.id.email);
        e4=(EditText)findViewById(R.id.description);
        button=(Button)findViewById(R.id.submit);
        progressDialog = new ProgressDialog(getApplicationContext());



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadImage();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Picasso.get().load(FilePathUri).into(imageView);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImage() {

        if (FilePathUri != null) {

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
            }else {

                progressDialog.setTitle("Details are Uploading...");
                progressDialog.show();
                StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                storageReference2.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Advertisement uploaded Successfully ", Toast.LENGTH_LONG).show();
                                @SuppressWarnings("VisibleForTests")
                                AddDetails addDetails = new AddDetails(name,contact,address,price,discription,taskSnapshot.getUploadSessionUri().toString());
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String userid = user.getUid();
                                databaseReference.child(userid).setValue(addDetails);

                                Intent intent = new Intent(AddAdvertiesment.this, AdvertiesmentList.class);
                                startActivity(intent);

                            }
                        });
            }
        }
        else {

            Toast.makeText(AddAdvertiesment.this, "Missed detail/details", Toast.LENGTH_LONG).show();
        }
    }


    }
