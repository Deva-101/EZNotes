package com.example.eznotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eznotes.models.NotesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password, user_name;
    private Button registerButton;
    private TextView textLogin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register);
        textLogin = findViewById(R.id.text_login);
        user_name = findViewById(R.id.user_name);

        registerButton.setOnClickListener(view ->
                Register());

        textLogin.setOnClickListener(view ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void Register() {
        String u_name = user_name.getText().toString().trim();
        String u_email = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (!u_email.contains("@") || !u_email.contains(".")){
            email.setError("Please enter a valid Email.");
        } else {
            if (u_name.isEmpty()) {
                user_name.setError("Username cannot be empty");
            } else if (pass.isEmpty()) {
                password.setError("Password cannot be empty");
            } else {
                mAuth.createUserWithEmailAndPassword(u_email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference myRef = database.getReference(Objects.requireNonNull(mAuth.getUid()));
                        createProfile(u_name, myRef);
                        startActivity((new Intent(RegisterActivity.this, MainActivity.class)));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed: " +
                                Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void createProfile(String name, DatabaseReference user){
        NotesModel notesModel = new NotesModel();
        List<String> default_profile = new ArrayList<>();
        default_profile.add("This is your first note! Tap here to edit this note or tap the \"+\" to create a new note!");
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());

        notesModel.setName(name);
        notesModel.setDate(timeStamp);
        notesModel.setNotes(default_profile);

        user.setValue(notesModel).addOnSuccessListener(unused ->
            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
            Toast.makeText(RegisterActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.back){
            setContentView(R.layout.activity_login);
        }
        return super.onOptionsItemSelected(item);
    }
}