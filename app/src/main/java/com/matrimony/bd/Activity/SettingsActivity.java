package com.matrimony.bd.Activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matrimony.bd.Activity.tutorial.TutorialActivity;
import com.matrimony.bd.Authentication.SignInActivity;
import com.matrimony.bd.R;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CardView rate, share, feedback, status,contact;
    private TextView statusText;
    GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fab = findViewById(R.id.floating_action_button);
        rate = findViewById(R.id.rate);
        share = findViewById(R.id.share);
        feedback = findViewById(R.id.feedback);
        contact = findViewById(R.id.contact);
        status = findViewById(R.id.status);
        statusText = findViewById(R.id.status_text);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("Comming soon..." + getPackageName())));
                }
            }
        });

      share.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String appPackageName = getPackageName();

              String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
              ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
              ClipData clip = ClipData.newPlainText("", link);
              clipboard.setPrimaryClip(clip);
              Toast.makeText(SettingsActivity.this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();

          }
      });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(intent);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "habibur.rahman.app123@gmail.com"));

                try {
                    startActivity(Intent.createChooser(feedback, "Choose an e-mail client"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SettingsActivity.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth. getCurrentUser();
                if (currentUser!=null){
                    revokeAccess();
                }else{
                    Intent signIn = new Intent(SettingsActivity.this, SignInActivity.class);
                    startActivity(signIn);
                    finish();
                }
            }
        });
    }

    //Recreate activity

    private void restartApp() {
        Intent i = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(i);
        finish();
    }

    private void revokeAccess() {
        FirebaseAuth.getInstance().signOut();

        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            statusText.setText("লগ আউট");
        }else{
            statusText.setText("লগ ইন");
        }
    }
}