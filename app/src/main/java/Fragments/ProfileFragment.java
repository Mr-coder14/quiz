package Fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quiz.EditDetails;
import com.example.quiz.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adaptor.TabsAdapter;
import Classes.User;
import LogInactiviies.LoginActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private TextView email,username;
    private CircleImageView profileimage;
    private DatabaseReference database;
    private String userid;
    private LinearLayout li;
    private ProgressBar progressBar;
    private LinearLayout lgout;
    private Button btn;
    private FirebaseAuth auth;
    private User user;
    private DatabaseReference usersRef;
    private String currentUserID;
    private FirebaseUser us;

    public ProfileFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_profile,container,false);
        email=view.findViewById(R.id.emailtxt);
        username=view.findViewById(R.id.usernametxt);
     lgout=view.findViewById(R.id.lodoutbtn);
     profileimage=view.findViewById(R.id.profilemiange);
     btn=view.findViewById(R.id.btneditprofile);
     progressBar=view.findViewById(R.id.profile_image_progress);
     li=view.findViewById(R.id.li);
     li.setVisibility(View.GONE);
     progressBar.setVisibility(View.VISIBLE);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the reference to the current user in Firebase Database
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

        // Load the profile image
        loadProfileImage();
//        tableLayout=view.findViewById(R.id.tab_mains);
//        viewPager=view.findViewById(R.id.viewpagermain);
        auth=FirebaseAuth.getInstance();
        us=auth.getCurrentUser();

        lgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditDetails.class));
            }
        });

        userid= FirebaseAuth.getInstance().getUid();
        database=FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        userinformation();
        return view;
    }

    private void dailogbox() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (us != null) {
                    auth.signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void userinformation() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);

                    if (user != null) {
                        email.setText(user.getEmail());
                        username.setText(user.getName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadProfileImage() {
        usersRef.child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.getValue(String.class);

                    if (imageUrl != null && !imageUrl.isEmpty()) {

                        Glide.with(getContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.person3)
                                .into(profileimage);
                        li.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load the Profile Image", Toast.LENGTH_SHORT).show();
                li.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

}