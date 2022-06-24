package com.example.cupidsarrow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Service;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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

                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.app_name);
                    String description = "Very Serious";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("2", name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                sendnotification( MainActivity.this,"Hello There!", "General Kenobi");

                //Log.d("AFTER N", "AFTER");
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
                   verify(view2, text1, text2, name, pass);

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



    public void verify(View view, String user_input, String pass_input, String message, String pass) {

        //Log.d("message", message);
        //Log.d("user input", user_input);
        //Log.d("pass", pass);
        //Log.d("pass input", pass_input);
        if (message.equals(user_input) && pass.equals(pass_input)) {
            Intent z = new Intent(this, LoveService.class);
            z.putExtra("user", message);
            startService(z);

            Intent i = new Intent(getApplicationContext(), HomeScreen.class);
            i.putExtra("user", message);
            startActivity(i);
        }
    }

    public static void sendnotification(Context context, String title, String content) {
        //Log.d("BEFORE N", "BEFORE");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(com.google.firebase.messaging.R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(17, builder.build());

    }
}