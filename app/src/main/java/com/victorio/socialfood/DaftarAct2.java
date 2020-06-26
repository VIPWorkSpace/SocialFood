package com.victorio.socialfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.victorio.socialfood.R.id.photo_home_user;

public class DaftarAct2 extends AppCompatActivity {
    ImageView photo_profile;
    EditText alamat_lengkap, rt_rw, kelurahan, kecamatan, kota, petunjuk_rumah, nama_lengkap;
    Button btn_register2, btn_add_photo;
    Uri photo_location;
    Integer photo_max = 1;
    DatabaseReference reference;
    StorageReference storage;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar2);

        getUsernameLocal();


        nama_lengkap = findViewById(R.id.nama_lengkap);
        photo_profile = findViewById(photo_home_user);
        alamat_lengkap = findViewById(R.id.alamat);
        rt_rw = findViewById(R.id.rt_rw);
        kelurahan = findViewById(R.id.kelurahan);
        kecamatan = findViewById(R.id.kecamatan);
        kota = findViewById(R.id.kota);
        petunjuk_rumah = findViewById(R.id.petunjuk);
        btn_register2 = findViewById(R.id.btn_regiser2);
        btn_add_photo = findViewById(R.id.btn_add_photo);

        //button menambahkan photo
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });


        btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_register2.setEnabled(false);
                btn_register2.setText("Loading...");
                final String xalamat_lengkap = alamat_lengkap.getText().toString();
                final String xrt_rw = rt_rw.getText().toString();
                final String xkelurahan = kelurahan.getText().toString();
                final String xkecamatan = kecamatan.getText().toString();
                final String xkota = kota.getText().toString();
                final String xpetunjuk_rumah = petunjuk_rumah.getText().toString();
                final String xnama_lengkap = nama_lengkap.getText().toString();

                if (xalamat_lengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Alamat lengkap tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                }else if (xnama_lengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama lengkap harus diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                }
                else if (xrt_rw.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "RT dan RW tidak boleh kosong ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else if (photo_location == null) {
                    Toast.makeText(getApplicationContext(), "Foto wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else if (xkelurahan == null) {
                    Toast.makeText(getApplicationContext(), "Kelurahan wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else if (xkecamatan == null) {
                    Toast.makeText(getApplicationContext(), "Kecamatan wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else if (xkota == null) {
                    Toast.makeText(getApplicationContext(), "Kota wajib diisi ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else if (xpetunjuk_rumah == null) {
                    Toast.makeText(getApplicationContext(), "Berikan petunjuk rumah ! ", Toast.LENGTH_SHORT).show();
                    btn_register2.setText("CONTINUE");
                    btn_register2.setEnabled(true);
                } else {
                    //menyimpan kepada database
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                    storage = FirebaseStorage.getInstance().getReference().child("PhotoUser").child(username_key_new);

                    //validasi file apakah ada
                    if (photo_location != null) {
                        final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                        storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //mengambil url foto dari firebase
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri_photo = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        reference.getRef().child("alamat").setValue(alamat_lengkap.getText().toString());
                                        reference.getRef().child("rt_rw").setValue(rt_rw.getText().toString());
                                        reference.getRef().child("kelurahan").setValue(kelurahan.getText().toString());
                                        reference.getRef().child("kecamatan").setValue(kecamatan.getText().toString());
                                        reference.getRef().child("kota").setValue(kota.getText().toString());
                                        reference.getRef().child("petunjuk_rumah").setValue(petunjuk_rumah.getText().toString());
                                        reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        //pindah activity
                                        Intent abc = new Intent(DaftarAct2.this, SuccessRegisterAct.class);
                                        startActivity(abc);
                                        finish();
                                    }
                                });


                            }
                        });
                    }
                }
            }
        });

    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //membuat method mengambil foto
    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).noFade().centerCrop().fit().into(photo_profile);

        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
