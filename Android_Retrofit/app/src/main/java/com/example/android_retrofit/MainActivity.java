package com.example.android_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int postIdInput, i, j;
    private EditText editText;
    private TextView textView;
    private String rootUrl = "https://jsonplaceholder.typicode.com/";
    private String iotaitUrl = "https://api.jsonbin.io/";
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Double interestRateArray[];
    private Double descendingInterestRate[] = new Double[6];
    private int arrayLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = findViewById(R.id.outputTextId);
        editText = findViewById(R.id.postIdEditTextID);

        // Gson object
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "ABC")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        // Retrofit Interface
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(rootUrl)
                .baseUrl(iotaitUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        getIotaItTestApiData();
    }

    public void getIotaItTestApiData(){
        Call<IotaItTestApiList> call = jsonPlaceHolderApi.getTestData();
        call.enqueue(new Callback<IotaItTestApiList>() {
            @Override
            public void onResponse(Call<IotaItTestApiList> call, Response<IotaItTestApiList> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }

                List<IotaItTestAPI> dataList = response.body().getData();
                for (IotaItTestAPI iotaItTestAPI : dataList) {
                    arrayLength++;
                }

                interestRateArray = new Double[arrayLength];
                descendingInterestRate = new Double[arrayLength];
                i=0;
                for (IotaItTestAPI iotaItTestAPI : dataList) {
                    interestRateArray[i] = iotaItTestAPI.getInterestRate();
                    i++;
                }

                j=0;
                Arrays.sort(interestRateArray);
                for (i = interestRateArray.length - 1; i >= 0; i--) {
                    descendingInterestRate[j] = interestRateArray[i];
                    j++;
                }

                textView.append("Descending order :\n-----------------------------\n\n");
                for(j=0; j<descendingInterestRate.length; j++) {
                    for (IotaItTestAPI iotaItTestAPI : dataList) {
                        if (iotaItTestAPI.getInterestRate() == descendingInterestRate[j]) {
                            String content = "";
                            content += "Name: " + iotaItTestAPI.getName() + "\n";
                            content += "Principle amount: " + iotaItTestAPI.getPrincipalAmount() + "\n";
                            content += "Interest rate: " + iotaItTestAPI.getInterestRate() + "\n";
                            content += "Tenure in month: " + iotaItTestAPI.getTenureInMonth() + "\n\n";
                            textView.append(content);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<IotaItTestApiList> call, Throwable t) {
                Log.d("Response failure ", t.getMessage());
            }
        });
    }

    public void enterButtonMethod(View view){
        if(!editText.getText().toString().equals("")){
            textView.setText("");
            postIdInput = Integer.parseInt(editText.getText().toString());
            getElementsOfPost(postIdInput);
//            getElementsOfComment(postIdInput);
//            createPost(postIdInput);
//            updatePost(postIdInput);
//            deletePost(postIdInput);
        } else {
            editText.setError("Please enter post id");
        }
    }

    public void getElementsOfPost(int postIdInput){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", Integer.toString(postIdInput));
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        // getPosts() method from "Post" model class
        // show data in descending order according to id sort
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts("id", "desc");
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,6,40}, null, null);
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
        // call.execute() method টি main thread এ ব্যবহার করা যাবে না, কারণ app ফ্রিজ হয়ে Exception থ্রো করবে
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }
                
                List<Post> posts = response.body();
                for(Post post: posts){
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Body text: " + post.getBodyText() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    public void getElementsOfComment(int postIdInput){
        // getComments() method from "Comment" model class
        // এখানে postIdInput integer value অনুসারে JsonPlaceHolderApi class এর idNumber এর value change হবে
        // এবং postIdInput অনুসারে Data(ডেটা) show করবে
        // Here data will be retrieved according to postIdInput (user input to Api)
//        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(postIdInput);
        Call<List<Comment>> call = jsonPlaceHolderApi
                .getComments("posts/" + postIdInput + "/comments");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    textView.setText("code: " + response.code());
                    return;
                }

                List<Comment> comments = response.body();
                for(Comment comment: comments){
                    String content = "";
                    content += "Post Id: " + comment.getPostId() + "\n";
                    content += "Id: " + comment.getId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Body: " + comment.getBodyText() + "\n\n";
                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void createPost(int postIdInput){
        Post post = new Post(postIdInput, "New Title", "New Text");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", Integer.toString(postIdInput));
        fields.put("title", "New Title From Map");

//        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textView.setText("code: " + response.code());
                    return;
                }
                
                Post postResponse = response.body();
                String content = "";
                content += "code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Body text: " + postResponse.getBodyText() + "\n\n";
                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void updatePost(int postIdInput) {
        Post post = new Post(13, null, "New Text");

        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "def");
        headers.put("Map-Header2", "ghi");

        Call<Post> call = jsonPlaceHolderApi.updatePatchPost(headers, postIdInput, post);
//        Call<Post> call = jsonPlaceHolderApi.updatePutPost("Header Post", postIdInput, post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textView.setText("code: " + response.code());
                    return;
                }

                Post postResponse = response.body();
                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Body text: " + postResponse.getBodyText() + "\n\n";
                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void deletePost(int postIdInput){
        Call<Void> call = jsonPlaceHolderApi.deletePost(postIdInput);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textView.setText("code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}
