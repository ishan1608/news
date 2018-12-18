package space.ishan1608.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.ArrayList;

public class HeadlinesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching news...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        NewsProvider newsProvider = NewsProvider.getInstance();
        Promise<ArrayList<Article>, String, Void> articlesPromise = newsProvider.fetchArticles();
        articlesPromise.done(new DoneCallback<ArrayList<Article>>() {
            @Override
            public void onDone(ArrayList<Article> articles) {
                Log.e("TEST", "Downloaded Articles");
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
}
