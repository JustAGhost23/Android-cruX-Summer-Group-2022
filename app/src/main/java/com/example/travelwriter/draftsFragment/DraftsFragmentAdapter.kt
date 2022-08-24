package com.example.travelwriter.draftsFragment

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelwriter.database.Article
import com.example.travelwriter.databinding.DraftListRowBinding

class DraftsDiffCallback: DiffUtil.ItemCallback<Article>() {
    override fun areContentsTheSame(oldArticle: Article, newArticle: Article): Boolean {
        return oldArticle == newArticle
    }
    override fun areItemsTheSame(oldArticle: Article, newArticle: Article): Boolean {
        return oldArticle.id == newArticle.id
    }
}
class DraftsAdapter(private val openArticleClickListener: ArticleClickListener,
                    private val deleteArticleClickListener: ArticleClickListener
)
    : ListAdapter<Article, RecyclerView.ViewHolder>(DraftsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder) {
            is ViewHolder -> {
                val item = getItem((position)) as Article
                viewHolder.bind(item, openArticleClickListener, deleteArticleClickListener)
            }
        }
    }
    class ViewHolder private constructor(val binding: DraftListRowBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from((parent.context))
                val binding = DraftListRowBinding.inflate(layoutInflater, parent, false)
                return  ViewHolder(binding)
            }
        }
        fun bind(
            article: Article,
            openArticleClickListener: ArticleClickListener,
            deleteArticleClickListener: ArticleClickListener
        ) {
            binding.article = article
            binding.articleClickListener = openArticleClickListener
            binding.deleteListItemButton.setOnClickListener {
                deleteArticleClickListener.onClickDelete(article)
            }
            binding.executePendingBindings()
        }
    }
}
class ArticleClickListener(val articleClickListener: (articleId: Int) -> Unit) {
    fun onClickDelete(article: Article) {
        articleClickListener(article.id)
    }
    fun onClickOpen(article: Article) {
        articleClickListener(article.id)
    }
}