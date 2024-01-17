package com.example.eznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String note_str = "Enter a new note...";
    public static int id_index, numOfNotes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView note = findViewById(R.id.textView);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(uid);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.entrySet() != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (map.containsKey("notes")) {
                                if (entry.getKey().equals("notes")) {
                                    if (map.get(entry.getKey()) != null) {
                                        ArrayList<String> u_notes = (ArrayList<String>) entry.getValue();
                                        LinearLayout linearLayout = findViewById(R.id.linearLayout);
                                        linearLayout.removeAllViews();
                                        numOfNotes = u_notes.size();
                                        for (int x = u_notes.size() - 1; x >= 0; x--) {
                                            if (u_notes.get(x) != null) {
                                                createElement(note.getContext(), u_notes.get(x), x);
                                            }C:\Users\deves\AndroidStudioProjects\EZNotes2\app\src\main\java\com\example\eznotes\MainActivity.java
                                        }
                                    } else {
                                        noNotesFoundError(note.getContext());
                                    }
                                }
                            } else {
                                noNotesFoundError(note.getContext());
                            }
                        }
                    } else{
                        noNotesFoundError(note.getContext());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to read the database", Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            logout();
        }
    }

    public void noNotesFoundError(Context context){
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        createElement(context, "No notes were found...", 0);

    }

    @SuppressLint("SetTextI18n")
    public void createElement(Context context, String u_note, int id){
        LinearLayout linearLayout = findViewById(R.id.linearLayout);


        Button button = new Button(context);
        button.setHeight(150);
        button.setText(u_note);
        button.setTextColor(Color.parseColor("#4f4f4f"));
        button.setAllCaps(false);
        button.setId(id);


        linearLayout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_str = u_note;
                id_index = id;
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });


    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.barLogout){
            logout();
        } else if (id == R.id.addNote){
            startActivity(new Intent(MainActivity.this, NewNote.class));

        }
        return super.onOptionsItemSelected(item);
    }
}