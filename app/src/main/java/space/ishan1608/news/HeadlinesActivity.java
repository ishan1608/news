package space.ishan1608.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.ArrayList;

public class HeadlinesActivity extends AppCompatActivity implements ArticlesAdapter.OnItemClickedListener {

    private RecyclerView articlesRecyclerView;
    private ArticlesAdapter articlesAdapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        gson = new Gson();

        articlesRecyclerView = findViewById(R.id.articles_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        articlesRecyclerView.setLayoutManager(mLayoutManager);

        // Articles List
        articlesAdapter = new ArticlesAdapter(this);
        articlesRecyclerView.setAdapter(articlesAdapter);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching news...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        NewsProvider newsProvider = NewsProvider.getInstance();
        Promise<ArrayList<Article>, String, Void> articlesPromise = newsProvider.fetchArticles();
        articlesPromise.done(new DoneCallback<ArrayList<Article>>() {
            @Override
            public void onDone(final ArrayList<Article> articles) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayArticles(articles);
                    }
                });
            }
        }).fail(new FailCallback<String>() {
            @Override
            public void onFail(String result) {
                displayError();
            }
        }).always(new AlwaysCallback<ArrayList<Article>, String>() {
            @Override
            public void onAlways(Promise.State state, ArrayList<Article> resolved, String rejected) {
                progressDialog.dismiss();
            }
        });
    }

    private void displayError() {
        Toast.makeText(this, "Error fetching news", Toast.LENGTH_LONG).show();
    }

    private void displayArticles(ArrayList<Article> articles) {
        articlesAdapter.setArticles(articles);
        articlesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Article article) {
        Intent articleDetailsIntent = new Intent(this, ArticleDetailsActivity.class);
        articleDetailsIntent.putExtra("article", gson.toJson(article));
        startActivity(articleDetailsIntent);
    }
}
