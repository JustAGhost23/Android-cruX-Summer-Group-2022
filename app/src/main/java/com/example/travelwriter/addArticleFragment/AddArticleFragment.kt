package com.example.travelwriter.addArticleFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelwriter.R
import com.example.travelwriter.database.ArticleDatabase
import com.example.travelwriter.databinding.AddArticleFragmentBinding

class AddArticleFragment : Fragment() {
    private lateinit var viewModel: AddArticleFragmentViewModel
    private lateinit var binding: AddArticleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val database = ArticleDatabase.getDatabase(this.requireActivity().application).articleDao
        val args = AddArticleFragmentArgs.fromBundle(requireArguments())
        val user = sharedPrefs?.getString("user", null)
        val articleId = sharedPrefs?.getInt("articleID", -1)

        viewModel = ViewModelProvider(this, AddArticleFragmentViewModelFactory(database, args.articleId, user!!))[AddArticleFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.add_article_fragment,
            container, false)


        binding.addArticleFragmentTitle.setText(viewModel.loadTitle(articleId!!))
        binding.addArticleFragmentBody.setText(viewModel.loadBody(articleId))

        viewModel.navigateToMain.observe(viewLifecycleOwner) { go ->
            if (go) {
                this.findNavController().navigate(
                    AddArticleFragmentDirections
                        .actionAddArticleFragmentToMainFragment()
                )
                viewModel.navigatedToMain()
            }
        }
        viewModel.navigateToDrafts.observe(viewLifecycleOwner) { go ->
            if (go) {
                this.findNavController().navigate(
                    AddArticleFragmentDirections
                        .actionAddArticleFragmentToDraftsFragment()
                )
                viewModel.navigatedToDrafts()
            }
        }
        binding.addArticleFragmentPostButton.setOnClickListener {
            viewModel.validInput.observe(viewLifecycleOwner) { go ->
                if (go) {
                    binding.addArticleFragmentTitle.error = null
                } else {
                    binding.addArticleFragmentTitle.error = "Please add a title"
                }
            }
            val alertDialogBuilder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            alertDialogBuilder?.setMessage(R.string.createPostDialogBoxMessage)!!
                .setCancelable(false)
                .setPositiveButton("Proceed") { dialog, _ ->
                    viewModel.updateTitle(binding.addArticleFragmentTitle.text.toString())
                    viewModel.updateBody(binding.addArticleFragmentBody.text.toString())
                    viewModel.createPost()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.setTitle(R.string.createPostDialogBoxTitle)
            alertDialog.show()
        }
        binding.addArticleFragmentSaveButton.setOnClickListener {
            viewModel.validInput.observe(viewLifecycleOwner) { go ->
                if (go) {
                    binding.addArticleFragmentTitle.error = null
                } else {
                    binding.addArticleFragmentTitle.error = "Please add a title"
                }
            }
            viewModel.updateTitle(binding.addArticleFragmentTitle.text.toString())
            viewModel.updateBody(binding.addArticleFragmentBody.text.toString())
            viewModel.saveAsDraft()
        }
        binding.lifecycleOwner = this
        binding.addArticleFragmentViewModel = viewModel

        return binding.root
    }
}