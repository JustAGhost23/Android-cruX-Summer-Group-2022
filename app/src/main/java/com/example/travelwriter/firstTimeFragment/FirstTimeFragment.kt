package com.example.travelwriter.firstTimeFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.travelwriter.R
import com.example.travelwriter.databinding.FirstTimeFragmentBinding

class FirstTimeFragment : Fragment() {
    private lateinit var viewModel: FirstTimeFragmentViewModel
    private lateinit var binding: FirstTimeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.putString("user", null)?.apply()
        viewModel = ViewModelProvider(this, FirstTimeFragmentViewModelFactory(sharedPrefs!!))[FirstTimeFragmentViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.first_time_fragment,
            container, false)

        viewModel.emptyName()

        binding.firstTimeFragmentButton.setOnClickListener {
            if (binding.firstTimeFragmentName.length() == 0) {
                binding.firstTimeFragmentName.error = "Please enter your name"
            }
            else {
                binding.firstTimeFragmentName.error = null
                viewModel.buttonClicked(binding.firstTimeFragmentName.text.toString())
            }
        }

        viewModel.navigateToMain.observe(viewLifecycleOwner) { go ->
            if (go) {
                this.findNavController().navigate(
                    FirstTimeFragmentDirections.actionFirstTimeFragmentToMainFragment()
                )
                viewModel.navigatedToMain()
            }
        }

        return binding.root
    }
}
