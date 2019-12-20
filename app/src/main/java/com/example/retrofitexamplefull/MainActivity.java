package com.example.retrofitexamplefull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.retrofitexamplefull.model.Comment;
import com.example.retrofitexamplefull.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
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

    TextView textView;
    List<Post> postList = new ArrayList<>();

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view_result);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor  = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Add headers manually
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header","xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                //End of header adding manually
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Log.d("!!!", "onCreate: " + jsonPlaceHolderApi);

    //    getPosts();
    //    getComments();
    //    CreatePost();
        updatePost();
        //    deletePost();
    }

    public void getPosts(){

        Map<String,String>  parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");
    //    Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[] {2,3,6} ,"id",null);        // userid:4,sort : id ,order: desc

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if( !response.isSuccessful()){
                    textView.setText("Code : " + response.code());
                }

                List<Post> posts = response.body();
                Log.d( "!!!",posts.toString());
                for (Post post : posts){
                    String content = " " ;
                    content += "ID: " + post.getId() + "\n";
                    content += "User Id: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    textView.append(content);
                }
               /* for(int i = 0 ; i < posts.size() ; i++){
                    postList.add( new Post(posts.get(i).getId(),posts.get(i).getUserId(),posts.get(i).getTitle(),posts.get(i).getText()));
                    Log.d("!!!",posts.get(i).getId());
                }*/
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    public void getComments(){
        Call<List<Comment>> call = jsonPlaceHolderApi.getComment(3);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if( !response.isSuccessful()){
                    textView.setText(response.code());
                }
                List<Comment> comments = response.body();

                for(Comment comment : comments){
                    String content = " " ;
                    content += "Post Id: " + comment.getPostId() + "\n";
                    content += "Id: " + comment.getId() + "\n";
                    content += "Name: " + comment.getName() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";
                    textView.append(content);

                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textView.setText(t.getMessage());

            }
        });
    }
    public void CreatePost(){
    //    Post post = new Post(23,"New Title","New Text");

    //    Call<Post> call = jsonPlaceHolderApi.createPost(post);
    //    Call<Post> call = jsonPlaceHolderApi.createPost(23,"New Title","New Text");
        Map<String,String> fields = new HashMap<>();
        fields.put("userId","23");
        fields.put("title", "New Title");
        fields.put("body","New Text");
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code:"  + response.code());
                }

                Post posts = response.body();
                Log.d( "!!!",posts.toString());

                    String content = " " ;
                content += "Code: " + response.code() + "\n";
                    content += "ID: " + posts.getId() + "\n";
                    content += "User Id: " + posts.getUserId() + "\n";
                    content += "Title: " + posts.getTitle() + "\n";
                    content += "Text: " + posts.getText() + "\n\n";
                    textView.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());

            }
        });
    }

    public void updatePost(){
        Post post = new Post(12,null,"New Text");

        Map<String,String> headers =new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghi");

     //   Call<Post> call = jsonPlaceHolderApi.putPost("abc",5,post);
        Call<Post> call = jsonPlaceHolderApi.patchPost(headers,5,post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code:"  + response.code());
                }

                Post posts = response.body();
                Log.d( "!!!",posts.toString());

                String content = " " ;
                content += "Code: " + response.code() + "\n";
                content += "ID: " + posts.getId() + "\n";
                content += "User Id: " + posts.getUserId() + "\n";
                content += "Title: " + posts.getTitle() + "\n";
                content += "Text: " + posts.getText() + "\n\n";
                textView.append(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());

            }
        });
    }

    public void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textView.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}
