package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.toborehumble.schoolrunner.pojo.Story;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadPictureActivity extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference profile_pic_ref;
    FirebaseUser auth_user;
    String username;

    private static final int REQUEST_IMAGE_GET = 123;
    private static final int REQUEST_SELECT_IMAGE = 124;
    private Uri profilePhotoUri;
    File image;
    byte[] bytes;

    ImageView upload_photo;
    Button upload_photo_btn;
    Button take_photo_btn;
    Button select_picture_btn;
    TextView image_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        upload_photo = findViewById(R.id.upload_picture);
        take_photo_btn = findViewById(R.id.take_photo_btn);
        upload_photo_btn = findViewById(R.id.upload_photo_btn);
        select_picture_btn = findViewById(R.id.select_photo_btn);
        image_location = findViewById(R.id.image_location);

        image_location.setText(null);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        username = bundle.getString("username");
        auth_user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        profile_pic_ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(auth_user.getUid()).child("profile").child("profilePicture");

        take_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        upload_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bytes == null) {
                    uploadProfilePicture(image);
                } else {
                    uploadProfilePicture(bytes);
                }
                upload_photo_btn.setEnabled(false);
            }
        });

        select_picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void selectImage() {
        Intent selectPicture = new Intent(Intent.ACTION_GET_CONTENT);
        selectPicture.setType("image/*");
        createFile();
        selectPicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        if (selectPicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(selectPicture, REQUEST_SELECT_IMAGE);
        }
    }

    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        createFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(cameraIntent, REQUEST_IMAGE_GET);
    }

    private void createFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(username + "_profile_photo", ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadProfilePicture(File file) {
        final StorageReference profile_ref = storageReference.child("profile_pictures/" + file.getName());
        UploadTask uploadTask = profile_ref.putFile(Uri.fromFile(file));
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                /*profile_pic_ref.setValue(profilePhotoUri.toString());*/
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPictureActivity.this, "pic failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadPictureActivity.this, "" +
                        taskSnapshot.getBytesTransferred(), Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(UploadPictureActivity.this, "Unable to get image download url",
                            Toast.LENGTH_SHORT).show();
                }
                return profile_ref.getDownloadUrl();
            }
        });
        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    profilePhotoUri = task.getResult();
                    profile_pic_ref.setValue(profilePhotoUri.toString());
                    createUserSignInStory();
                    toSplashActivity();
                }
            }
        });
    }

    private void uploadProfilePicture(byte[] bytes) {
        final StorageReference profile_ref = storageReference.child("profile_pictures/" + image.getName());
        UploadTask uploadTask = profile_ref.putBytes(bytes);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                /*profile_pic_ref.setValue(profilePhotoUri.toString());*/
            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPictureActivity.this, "pic failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadPictureActivity.this, "" +
                        taskSnapshot.getBytesTransferred(), Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(UploadPictureActivity.this, "Unable to get image download url",
                            Toast.LENGTH_SHORT).show();
                }
                return profile_ref.getDownloadUrl();
            }
        });
        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    profilePhotoUri = task.getResult();
                    profile_pic_ref.setValue(profilePhotoUri.toString());
                    createUserSignInStory();
                    toSplashActivity();
                }
            }
        });
    }

    private void toSplashActivity() {
        Intent toSplashActivity = new Intent(
                UploadPictureActivity.this, SplashActivity.class
        );
        toSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toSplashActivity);
    }

    private void createUserSignInStory() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Story story = new Story(username, "created a new account", auth_user.getUid());
        String key = dbRef.child("stories").push().getKey();
        Map<String, Object> storyMap = new HashMap<>();
        storyMap.put("/stories/" + key, story);
        dbRef.updateChildren(storyMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadPictureActivity.this, "story created", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPictureActivity.this, "story failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE: {
                upload_photo.setImageURI(data.getData());
                image_location.setText(image.getName());
                upload_photo_btn.setEnabled(true);
                take_photo_btn.setEnabled(false);
                select_picture_btn.setEnabled(false);

                Bitmap thumbnail = ((BitmapDrawable) upload_photo.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bytes = baos.toByteArray();
            }
            break;
            case REQUEST_IMAGE_GET: {
                upload_photo.setImageURI(Uri.fromFile(image));
                image_location.setText(image.getName());
                upload_photo_btn.setEnabled(true);
                take_photo_btn.setEnabled(false);
                select_picture_btn.setEnabled(false);
            }
            break;
        }
    }
}
