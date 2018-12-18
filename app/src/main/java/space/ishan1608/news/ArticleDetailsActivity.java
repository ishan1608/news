package space.ishan1608.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        TextView articleDetailsTitleTextView = findViewById(R.id.article_details_title_text_view);
        TextView articleDetailsDescriptionTextView = findViewById(R.id.article_details_description_text_view);
        TextView articleDetailsAuthorTextView = findViewById(R.id.article_details_author_text_view);


    }
}
