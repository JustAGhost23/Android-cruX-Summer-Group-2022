package com.example.travelwriter.articleFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val postedArticleId = sharedPrefs?.getString("postedArticleId", null)
        val user = sharedPrefs?.getString("user", null)
        println(postedArticleId)

        viewModel = ViewModelProvider(this, ArticleFragmentViewModelFactory())[ArticleFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.article_fragment, container,
            false)

        binding.articleFragmentTitleText.text = postedArticleTitle
        binding.articleFragmentBodyText.text = postedArticleBody
        binding.articleFragmentAuthorText.text = "by $postedArticleAuthor"
        if(user == postedArticleAuthor) {
            binding.articleFragmentDeleteButton.visibility = View.VISIBLE
        }
        else {
            binding.articleFragmentDeleteButton.visibility = View.GONE
        }

        viewModel.navigateToMain.observe(viewLifecycleOwner) { go ->
            if (go) {
                this.findNavController().navigate(
                    ArticleFragmentDirections
                        .actionArticleFragmentToMainFragment()
                )
                viewModel.navigatedToMain()
            }
        }

        binding.articleFragmentDeleteButton.setOnClickListener {
            val alertDialogBuilder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            alertDialogBuilder?.setMessage(R.string.deletePostDialogBoxMessage)!!
                .setCancelable(false)
                .setPositiveButton("Proceed") { dialog, _ ->
                    viewModel.delete(postedArticleAuthor!!, postedArticleId!!)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.setTitle(R.string.deletePostDialogBoxTitle)
            alertDialog.show()
        }

        binding.lifecycleOwner = this

        return binding.root
    }
}