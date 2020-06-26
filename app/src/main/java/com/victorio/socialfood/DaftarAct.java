package com.victorio.socialfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DaftarAct extends AppCompatActivity {
    Button btn_register;
    EditText btn_username, btn_password, btn_email;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        btn_username = findViewById(R.id.username);
        btn_password = findViewById(R.id.password);
        btn_email = findViewById(R.id.email);
        btn_register = findViewById(R.id.btn_regiser);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_register.setText("LOADING");
                btn_register.setEnabled(false);

                final String username = btn_username.getText().toString();
                final String password = btn_password.getText().toString();
                final String email_address = btn_email.getText().toString();


                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username Kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_register.setText("CONTINUE");
                    btn_register.setEnabled(true);
                } else {
                    if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Password Kosong ! ", Toast.LENGTH_SHORT).show();
                        btn_register.setText("CONTINUE");
                        btn_register.setEnabled(true);
                    }
                    if (email_address.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Email Kosong ! ", Toast.LENGTH_SHORT).show();
                        btn_register.setText("CONTINUE");
                        btn_register.setEnabled(true);
                    } else {
                        //Menyimpan data kepada local storage
                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(username_key, btn_username.getText().toString());
                        editor.apply();
//
//                //Simpan kepada database
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(btn_username.getText().toString());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().child("username").setValue(btn_username.getText().toString());
                                dataSnapshot.getRef().child("password").setValue(btn_password.getText().toString());
                                dataSnapshot.getRef().child("email_address").setValue(btn_email.getText().toString());
                                dataSnapshot.getRef().child("user_dompet").setValue(40000);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Intent lanjut = new Intent(DaftarAct.this, DaftarAct2.class);
                        startActivity(lanjut);
                        finish();

                    }
                }
            }
        });
    }
}
