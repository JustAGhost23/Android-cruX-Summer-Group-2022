package com.example.travelwriter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.travelwriter.databinding.AboutFragmentBinding

class AboutFragment : Fragment() {
    private lateinit var binding : AboutFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.about_fragment, container,
            false)
        return binding.root
    }
}