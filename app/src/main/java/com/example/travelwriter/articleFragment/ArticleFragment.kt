package com.example.travelwriter.articleFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.example.travelwriter.R
import com.example.travelwriter.databinding.ArticleFragmentBinding

class ArticleFragment : Fragment() {
    private lateinit var viewModel: ArticleFragmentViewModel
    private lateinit var binding: ArticleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val postedArticleTitle = sharedPrefs?.getString("postedArticleTitle", null)
        val postedArticleBody = sharedPrefs?.getString("postedArticleBody", null)
        val postedArticleAuthor = sharedPrefs?.getString("postedArticleAuthor", null)

        viewModel = ViewModelProvider(this, ArticleFragmentViewModelFactory())[ArticleFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.article_fragment, container,
            false)

        binding.articleFragmentTitleText.text = postedArticleTitle
        binding.articleFragmentBodyText.text = postedArticleBody
        binding.articleFragmentAuthorText.text = "by ${postedArticleAuthor}"

        binding.lifecycleOwner = this
        binding.articleFragmentViewModel = viewModel

        return binding.root
    }
}