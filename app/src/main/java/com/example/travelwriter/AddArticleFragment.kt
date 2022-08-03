package com.example.travelwriter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.travelwriter.databinding.AddArticleFragmentBinding

class AddArticleFragment : Fragment() {
    private lateinit var binding: AddArticleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DataBindingUtil.inflate(inflater, R.layout.add_article_fragment,
            container, false)

        binding.addArticleFragmentPostButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(AddArticleFragmentDirections.actionAddArticleFragmentToMainFragment())
        )
        binding.addArticleFragmentSaveButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(AddArticleFragmentDirections.actionAddArticleFragmentToDraftsFragment())
        )
        return binding.root
    }
}