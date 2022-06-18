package com.example.cupidsarrow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                TextInputLayout textInputLayout = findViewById(R.id.input1);
                String text1 = textInputLayout.getEditText().getText().toString();

                TextInputLayout textInputLayout2 = findViewById(R.id.input2);
                String text2 = textInputLayout2.getEditText().getText().toString();


                View view = findViewById(android.R.id.content).getRootView();


//                DocumentReference ds = db.collection("usernames").document("Sadman");

                db.collection("usernames").whereEqualTo("name", text1).get().addOnCompleteListener(result ->
                {
                   if (result.isSuccessful() && (result.getResult().getDocuments().size() != 0)){

                   DocumentSnapshot doc = result.getResult().getDocuments().get(0);
                   String name = doc.get("name").toString();
                   String pass = doc.get("pass").toString();
                   View view2 = findViewById(android.R.id.content).getRootView();
                   sendMessage(view2, text1, text2, name, pass);

                   }

                   else {
                       Log.d("ERROR!", "Error getting documents: ", result.getException());
                   }
                });

//                ds.get().addOnCompleteListener(result -> {
//                    Log.d("BEFORE", "BEFORE");
//                    String name = result.getResult().get("name").toString();
//                    String pass = result.getResult().get("pass").toString();
//                    Log.d("NAME", name);
//                    Log.d("PASS", pass);
//                    View view2 = findViewById(android.R.id.content).getRootView();
//                    sendMessage(view2, text1, text2, name, pass);
//                });
//                Log.d("AFTER", "AFTER");



            }
        });

    }



    public void sendMessage(View view, String user_input, String pass_input, String message, String pass) {

        Log.d("message", message);
        Log.d("user input", user_input);
        Log.d("pass", pass);
        Log.d("pass input", pass_input);
        if (message.equals(user_input) && pass.equals(pass_input)) {
            Intent i = new Intent(getApplicationContext(), HomeScreen.class);
           startActivity(i);
        }
    }
}