package space.ishan1608.news;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private ArrayList<Article> articles;
    private OnItemClickedListener onItemClickedListener;

    public ArticlesAdapter(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.headline_article_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Article article = articles.get(i);
        viewHolder.articleTitleTextView.setText(article.getTitle());
        viewHolder.articleDescriptionTextView.setText(article.getDescription());
        viewHolder.articleAuthorTextView.setText(article.getAuthor());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListener.onItemClicked(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articles == null) {
            return 0;
        }
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView articleTitleTextView, articleAuthorTextView, articleDescriptionTextView;
        public View articleContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleTitleTextView = itemView.findViewById(R.id.article_title_text_view);
            articleDescriptionTextView = itemView.findViewById(R.id.article_description_text_view);
            articleAuthorTextView = itemView.findViewById(R.id.article_author_text_view);
            articleContainer = itemView;
        }
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public interface OnItemClickedListener {
        void onItemClicked(Article article);
    }
}
