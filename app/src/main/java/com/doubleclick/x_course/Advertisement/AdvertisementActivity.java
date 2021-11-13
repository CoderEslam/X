package com.doubleclick.x_course.Advertisement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.doubleclick.x_course.Adapter.AdvertisementAdapter;
import com.doubleclick.x_course.Model.Advertisement;
import com.doubleclick.x_course.R;
import com.doubleclick.x_course.ViewModel.AdvertisementViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvertisementActivity extends AppCompatActivity {

    private LottieAnimationView animationImage;
    private EditText videoId, aboutDiploma;
    private ImageView image_diploma;
    private RecyclerView recycler_Advertisement;
    private Button Upload;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private DatabaseReference referenceAdvertisement;
    private AdvertisementViewModel advertisementViewModel;
    private AdvertisementAdapter advertisementAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        animationImage = findViewById(R.id.animationImage);
        image_diploma = findViewById(R.id.image_diploma);
        videoId = findViewById(R.id.videoId);
        aboutDiploma = findViewById(R.id.aboutDiploma);
        Upload = findViewById(R.id.Upload);
        recycler_Advertisement = findViewById(R.id.recycler_Advertisement);
        recycler_Advertisement.setLayoutManager(new LinearLayoutManager(this));
        advertisementViewModel = new ViewModelProvider(this).get(AdvertisementViewModel.class);
        storageReference = FirebaseStorage.getInstance().getReference("Advertisement");
        referenceAdvertisement = FirebaseDatabase.getInstance().getReference().child("Advertisement");
        advertisementViewModel.LoadAdv().observe(this, new Observer<ArrayList<Advertisement>>() {
            @Override
            public void onChanged(ArrayList<Advertisement> advertisements) {
                advertisementAdapter = new AdvertisementAdapter(advertisements);
                recycler_Advertisement.setAdapter(advertisementAdapter);
                advertisementAdapter.notifyDataSetChanged();
            }
        });
        animationImage.setOnClickListener(view -> {
            openGallery();
        });
        Upload.setOnClickListener(view -> {
            uploadImage();
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Advertisement advertisement = advertisementAdapter.getAdv(position);
                referenceAdvertisement.child(advertisement.getPushId()).removeValue();

            }
        }).attachToRecyclerView(recycler_Advertisement);

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            animationImage.setVisibility(View.GONE);
            image_diploma.setImageURI(imageUri);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(AdvertisementActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        String push = referenceAdvertisement.push().getKey().toString();
                        Map<String, Object> map = new HashMap<>();
                        if (!aboutDiploma.getText().toString().isEmpty()) {
                            map.put("about", aboutDiploma.getText().toString());
                        }
                        if (!videoId.getText().toString().isEmpty()) {
                            map.put("videoId", videoId.getText().toString());
                        }
                        map.put("image", mUri);
                        map.put("pushId", push);
                        referenceAdvertisement.child(push).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(AdvertisementActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdvertisementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(AdvertisementActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

}