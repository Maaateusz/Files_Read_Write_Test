package com.example.files_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button openImageButton;
    private Button openFileButton;
    private Button saveInternalButton;
    private EditText titleEditText;
    private EditText textEditText;
    private Button saveInternalCacheButton;
    private Button internalStoragePathButton;
    private Button deleteFileButton;
    private EditText deleteFileNameEditText;
    private TextView internalPathTextView;
    private Button goToExternalStorageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sprawdzienie uprawnień
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, 28);
        }

        //XML elements
        openImageButton = (Button) findViewById(R.id.OpenImageButton);
        openFileButton = (Button) findViewById(R.id.OpenFileButton);
        saveInternalButton = (Button) findViewById(R.id.SaveInternalButton);
        saveInternalCacheButton = (Button) findViewById(R.id.SaveCacheButton);
        internalStoragePathButton = (Button) findViewById(R.id.InternalStoragePathButton);
        deleteFileButton = (Button) findViewById(R.id.DeleteFileButton);
        goToExternalStorageButton = (Button) findViewById(R.id.GoToExternalStorageButton);
        titleEditText = (EditText) findViewById(R.id.TitleEditText);
        textEditText = (EditText) findViewById(R.id.TextEditText);
        deleteFileNameEditText = (EditText) findViewById(R.id.DeleteFileNameEditText);
        internalPathTextView = (TextView) findViewById(R.id.InternalPathTextView);

        //OnClick Listeners
        openImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        goToExternalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                Intent intent = new Intent(MainActivity.this, ExternalMemoryActivity.class);
                startActivity(intent);
            }
        });

        saveInternalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToInternalStorage();
            }
        });

        saveInternalCacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToInternalCache();
            }
        });

        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile();
            }
        });

        internalStoragePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInternalStoragePath();
                showFilesList();
            }
        });

        deleteFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 28: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("MainActivity", "Permission Granted");
                } else { //PERMISION DENIED : DISABLE the functionality ...
                    Log.i("MainActivity", "Permission Denied");
                }
            }
        }
    }

    //Operations on files
    public void saveToInternalStorage() {
        String fileName = titleEditText.getText().toString();
        String messageData = textEditText.getText().toString();

        FileOutputStream fos = null;
        //fos = openFileOutput(fileName, MODE_APPEND); dopisanie
        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(messageData.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveToInternalCache() {
        String cacheData = textEditText.getText().toString();

        File cacheDir = getCacheDir();
        File myCacheDir = new File(cacheDir, "myInternalCacheFile.txt");
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myCacheDir);
            fos.write(cacheData.getBytes());
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

    public void readFromInternalCache(){
        StringBuffer stringBuffer = new StringBuffer();

        File cacheDir = getCacheDir();
        File cacheFile = new File(cacheDir, "myInternalCacheFile.txt");
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(cacheFile);
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

        //wypisanie stringBuffer
    }

    public void readFile() {
        String fileName = deleteFileNameEditText.getText().toString();
        StringBuffer strBuffer = new StringBuffer();

        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            int read;
            while ((read = fis.read()) != -1) {
                strBuffer.append((char) read);
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

        titleEditText.setText(fileName);
        textEditText.setText(strBuffer);
    }

    public void showInternalStoragePath() {
        String path = getFilesDir().toString();
        internalPathTextView.setText(path);
    }

    public void showFilesList() {
        String[] fileList = fileList();
        StringBuilder strBuilder = new StringBuilder();

        for (String file : fileList) {
            strBuilder.append(file).append(", ");
        }

        internalPathTextView.setText(strBuilder);
    }

    public void deleteFile() {
        String fileName = deleteFileNameEditText.getText().toString();
        boolean isDeleted = deleteFile(fileName);
        if (isDeleted) {
            Toast.makeText(this, "File Deleted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to Delete!", Toast.LENGTH_LONG).show();
        }
    }
}
