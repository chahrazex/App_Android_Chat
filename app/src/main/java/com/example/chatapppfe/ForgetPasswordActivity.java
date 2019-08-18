package com.example.chatapppfe;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth ;
    private ProgressBar progressBar ;
    private EditText Email ;
    private Button ForgetPasswordbtn ;
    private Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        InitializeFields();

        ForgetPasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Forget_Password();
            }
        });
    }
    public  void InitializeFields()
    {
        toolbar=findViewById(R.id.activity_forgetPassword_toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth=FirebaseAuth.getInstance();
        Email=findViewById(R.id.Forget_Password_activity_email_input);
        ForgetPasswordbtn=findViewById(R.id.Forget_Password_activity_forgotPassword_btn);
        progressBar=findViewById(R.id.Forget_Password_activity_ProgressBar);
    }
    public void Forget_Password()
    {
        progressBar.setVisibility(View.VISIBLE);
        if(TextUtils.isEmpty(Email.getText()))
        {
            Toasty.error(ForgetPasswordActivity.this,"Enter your Email Please !",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else
        {
            firebaseAuth.sendPasswordResetEmail(Email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toasty.success(ForgetPasswordActivity.this,"Password send Successfuly chek your email ...", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toasty.error(ForgetPasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    }
                }
            });
        }

    }
}
