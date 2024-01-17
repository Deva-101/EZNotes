package com.example.eznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        String note = MainActivity.note_str;
        EditText noteContent = findViewById(R.id.noteContents);
        Button back = findViewById(R.id.back);
        Button save = findViewById(R.id.save);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference(Objects.requireNonNull(mAuth.getUid()));


        back.setOnClickListener(view -> new AlertDialog.Builder(noteContent.getContext())
            .setTitle("Confirmation")
            .setMessage("This action will discard all changes made")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(NewNote.this, MainActivity.class));
                }
            })
            .setNegativeButton(android.R.string.no, null).show());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                user.child("notes")
                        .child(String.valueOf(MainActivity.numOfNotes+1))
                        .setValue(noteContent.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(NewNote.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NewNote.this, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewNote.this, "Failed to Save. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}