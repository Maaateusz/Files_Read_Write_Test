package com.example.files_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.getExternalStoragePublicDirectory;

//Dodać w androidmanifest:
// <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

public class ExternalMemoryActivity extends AppCompatActivity {

    private Button loadExteranlCacheButton;
    private Button saveExteranlCacheButton;
    private Button LoadExternalPrivateStorageButton;
    private Button LoadExternalPublicStorageButton;
    private Button SaveExternalPublicStorageButton;
    private Button SaveExternalPrivateStorageButton;
    private EditText titleExternalEditText;
    private EditText textExternalEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_memory);

        loadExteranlCacheButton = (Button) findViewById(R.id.LoadExteranlCacheButton);
        saveExteranlCacheButton = (Button) findViewById(R.id.SaveExteranlCacheButton);
        LoadExternalPrivateStorageButton = (Button) findViewById(R.id.LoadExternalPrivateStorageButton);
        LoadExternalPublicStorageButton = (Button) findViewById(R.id.LoadExternalPublicStorageButton);
        SaveExternalPublicStorageButton = (Button) findViewById(R.id.SaveExternalPublicStorageButton);
        SaveExternalPrivateStorageButton = (Button) findViewById(R.id.SaveExternalPrivateStorageButton);
        titleExternalEditText = (EditText) findViewById(R.id.TitleExternalEditText);
        textExternalEditText = (EditText) findViewById(R.id.TextExternalEditText);

        loadExteranlCacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromExternalCache();
            }
        });

        saveExteranlCacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToExternalCache();
            }
        });

        LoadExternalPrivateStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromExternalPrivateStorage();
            }
        });

        LoadExternalPublicStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromExternalPublicStorage();
            }
        });

        SaveExternalPublicStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToExternalPublicStorage();
            }
        });

        SaveExternalPrivateStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToExternalPrivateStorage();
            }
        });

    }

    public void saveToExternalCache(){
        String cacheData = textExternalEditText.getText().toString();

        File cacheDir = getCacheDir();
        File myCacheDir = new File(cacheDir, "myExternalCacheFile.txt");
        writeToFile(myCacheDir, cacheData);
    }

    public void loadFromExternalCache(){
        File cacheDir = getCacheDir();
        File cacheFile = new File(cacheDir, "myExternalCacheFile.txt");

        textExternalEditText.setText(loadFromFile(cacheFile));
    }

    public void saveToExternalPrivateStorage(){
        String data = textExternalEditText.getText().toString();
        if(isExternalStorageWritable()) {
            File dir = getExternalFilesDir("MyFolder");
            File file = new File(dir, "myExternalPrivate.txt");
            writeToFile(file, data);
        } else {
            Toast.makeText(this, "External media Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveToExternalPublicStorage(){
        String data = textExternalEditText.getText().toString();
        if(isExternalStorageWritable()) {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // android:requestLegacyExternalStorage="true" POTRZEBNE!!!!!!!!!!!!!
            File file = new File(dir, "myExternalPublic.txt");
            writeToFile(file, data);
        } else {
            Toast.makeText(this, "External media Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFromExternalPrivateStorage(){
        if(isExternalStorageReadable()){
            File dir = getExternalFilesDir("MyFolder");
            File file = new File(dir, "myExternalPrivate.txt");
            textExternalEditText.setText(loadFromFile(file));
        } else {
            Toast.makeText(this, "External media Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFromExternalPublicStorage(){
        if(isExternalStorageReadable()){
            File dir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir, "myExternalPublic.txt");
            textExternalEditText.setText(loadFromFile(file));
        } else {
            Toast.makeText(this, "External media Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteExternalData(){

    }

    //Metody Ogólne
    public void writeToFile(File file, String data){
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try{
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadFromFile(File file){
        StringBuffer stringBuffer = new StringBuffer();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            int read;
            while ((read = fis.read()) != -1) {
                stringBuffer.append((char) read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

    //Check is external storage is available to read and write
    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    //Check is external storage is available to at least read
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }
}
