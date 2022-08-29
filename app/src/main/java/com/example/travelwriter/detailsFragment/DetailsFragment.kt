package com.example.travelwriter.detailsFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.travelwriter.R
import com.example.travelwriter.database.ArticleDatabase
import com.example.travelwriter.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding : DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val database = ArticleDatabase.getDatabase(this.requireActivity().application).articleDao

        viewModel = ViewModelProvider(this, DetailsFragmentViewModelFactory(database, sharedPrefs!!))[DetailsFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container,
            false)

        viewModel.articleCount.observe(viewLifecycleOwner){ list ->
            list?.let{
                val articleCount = it
                println(articleCount)
                binding.detailsFragmentDraftsText.text = "Number of drafts: $articleCount"
            }
        }
        viewModel.postedArticleCount.observe(viewLifecycleOwner){ list ->
            list.let{
                val postedArticleCount = it
                binding.detailsFragmentPostsText.text = "Articles Posted: $postedArticleCount"
            }
        }

        return binding.root
    }
}