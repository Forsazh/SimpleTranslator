package com.forsazhgames.simpletranslator;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Forsazhrus on 25.10.2016.
 */
public interface YandexTranslateService {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<TranslatedResponse> translate(@FieldMap Map<String, String> mapJson);
}
