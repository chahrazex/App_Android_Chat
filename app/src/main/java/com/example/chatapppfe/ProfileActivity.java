package com.example.chatapppfe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chatapppfe.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    //Android Layout

    private Toolbar toolbar ;
    private LinearLayout deletCount,changPassword ,changeUsersName;
    private FirebaseUser firebaseUser ;
    private ProgressBar progressBar ;
    private CircleImageView profile_image ;
    private TextView username ;
    private static final int IMAGE_REQUEST=1 ;
    private DatabaseReference reference;

    //Storage Firebase

    private StorageReference storageReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        InitializeFields();



        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class);
                username.setText(users.getUsername());
                if(users.getImageURL().equals("default"))
                {

                    profile_image.setImageResource(R.drawable.default_image_user);
                }
                else
                {
                    Picasso.with(ProfileActivity.this).load(users.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        changeUsersName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsersname();
            }
        });
        deletCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletAccount();
            }
        });
        changPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }

    private void changeUsersname() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Change Username ") ;
        dialog.setMessage("Enter a new username :");
        dialog.setIcon(R.drawable.ic_action_alternate_email) ;

        final  EditText newUsername=new EditText(this);
        newUsername.setHint(username.getText());
        dialog.setView(newUsername) ;
        dialog.setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(newUsername.getText()))
                {

                }
                else
                {
                    firebaseUser=FirebaseAuth.getInstance().getCurrentUser() ;
                    reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                    HashMap<String,Object> hashMap=new HashMap<>() ;
                    hashMap.put("username",newUsername.getText().toString()) ;
                    reference.updateChildren(hashMap) ;
                    Toasty.success(getApplicationContext(),"username changed successfully...",Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show() ;

    }
    public void changePassword()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this) ;
        dialog.setIcon(R.drawable.ic_action_lock) ;
        dialog.setTitle("Change Password");
        dialog.setMessage("Enter a new  password :");
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText input=new EditText(this);
        input.setLayoutParams(lp);
        input.setHint("new password");
        dialog.setView(input);
        dialog.setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().length() < 8) {

                    Toasty.error(ProfileActivity.this, "password must be at last 8 caractères"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseUser.updatePassword(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toasty.success(ProfileActivity.this, "Password changed " +
                                        "successfully...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toasty.error(ProfileActivity.this, task.getException().getMessage()
                                        , Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show() ;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==IMAGE_REQUEST &&resultCode==RESULT_OK)
        {
            Uri imageUri =data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                storageReference= FirebaseStorage.getInstance().getReference("Profile_Images").child(firebaseUser.getUid()+"jpg");
                final StorageReference filePath = storageReference.child(firebaseUser.getUid() + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri download_url=uri;
                                String url=download_url.toString();
                                reference.child("imageURL").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(ProfileActivity.this, "Working", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });


            }
        }
    }

    /*---------------------------------Initilaization Faield-----------------------------------------*/
    public void InitializeFields()
    {
        toolbar = findViewById(R.id.toolbar_profile);
        deletCount = findViewById(R.id.compte_activity_delet_acount);
        changPassword = findViewById(R.id.compte_activity_change_password);
        progressBar = findViewById(R.id.compte_activity_progressBar);
        profile_image=findViewById(R.id.compte_activity_profile_image);
        username=findViewById(R.id.compte_activity_username);
        changeUsersName=findViewById(R.id.compte_activity_change_username);
        // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /*--------------------------------Deliting Account-----------------------------------------------*/
    public void DeletAccount ()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(ProfileActivity.this);
        dialog.setTitle("Are you sure ?");
        dialog.setMessage("Deleting this account will result in completely removing your " +
                "account from the system and you won't be able to access the app");
        dialog.setIcon(R.drawable.ic_action_delete);

        //Si l'utilsateur Confirmer la suppr(Delete)
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                final String  id=firebaseUser.getUid() ;
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent=new Intent(ProfileActivity.this,LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else
                        {
                            Toasty.error(ProfileActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        //Si l'utilsateur annuler la suppr(Dismiss)
        dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog= dialog.create();
        alertDialog.show();
    }
    /*--------------------------------change image profile-----------------------------------------------*/
    public void openImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),IMAGE_REQUEST);

    }


}
