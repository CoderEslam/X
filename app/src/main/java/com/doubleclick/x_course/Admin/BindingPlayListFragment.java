package com.doubleclick.x_course.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.doubleclick.x_course.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BindingPlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BindingPlayListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    public BindingPlayListFragment() {
        // Required empty public constructor
    }

    private Spinner spinner;
    private DatabaseReference referenceMobile, referenceWeb, referenceGraphicDesign, referenceAllPlayLists;
    private ArrayList<String> diplomaNameArrayList = new ArrayList<>();
    private String nameDiplomaSpinner;
    private EditText numberOfDiploma;
    private Button done;
    private LinearLayout Mobile, Graphic, Web;
    private String[] list;
    // additional information
    private EditText nameOfDevelpoer,
            aboutCourse,
            UrlAnimation,
            lastPrice,
            newPrice,
            promo_youtube;
    private ImageView imageOfDiploma;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri = null;
    private StorageTask uploadTask;
    private StorageReference storageReference;

    // Mobile EditText
    private EditText JavaPlayList,
            FlutterPlayList,
            KotinPlayList,
            IosPlayList;

    // Web EditText
    private EditText HTMLPlayList,
            CSSPlayList,
            JavaScriptPlayList,
            PhpPlayList,
            SQLPlayList,
            BootStrapPlayList,
            LaravelPlayList,
            ReactPlayList;

    //Graphic EditText
    private EditText PhotoshopPlayList,
            IllustratorPlayList,
            InDesignPlayList,
            PremierProplaylist,
            AfterEffectPlayList,
            AdobeAuditionPlayList,
            AdobeXDPlaylist;
    private Date date;


    // TODO: Rename and change types and number of parameters
    public static BindingPlayListFragment newInstance(String param1, String param2) {
        BindingPlayListFragment fragment = new BindingPlayListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referenceAllPlayLists = FirebaseDatabase.getInstance().getReference().child("AllPlayLists");
        referenceMobile = FirebaseDatabase.getInstance().getReference().child("Mobile");
        referenceWeb = FirebaseDatabase.getInstance().getReference().child("Web");
        referenceGraphicDesign = FirebaseDatabase.getInstance().getReference().child("GraphicDesign");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        date = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist_binding, container, false);
        ////////////////////////////////////////////////////////////////////////////////
        Web = view.findViewById(R.id.Web);
        HTMLPlayList = view.findViewById(R.id.HTMLPlayList);
        CSSPlayList = view.findViewById(R.id.CSSPlayList);
        JavaScriptPlayList = view.findViewById(R.id.JavaScriptPlayList);
        PhpPlayList = view.findViewById(R.id.PhpPlayList);
        SQLPlayList = view.findViewById(R.id.MySQLPlayList);
        BootStrapPlayList = view.findViewById(R.id.BootStrapPlayList);
        LaravelPlayList = view.findViewById(R.id.LaravelPlayList);
        ReactPlayList = view.findViewById(R.id.ReactPlayList);
        ///////////////////////////////////////////////////////////////////////////////
        Mobile = view.findViewById(R.id.Mobile); //Mobile
        JavaPlayList = view.findViewById(R.id.JavaPlayList);
        FlutterPlayList = view.findViewById(R.id.FlutterPlayList);
        KotinPlayList = view.findViewById(R.id.KotinPlayList);
        IosPlayList = view.findViewById(R.id.IosPlayList);
        ////////////////////////////////////////////////////////////////////////////////
        Graphic = view.findViewById(R.id.Graphic);//Graphic
        PhotoshopPlayList = view.findViewById(R.id.PhotoshopPlayList);
        IllustratorPlayList = view.findViewById(R.id.IllustratorPlayList);
        InDesignPlayList = view.findViewById(R.id.InDesignPlayList);
        PremierProplaylist = view.findViewById(R.id.PremierProplaylist);
        AfterEffectPlayList = view.findViewById(R.id.AfterEffectPlayList);
        AdobeAuditionPlayList = view.findViewById(R.id.AdobeAuditionPlayList);
        AdobeXDPlaylist = view.findViewById(R.id.AdobeXDPlaylist);
        ////////////////////////////////////////////////////////////////////////////////
        // additional information
        nameOfDevelpoer = view.findViewById(R.id.nameOfDevelpoer);
        aboutCourse = view.findViewById(R.id.aboutCourse);
        UrlAnimation = view.findViewById(R.id.UrlAnimation);
        lastPrice = view.findViewById(R.id.lastPrice);
        newPrice = view.findViewById(R.id.newPrice);
        imageOfDiploma = view.findViewById(R.id.imageOfDiploma);
        promo_youtube = view.findViewById(R.id.promo_youtube);
        ////////////////////////////////////////////////////////////////////////////////
        spinner = view.findViewById(R.id.coursesspinner);
        spinner.setOnItemSelectedListener(this);
        numberOfDiploma = view.findViewById(R.id.number);
        done = view.findViewById(R.id.done);
        list = getResources().getStringArray(R.array.Diploma);
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        imageOfDiploma.setOnClickListener(cklicked -> {
            openGallery();
        });


        done.setOnClickListener(cklicked -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                if (imageUri != null) {
                    uploadImage();
                } else {
                    uploadWithoutImage();
                }
            }
        });

        //https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/



        return view;
    }

    private void uploadWithoutImage() {
        HashMap<String, Object> map = new HashMap<>();
        if (!nameOfDevelpoer.getText().toString().equals("")) {
            map.put("NameOfDevelpoer", "" + nameOfDevelpoer.getText().toString());
        } else {
            Toast.makeText(getContext(), "Name of Delveloper", Toast.LENGTH_LONG).show();
        }
        if (!aboutCourse.getText().toString().equals("")) {
            map.put("AboutCourse", "" + aboutCourse.getText().toString());
        } else {
            Toast.makeText(getContext(), "About Course", Toast.LENGTH_LONG).show();
        }
        if (!lastPrice.getText().toString().equals("")) {
            map.put("LastPrice", "" + lastPrice.getText().toString());
        } else {
            Toast.makeText(getContext(), "Last Price", Toast.LENGTH_LONG).show();
        }
        if (!newPrice.getText().toString().equals("")) {
            map.put("newPrice", "" + newPrice.getText().toString());
        } else {
            Toast.makeText(getContext(), "New Price", Toast.LENGTH_LONG).show();
        }
        if (!nameOfDevelpoer.getText().toString().equals("")
                && !aboutCourse.getText().toString().equals("")
                && !lastPrice.getText().toString().equals("")
                && !newPrice.getText().toString().equals("")
                && !numberOfDiploma.getText().toString().equals("")
                && !promo_youtube.getText().toString().equals("")) {
            //numberOfDiploma
            if (nameDiplomaSpinner.equals("Web")) {
                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                referenceWeb.child(CompositeId).updateChildren(map);
                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                WebPlayList(CompositeId);
            } else if (nameDiplomaSpinner.equals("Mobile")) {
                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                referenceMobile.child(CompositeId).updateChildren(map);
                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                MobilePlayList(CompositeId);
            } else if (nameDiplomaSpinner.equals("graphicDesign")) {
                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                referenceGraphicDesign.child(CompositeId).updateChildren(map);
                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                GraphicPlayList(CompositeId);
            }
            Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
        }


    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageOfDiploma.setImageURI(imageUri);
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
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
                        HashMap<String, Object> map = new HashMap<>();
                        if (!nameOfDevelpoer.getText().toString().equals("")
                                && !aboutCourse.getText().toString().equals("")
                                && !lastPrice.getText().toString().equals("")
                                && !newPrice.getText().toString().equals("")
                                && !mUri.equals("")
                                && !promo_youtube.getText().toString().equals("")) {
                            if (!nameOfDevelpoer.getText().toString().equals("")) {
                                map.put("NameOfDevelpoer", "" + nameOfDevelpoer.getText().toString());
                            } else {
                                Toast.makeText(getContext(), "Name of Delveloper", Toast.LENGTH_LONG).show();
                            }
                            if (!aboutCourse.getText().toString().equals("")) {
                                map.put("AboutCourse", "" + aboutCourse.getText().toString());
                            } else {
                                Toast.makeText(getContext(), "About Course", Toast.LENGTH_LONG).show();
                            }
                            if (UrlAnimation.getText().toString().equals("")) {
                                map.put("UrlAnimation", "");
                            } else {
                                map.put("UrlAnimation", "" + UrlAnimation.getText().toString());
                            }
                            if (!lastPrice.getText().toString().equals("")) {
                                map.put("LastPrice", "" + lastPrice.getText().toString());
                            } else {
                                Toast.makeText(getContext(), "Last Price", Toast.LENGTH_LONG).show();
                            }
                            if (!newPrice.getText().toString().equals("")) {
                                map.put("newPrice", "" + newPrice.getText().toString());
                            } else {
                                Toast.makeText(getContext(), "New Price", Toast.LENGTH_LONG).show();
                            }//numberOfDiploma
                            map.put("imageOfDiploma", "" + mUri);
                            map.put("promo_youtube", promo_youtube.getText().toString());
                            if (nameDiplomaSpinner.equals("Web")) {
                                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                                referenceWeb.child(CompositeId).updateChildren(map);
                                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                                WebPlayList(CompositeId);
                            } else if (nameDiplomaSpinner.equals("Mobile")) {
                                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                                referenceMobile.child(CompositeId).updateChildren(map);
                                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                                MobilePlayList(CompositeId);
                            } else if (nameDiplomaSpinner.equals("graphicDesign")) {
                                String CompositeId = nameOfDevelpoer.getText().toString() + ":" + nameDiplomaSpinner + ":" + numberOfDiploma.getText().toString() + ":" + date.getTime();
                                referenceGraphicDesign.child(CompositeId).updateChildren(map);
                                referenceAllPlayLists.child(CompositeId).updateChildren(map);
                                GraphicPlayList(CompositeId);
                            }
                        }
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        nameDiplomaSpinner = list[i];
        if (nameDiplomaSpinner.equals("Web")) {
            Web.setVisibility(View.VISIBLE);
            Mobile.setVisibility(View.GONE);
            Graphic.setVisibility(View.GONE);
        } else if (nameDiplomaSpinner.equals("Mobile")) {
            Web.setVisibility(View.GONE);
            Mobile.setVisibility(View.VISIBLE);
            Graphic.setVisibility(View.GONE);
        } else if (nameDiplomaSpinner.equals("graphicDesign")) {
            Web.setVisibility(View.GONE);
            Mobile.setVisibility(View.GONE);
            Graphic.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        nameDiplomaSpinner = list[0];
    }

    private void WebPlayList(String pushId) {
        Map<String, Object> wepMap = new HashMap<>();
        wepMap.put("numberOfDiploma", numberOfDiploma.getText().toString());
        wepMap.put("nameOfDiploma", nameDiplomaSpinner);
        wepMap.put("pushId", pushId);
        wepMap.put("timestamp", date.getTime());
        if (!HTMLPlayList.getText().toString().isEmpty()) {
            wepMap.put("HTML", HTMLPlayList.getText().toString().trim());

        }
        if (!CSSPlayList.getText().toString().isEmpty()) {
            wepMap.put("CSS", CSSPlayList.getText().toString().trim());

        }
        if (!JavaScriptPlayList.getText().toString().isEmpty()) {
            wepMap.put("JavaScript", JavaScriptPlayList.getText().toString().trim());

        }
        if (!PhpPlayList.getText().toString().isEmpty()) {
            wepMap.put("Php", PhpPlayList.getText().toString().trim());

        }
        if (!SQLPlayList.getText().toString().isEmpty()) {
            wepMap.put("MySQL", SQLPlayList.getText().toString().trim());

        }
        if (!LaravelPlayList.getText().toString().isEmpty()) {
            wepMap.put("Laravel", LaravelPlayList.getText().toString().trim());

        }
        if (!ReactPlayList.getText().toString().isEmpty()) {
            wepMap.put("React", ReactPlayList.getText().toString().trim());

        }
        if (!BootStrapPlayList.getText().toString().isEmpty()) {
            wepMap.put("BootStrap", BootStrapPlayList.getText().toString().trim());

        }
        referenceWeb.child(pushId).updateChildren(wepMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    referenceAllPlayLists.child(pushId).updateChildren(wepMap);
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void MobilePlayList(String pushId) {
        Map<String, Object> mobileMap = new HashMap<>();
        mobileMap.put("numberOfDiploma", numberOfDiploma.getText().toString());
        mobileMap.put("nameOfDiploma", nameDiplomaSpinner);
        mobileMap.put("pushId", pushId);
        mobileMap.put("timestamp", date.getTime());
        if (!JavaPlayList.getText().toString().isEmpty() && !JavaPlayList.getText().toString().equals("")) {
            mobileMap.put("Java", JavaPlayList.getText().toString().trim());

        }
        if (!FlutterPlayList.getText().toString().isEmpty() && !FlutterPlayList.getText().toString().equals("")) {
            mobileMap.put("Flutter", FlutterPlayList.getText().toString().trim());

        }
        if (!KotinPlayList.getText().toString().isEmpty() && !KotinPlayList.getText().toString().equals("")) {
            mobileMap.put("Kotlin", KotinPlayList.getText().toString().trim());

        }
        if (!IosPlayList.getText().toString().isEmpty() && !IosPlayList.getText().toString().equals("")) {
            mobileMap.put("Ios", IosPlayList.getText().toString().trim());

        }
        referenceMobile.child(pushId).updateChildren(mobileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    referenceAllPlayLists.child(pushId).updateChildren(mobileMap);
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void GraphicPlayList(String pushId) {
        Map<String, Object> graphicMap = new HashMap<>();
        graphicMap.put("numberOfDiploma", numberOfDiploma.getText().toString());
        graphicMap.put("nameOfDiploma", nameDiplomaSpinner);
        graphicMap.put("pushId", pushId);
        graphicMap.put("timestamp", date.getTime());
        if (!PhotoshopPlayList.getText().toString().isEmpty()) {
            graphicMap.put("Photoshop", PhotoshopPlayList.getText().toString().trim());

        }
        if (!IllustratorPlayList.getText().toString().isEmpty()) {
            graphicMap.put("Illustrator", IllustratorPlayList.getText().toString().trim());
        }
        if (!InDesignPlayList.getText().toString().isEmpty()) {
            graphicMap.put("InDesign", InDesignPlayList.getText().toString().trim());

        }
        if (!PremierProplaylist.getText().toString().isEmpty()) {
            graphicMap.put("PremierPro", PremierProplaylist.getText().toString().trim());
        }
        if (!AfterEffectPlayList.getText().toString().isEmpty()) {
            graphicMap.put("AfterEffect", AfterEffectPlayList.getText().toString().trim());
        }
        if (!AdobeAuditionPlayList.getText().toString().isEmpty()) {
            graphicMap.put("AdobeAudition", AdobeAuditionPlayList.getText().toString().trim());

        }
        if (!AdobeXDPlaylist.getText().toString().isEmpty()) {
            graphicMap.put("AdobeXD", AdobeXDPlaylist.getText().toString().trim());
        }
        referenceGraphicDesign.child(pushId).updateChildren(graphicMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    referenceAllPlayLists.child(pushId).updateChildren(graphicMap);
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}