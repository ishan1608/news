package space.ishan1608.news;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewsProvider {
    private static volatile NewsProvider instance;

    private OkHttpClient client;
    private Gson gson;

    public NewsProvider() {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static NewsProvider getInstance() {
        if (instance == null) {
            synchronized (NewsProvider.class) {
                if (instance == null) {
                    instance = new NewsProvider();
                    instance.client = new OkHttpClient();
                    instance.gson = new Gson();
                }
            }
        }
        return instance;
    }

    public Promise<ArrayList<Article>, String, Void> fetchArticles() {
        final Deferred<ArrayList<Article>, String, Void> deferred = new DeferredObject<>();
        Request request = new Request.Builder()
                .url("https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=995fbd2e4dc345558cf2b4e131596467")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                deferred.reject(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            JSONObject responseJson = new JSONObject(responseBody.string());
                            ArrayList<Article> articles = gson.fromJson(responseJson.getJSONArray("articles").toString(), new TypeToken<ArrayList<Article>>() {}.getType());
                            deferred.resolve(articles);
                        } catch (JSONException|IOException e) {
                            e.printStackTrace();
                            deferred.reject("Response Invalid");
                        }
                    } else {
                        deferred.reject("Response null");
                    }
                } else {
                    deferred.reject("Response unsuccessful");
                }
            }
        });
        return deferred.promise();
    }
}
