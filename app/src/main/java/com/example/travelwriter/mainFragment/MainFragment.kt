package com.example.travelwriter.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.travelwriter.R
import com.example.travelwriter.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment,
            container, false)

        binding.mainFragmentAddArticleButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(MainFragmentDirections.actionMainFragmentToAddArticleFragment())
        )

        return binding.root
    }
}