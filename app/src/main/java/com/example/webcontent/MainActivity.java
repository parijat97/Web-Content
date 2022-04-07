package com.example.webcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> html = new ArrayList<>();
    TextView text,url;
    Button Go;
    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String out = "done";
            URL url;
            HttpURLConnection urlConnection;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                Scanner scan = new Scanner(reader);
                while(scan.hasNext()){
                    html.add(scan.nextLine());
                }
                return out;
            }catch (Exception e){
                return e.toString();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadTask downloadTask = new DownloadTask();

        text = findViewById(R.id.textV);
        url = findViewById(R.id.url);
        Go = findViewById(R.id.go);
        ListView listView = findViewById(R.id.List);
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!url.getText().toString().equals("")){
                    String temp = url.getText().toString();
                    url.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    Go.setVisibility(View.INVISIBLE);
                    try{
                        String result = downloadTask.execute(temp).get();
                        if(result.equals("done")) {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, html);
                            listView.setAdapter(arrayAdapter);
                        }else{
                            ArrayList<String> temp1 = new ArrayList<>();
                            temp1.add(result);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,temp1);
                            listView.setAdapter(arrayAdapter);
                        }
                    }catch(Exception e){
                        ArrayList<String> temp1 = new ArrayList<>();
                        temp1.add(e.toString());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,temp1);
                        listView.setAdapter(arrayAdapter);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Enter the URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}