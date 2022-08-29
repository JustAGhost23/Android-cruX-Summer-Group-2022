package com.example.travelwriter.mainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.travelwriter.R
import com.example.travelwriter.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.putInt("articleID", -1)?.apply()

        viewModel = ViewModelProvider(
            this,
            MainFragmentViewModelFactory()
        )[MainFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment,
            container, false
        )
        val adapter =
            MainFragmentAdapter(viewModel.postedArticleList.value!!,
                ArticleClickListener { article ->
                viewModel.openArticleWithId(article, sharedPrefs!!)
            })

        viewModel.postedArticleListString.observe(viewLifecycleOwner) { string ->
            string?.let {
                viewModel.stringToMutableList(viewModel.postedArticleListString.value!!)
            }
        }
        viewModel.postedArticleList.observe(viewLifecycleOwner) { list ->
            list?.let {
                adapter.updateDataSet(it)
            }
        }

        viewModel.navigateToArticle.observe(viewLifecycleOwner) { go ->
            if (go) {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionMainFragmentToArticleFragment()
                )
                viewModel.navigatedToArticle()
            }
        }

        binding.mainFragmentAddArticleButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(MainFragmentDirections.actionMainFragmentToAddArticleFragment())
        )

        binding.mainFragmentList.adapter = adapter
        binding.lifecycleOwner = this

        return binding.root
    }
}

