package com.example.android_retrofit;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {
    // GET
    // এখানে MainActivity class এর postIdInput integer value অনুসারে userId এর value change হবে
    // এবং postIdInput অনুসারে Data(ডেটা) show করবে
    @GET("posts")
    Call<List<Post>> getPosts(@Query("userId") Integer userId,
                              @Query("userId") Integer[] userId2,
                              @Query("_sort") String sort,
                              @Query("_order") String order);
    
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> paramaters);

    // এখানে MainActivity class এর postIdInput integer value অনুসারে idNumber এর value change হবে
    // এবং postIdInput অনুসারে Data(ডেটা) show করবে
    @GET("posts/{idNumber}/comments")
    Call<List<Comment>> getComments(@Path("idNumber") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

    // POST
    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("bodyText") String bodyText
    );
    
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

    // Header for each post
    @Headers({"Static-Header1: 451", "Static-Header2: 457"})
    // PUT
    @PUT("posts/{idNumber}")
    Call<Post> updatePutPost(@Header("Dynamic-Header") String header,
                             @Path("idNumber") int postId,
                             @Body Post post);

    // PATCH
    @PATCH("posts/{idNumber}")
    Call<Post> updatePatchPost(@HeaderMap Map<String, String> headers,
                               @Path("idNumber") int postId,
                               @Body Post post);

    // DELETE
    @DELETE("posts/{idNumber}")
    Call<Void> deletePost(@Path("idNumber") int postId);
}
