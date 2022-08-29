package com.example.travelwriter.mainFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelwriter.R
import com.example.travelwriter.database.Article
import com.example.travelwriter.databinding.PostListRowBinding


class MainFragmentAdapter(private var articleList: MutableList<Article>, private val openArticleClickListener: ArticleClickListener) :
    RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: PostListRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.post_list_row, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: Article = articleList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ViewHolder(postListRowBinding: PostListRowBinding) :
        RecyclerView.ViewHolder(postListRowBinding.root) {
        var binding: PostListRowBinding = postListRowBinding
        fun bind(obj: Article) {
            binding.article = obj
            binding.articleClickListener = openArticleClickListener
            binding.executePendingBindings()
        }

    }

    fun updateDataSet(updatedArticleList: MutableList<Article>) {
        articleList.clear()
        articleList = updatedArticleList
        notifyItemChanged(itemCount)
    }

}
class ArticleClickListener(val articleClickListener: (article: Article) -> Unit) {
    fun onClickOpen(article: Article) {
        articleClickListener(article)
    }
}