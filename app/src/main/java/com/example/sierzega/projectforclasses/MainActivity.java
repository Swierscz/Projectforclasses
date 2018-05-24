package com.example.sierzega.projectforclasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private NumberHolder numberHolder;
    private ArrayAdapter<String> arrayAdapter;

    public static final String BASE_URL = "http://192.168.42.229:8080/";
    Retrofit retrofit;
    MyApiEndpointInterface apiService;


    EditText amountOfNumbersToGenerate, edtTxtAvgValue;
    Button btnGenerateNumbers, btnCalculateAvgValue;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService =  retrofit.create(MyApiEndpointInterface.class);

        numberHolder = NumberHolder.getInstance();

        initializeViewComponents();


        btnGenerateNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        numberHolder.fillListWithData(getRandomNumbersFromServer());
                        updateListOfNumbersWithGeneratedValues();
                    }
                });
                thread.start();
            }
        });

        btnCalculateAvgValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        apiService.sendNumbersValues(numberHolder.getListOfIntegers()).enqueue(new Callback<List<Integer>>() {
                            @Override
                            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                                getAvgValueFromServerAndUpdateTextField();
                            }
                            @Override
                            public void onFailure(Call<List<Integer>> call, Throwable t) {
                                Log.i(TAG, "Sending numbers failed");
                            }
                        });
                    }
                });
                thread.start();
            }
        });


    }

    private void updateListOfNumbersWithGeneratedValues() {
        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, ListConversionUtils.convertIntegersToStringsInList(numberHolder.getListOfIntegers()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(arrayAdapter);
            }
        });
    }

    private void initializeViewComponents() {
        btnGenerateNumbers = (Button) findViewById(R.id.btnGenerateNumbers);
        btnCalculateAvgValue = (Button) findViewById(R.id.btnCalculateAvgValue);
        listView = (ListView) findViewById(R.id.lvListOfNumbers);
        amountOfNumbersToGenerate = (EditText) findViewById(R.id.edtTxtAmountOfNumbersToGenerate);
        edtTxtAvgValue = (EditText) findViewById(R.id.edtTxtAvgValue);
    }

    public List<Integer> getRandomNumbersFromServer() {
        Call<List<Integer>> callWithListOfNumbers = apiService.getRandomNumbers(Integer.parseInt(amountOfNumbersToGenerate.getText().toString()));
        List<Integer> tempList;
        try {
            tempList = callWithListOfNumbers.execute().body();
            return tempList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void getAvgValueFromServerAndUpdateTextField() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Call<Integer> callValue = apiService.getAvgValue();
                int i = 0;
                try {
                    i = callValue.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edtTxtAvgValue.setText("" + finalI);
                    }
                });
            }
        });
        thread.start();
    }
}
