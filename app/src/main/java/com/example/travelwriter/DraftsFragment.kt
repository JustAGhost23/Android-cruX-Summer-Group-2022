package com.example.travelwriter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.travelwriter.databinding.DraftsFragmentBinding

class DraftsFragment : Fragment() {
    private lateinit var binding: DraftsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.drafts_fragment, container,
            false)
        return binding.root
    }
}