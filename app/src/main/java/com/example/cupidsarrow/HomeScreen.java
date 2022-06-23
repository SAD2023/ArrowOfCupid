package com.example.cupidsarrow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class HomeScreen extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        // getting the user's name
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("user");


        db.collection("partners").document(user).get().addOnCompleteListener(result -> {
            if (result.isSuccessful()){
                partner = result.getResult().get("partner").toString();
                Log.d("PARTNER", partner);
            }
        });

        // getting any outstanding kisses
        db.collection("incoming").whereEqualTo("For", user).get().addOnCompleteListener(result ->
        {
            if (result.isSuccessful() && (result.getResult().getDocuments().size() != 0)){

                DocumentSnapshot doc = result.getResult().getDocuments().get(0);
                String from = doc.get("From").toString();
                String is_for = doc.get("For").toString();
                MainActivity.sendnotification(HomeScreen.this, is_for + ", you have a new kiss!", "From " + from);

                db.collection("incoming").document(user)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ERROR", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR", "Error deleting document", e);
                            }
                        });

            }

            else {
                Log.d("ERROR!", "Error getting documents: ", result.getException());
            }
        });

        // what happens when someone sends a kiss
        Button button = (Button) findViewById(R.id.kissbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Map<String, Object> kiss = new HashMap<>();
                kiss.put("For", partner);
                kiss.put("From", user);

                db.collection("incoming").document(partner)
                        .set(kiss)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ERROR", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR", "Error writing document", e);
                            }
                        });


                MainActivity.sendnotification(HomeScreen.this, "Sent a kiss for ", partner + "!");


            }});
    }
}