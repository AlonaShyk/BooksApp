package com.shyk.alena.booksapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.Items;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleBooksRestClient {
    private static GoogleBooksRestClient instance;
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";
    private final GoogleBooksService googleBooksService;

    private GoogleBooksRestClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        googleBooksService = retrofit.create(GoogleBooksService.class);
    }

    public static synchronized GoogleBooksRestClient getInstance() {
        if (instance == null)
            instance = new GoogleBooksRestClient();
        return instance;
    }

    public void getBook(String id, final RetrofitListener callback) {
        Call<BooksVolume> call = googleBooksService.loadBook(id, GoogleBooksConfig.getApiKey());
        call.enqueue(new Callback<BooksVolume>() {
            @Override
            public void onResponse(Call<BooksVolume> call, Response<BooksVolume> response) {
                if (response.isSuccessful()) {
                    BooksVolume booksVolume = response.body();
                    callback.onBook(booksVolume);
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<BooksVolume> call, Throwable t) {
                callback.onError();
                t.printStackTrace();
            }
        });

    }

    public void getBookList(String searchKeyWords,
                            final RetrofitListener callback,
                            String startIndex,
                            String maxResult) {
        Call<Items> call = googleBooksService.loadBooksList(searchKeyWords, startIndex, maxResult, GoogleBooksConfig.getApiKey());
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if (response.isSuccessful()) {
                    List<BooksVolume> items = response.body().getBooksVolumes();
                    callback.onList(items);
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


}
