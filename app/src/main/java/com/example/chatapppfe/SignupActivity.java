package com.example.chatapppfe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    DatabaseReference reference ;
    FirebaseAuth firebaseAuth ;
    private ProgressBar progressBar ;
    private EditText Username ;
    private EditText Email ;
    private EditText Password ;
    private Button Signup ;
    private TextView Login ;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        InitializeFields();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });

    }
    public  void InitializeFields()
    {
        firebaseAuth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.activity_signup_toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Username=findViewById(R.id.signup_activity_username_input);
        Email=findViewById(R.id.signup_activity_email_input);
        Password=findViewById(R.id.signup_activity_password_input);
        Signup=findViewById(R.id.signup_activity_signup_btn);
        Login=findViewById(R.id.signup_activity_login_textView);
        progressBar=findViewById(R.id.signup_activity_ProgressBar);
    }
    public void Signup()
    {
        progressBar.setVisibility(View.VISIBLE);
        //Tester si l'utilsateur a remplir tous les champs correctemment(Username ,Email ,Password )
        if(TextUtils.isEmpty(Username.getText())||TextUtils.isEmpty(Email.getText())||TextUtils.isEmpty(Password.getText()))
        {
            Toasty.error(SignupActivity.this,"All fields are required !", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {    //Sinon Tester si le password contien au moin  6 caractére
            if (Password.getText().length()<8)
            {
                Toasty.error(SignupActivity.this,"Password must be at least 8 characters long !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
            //sinon envoyer la requette au base de donner firebase
            else
            {
                firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        //Si l'iscription est fait corectement  dans la base de donner
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String userid =firebaseUser.getUid();
                            reference=FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",Username.getText().toString());
                            hashMap.put("imageURL","default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toasty.success(SignupActivity.this,"Successfully registered" ,Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        }
                        //Sinon afficher le probéleme
                        else
                        {
                            Toasty.error(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }
}
