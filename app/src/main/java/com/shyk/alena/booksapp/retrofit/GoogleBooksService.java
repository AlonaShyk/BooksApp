package com.shyk.alena.booksapp.retrofit;

import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.models.Items;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface GoogleBooksService {

    @GET("volumes/{volumes}")
    Call<BooksVolume> loadBook(@Path("volumes") String id,
                               @Query("key") String apiKey);

    @GET("volumes")
    Call<Items> loadBooksList(@Query("q") String q,
                              @Query("startIndex") String startIndex,
                              @Query("maxResults") String maxResult,
                              @Query("key") String apiKey);


}
