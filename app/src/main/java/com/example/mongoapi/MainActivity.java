package com.example.mongoapi;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mongoapi.heroesAPI.HeroesAPI;
import com.example.mongoapi.model.Heroes;
import com.example.mongoapi.url.Url;
import com.google.gson.Gson;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etname, etdesc;
    private Button btnsave;
    private ImageView imgprofile;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etname = findViewById(R.id.etname);
        etdesc = findViewById(R.id.etdesc);
        imgprofile = findViewById(R.id.imgprofile);
        btnsave = findViewById(R.id.btnsave);
        imgprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void BrowseImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void Save() {

        String name = etname.getText().toString();
        String desc = etdesc.getText().toString();

        Heroes heroes = new Heroes (name,desc);

        Map<String, String>map = new HashMap<>();
        map.put("name" ,name);
        map.put("desc", desc);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroesAPI heroesAPI = retrofit.create(HeroesAPI.class);

        //Call<Void> heroesCall = heroesAPI.addHero(heroes);

        Call<Void> heroesCall = heroesAPI.addHero(map);

        heroesCall.enqueue(new Callback<Void>(){

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT). show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
            }
        }

        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private void previewImage(String imagePath) {

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgprofile.setImageBitmap(myBitmap);
        }
    }

    private String getRealPathFromUri(Uri uri) {

        String[] projection  = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);

        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return  result;
    }


}
