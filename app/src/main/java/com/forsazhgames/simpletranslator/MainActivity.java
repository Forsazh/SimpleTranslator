package com.forsazhgames.simpletranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String URL = "https://translate.yandex.net";
    private final String KEY = "trnsl.1.1.20161025T112004Z.1d184eddbf0c8425.f21fc6fcb6d54f55793c15a29d0e0dc6d3d78345";
    private final String EN_RU = "en-ru";
    private final String RU_EN = "ru-en";

    private EditText forTranslateET;
    private TextView translatedTV;
    private Button enRuButton;
    private Button ruEnButton;

    private boolean wasTouch;
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL).build();

    private YandexTranslateService service = retrofit.create(YandexTranslateService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        forTranslateET = (EditText) findViewById(R.id.for_translate);
        forTranslateET.setOnClickListener(this);
        translatedTV = (TextView) findViewById(R.id.translated);
        enRuButton = (Button) findViewById(R.id.en_ru_btn);
        enRuButton.setOnClickListener(this);
        ruEnButton = (Button) findViewById(R.id.ru_en_btn);
        ruEnButton.setOnClickListener(this);
    }

    private void tryToTranslate(String lang) {
        Map<String, String> mapJson = new HashMap<>();
        mapJson.put("key", KEY);
        mapJson.put("text", forTranslateET.getText().toString());
        mapJson.put("lang", lang);

        service.translate(mapJson).enqueue(new Callback<TranslatedResponse>() {
            @Override
            public void onResponse(Call<TranslatedResponse> call, Response<TranslatedResponse> response) {
                TranslatedResponse translatedResponse = response.body();
                String result = "";
                for (String s : translatedResponse.getText()) {
                    result += s;
                }
                translatedTV.setText(result);
            }

            @Override
            public void onFailure(Call<TranslatedResponse> call, Throwable t) {
                translatedTV.setText(R.string.error_response);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.for_translate:
                if (!wasTouch) {
                    forTranslateET.setText("");
                    wasTouch = true;
                }
                break;
            case R.id.en_ru_btn:
                tryToTranslate(EN_RU);
                break;
            case R.id.ru_en_btn:
                tryToTranslate(RU_EN);
                break;
        }
    }
}