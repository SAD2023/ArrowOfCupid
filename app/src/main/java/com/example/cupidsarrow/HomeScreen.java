package com.example.cupidsarrow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeScreen extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        db.collection("incoming").whereEqualTo("For", "Kayla").get().addOnCompleteListener(result ->
        {
            if (result.isSuccessful() && (result.getResult().getDocuments().size() != 0)){

                DocumentSnapshot doc = result.getResult().getDocuments().get(0);
                String from = doc.get("From").toString();
                String is_for = doc.get("For").toString();
                MainActivity.sendnotification(HomeScreen.this, is_for + ", you have a new kiss!", "From " + from);

            }

            else {
                Log.d("ERROR!", "Error getting documents: ", result.getException());
            }
        });

        Button button = (Button) findViewById(R.id.kissbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.sendnotification(HomeScreen.this, "Among", "Us");

            }});
    }
}