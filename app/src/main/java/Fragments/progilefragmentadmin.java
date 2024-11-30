package Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Classes.User;
import LogInactiviies.LoginActivity;


public class progilefragmentadmin extends Fragment {
    private Button btn;
    private TextView email,username;
    private DatabaseReference database;
    private String userid;
    private LinearLayout lgout;
    private FirebaseAuth auth;
    private User user;
    private FirebaseUser us;

    public progilefragmentadmin() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progilefragmentadmin, container, false);
        email=view.findViewById(R.id.emailtxt1);
        username=view.findViewById(R.id.usernametxt1);
        lgout=view.findViewById(R.id.lodoutbtn1);
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

        userid= FirebaseAuth.getInstance().getUid();
        database= FirebaseDatabase.getInstance().getReference().child("admins").child(userid);
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
}