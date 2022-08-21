package com.example.travelwriter.detailsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.travelwriter.R
import com.example.travelwriter.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding : DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, DetailsFragmentViewModelFactory())[DetailsFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.details_fragment, container,
            false)

        return binding.root
    }
}