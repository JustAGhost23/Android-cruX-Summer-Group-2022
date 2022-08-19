package com.example.travelwriter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.travelwriter.databinding.FirstTimeFragmentBinding

class FirstTimeFragment : Fragment() {
    private lateinit var binding: FirstTimeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.first_time_fragment,
            container, false)

        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)

        binding.firstTimeFragmentButton.setOnClickListener{
            if (binding.firstTimeFragmentName.length() == 0){
                binding.firstTimeFragmentName.error = "Please Enter your name"
            } else {
                sharedPrefs?.edit()?.putString("user", binding.firstTimeFragmentName.text.toString())?.apply()
                it.findNavController().navigate(FirstTimeFragmentDirections.actionFirstTimeFragmentToMainFragment())
            }
        }

        return binding.root
    }
}