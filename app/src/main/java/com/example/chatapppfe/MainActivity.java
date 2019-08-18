package com.example.chatapppfe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.chatapppfe.Fragments.ChatFragment;
import com.example.chatapppfe.Fragments.ContactFragment;
import com.example.chatapppfe.Fragments.GroupeFragment;
import com.example.chatapppfe.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout seartch;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference ;
    private Toolbar toolbar ;
    private BottomNavigationView bottomNavigationView ;
    private ViewPager viewPager;
    private TextView username ;
    private CircleImageView profile_image ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =findViewById(R.id.toolbar);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navLisnter);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatFragment()).commit();
        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        //seartch=findViewById(R.id.search_layout) ;
        viewPager=findViewById(R.id.viewpager) ;
        //viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        /*TabLayout tabLayout=findViewById(R.id.tablayout) ;
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bubble_speak);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list) ;
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_group) ;*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        InitializeFields();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });



        /*------------------------Get user information from database----------------------------------------*/
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser() ;
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users =dataSnapshot.getValue(Users.class) ;

                username.setText(users.getUsername());
                if (users.getImageURL().equals("default"))
                {
                    profile_image.setImageResource(R.drawable.default_image_user);
                }
                else
                {
                    Picasso.with(getApplicationContext()).load(users.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void InitializeFields()
    {

    }
    private  BottomNavigationView.OnNavigationItemSelectedListener navLisnter =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment= null ;
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_chat :
                            selectedFragment=new ChatFragment() ;

                            break;
                        case R.id.nav_contact :
                            selectedFragment=new ContactFragment() ;

                            break;
                        case R.id.nav_groupe :
                            selectedFragment=new GroupeFragment() ;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit() ;
                    return true ;
                }
            };
    /*--------------------------inflate the Option Menu ----------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*----------------------------------Logout-----------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        switch (item.getItemId())
        {
            case R.id.logout_item:
                logout();
                break;
            case R.id.change_password_item :
                break;
            case R.id.new_groupe_item :
                CreateNewGroupe();
                break;
            case R.id.new_message_item :
                SendToAll();
                break;
            default:
                return true;

        }
        return  true ;

    }
    public void logout()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout ?");
        dialog.setIcon(R.drawable.ic_action_exit);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();

    }
    /*------------------------------Create new Groupe ------------------------------------*/
    public void CreateNewGroupe()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Create New Group ");
        dialog.setMessage("Enter the group name :");
        dialog.setIcon(R.drawable.ic_new_group);
        final EditText name_groupe =new EditText(this);
        name_groupe.setHint("@example: Friends");
        dialog.setView(name_groupe) ;
        dialog.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(name_groupe.getText()))
                {
                    name_groupe.setHint("Enter name please..");
                }
                else
                {
                    reference= FirebaseDatabase.getInstance().getReference("Group");
                    reference.child(name_groupe.getText().toString()).setValue("") ;
                    name_groupe.setText("");
                }

            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    /*------------------------------Send Message to Users ------------------------------------*/
    public void SendToAll()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Send Message To All Users");
        dialog.setIcon(R.drawable.ic_action_send) ;
        dialog.setMessage("Enter your message :") ;
        final EditText input =new EditText(this);
        input.setHint("Hi !");
        dialog.setView(input) ;
        dialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(input.getText()))
                {
                    firebaseUser=FirebaseAuth.getInstance().getCurrentUser() ;
                    reference=FirebaseDatabase.getInstance().getReference("Users");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                final Users users=snapshot.getValue(Users.class);
                                if (!users.getId().equals(firebaseUser.getUid()))
                                {
                                    Calendar calendar=Calendar.getInstance() ;
                                    SimpleDateFormat format=new SimpleDateFormat("HH:mm") ;
                                    String time=format.format(calendar.getTime()) ;
                                    String key = reference.child("Chats").push().getKey() ;
                                    HashMap<String,Object>hashMap=new HashMap<>();
                                    hashMap.put("sender",firebaseUser.getUid());
                                    hashMap.put("receiver",users.getId());
                                    hashMap.put("message",input.getText().toString());
                                    hashMap.put("id",key);
                                    hashMap.put("time",time);
                                    hashMap.put("isseen",false) ;
                                    reference=FirebaseDatabase.getInstance().getReference("Chats") ;
                                    reference.child(key).setValue(hashMap) ;
                                    final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid()).child(users.getId());
                                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(!dataSnapshot.exists())
                                            {
                                                chatRef.child("id").setValue(users.getId());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    /*---------------------------Chiking Status Online or Offline ----------------------*/
    public  void status(String status)
    {
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object>hashMap=new HashMap<>() ;
        hashMap.put("status",status) ;
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}



