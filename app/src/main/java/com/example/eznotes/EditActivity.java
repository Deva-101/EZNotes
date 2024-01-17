package com.example.eznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference(Objects.requireNonNull(mAuth.getUid()));

        EditText noteContent = findViewById(R.id.noteContents);
        Button back = findViewById(R.id.back);
        Button delete = findViewById(R.id.delete);
        Button save = findViewById(R.id.save);

        if (!MainActivity.note_str.equals("This is your first note! Tap here to edit this note or tap the \"+\" to create a new note!"))
        noteContent.setText(MainActivity.note_str);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.note_str.equals(noteContent.getText().toString())){
                    new AlertDialog.Builder(noteContent.getContext())
                        .setTitle("Confirmation")
                        .setMessage("This action will discard all changes made")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(EditActivity.this, MainActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                } else {
                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                }
            }
        });

//        back.setOnClickListener(view ->
//                new AlertDialog.Builder(noteContent.getContext())
//                .setTitle("Confirmation")
//                .setMessage("This action will discard all changes made")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivity(new Intent(EditActivity.this, MainActivity.class));
//                    }
//                })
//                .setNegativeButton(android.R.string.no, null).show());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new AlertDialog.Builder(noteContent.getContext())
                            .setTitle("This action cannot be undone")
                            .setMessage("Are your sure your want to delete this note?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    user.child("notes").child(String.valueOf(MainActivity.id_index)).removeValue().addOnSuccessListener(unused ->
                                            Toast.makeText(EditActivity.this, "Successfully deleted note", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                                            Toast.makeText(EditActivity.this, "Failed to delete. Please try again later.", Toast.LENGTH_SHORT).show());
                                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();


            }
        });

        save.setOnClickListener(view -> {
            // Write a message to the database
            user.child("notes")
                .child(String.valueOf(MainActivity.id_index))
                .setValue(noteContent.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditActivity.this, MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        noteContent.setError("An error occurred while saving... Please try again.");
                        Toast.makeText(EditActivity.this, "Failed to save. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        });

    }
}