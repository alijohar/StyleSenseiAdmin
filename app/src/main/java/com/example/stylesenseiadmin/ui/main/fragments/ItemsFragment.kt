package com.example.stylesenseiadmin.ui.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.databinding.ActivityMainBinding
import com.example.stylesenseiadmin.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

    private lateinit var viewModel: ItemsViewModel
    private lateinit var binding: FragmentItemsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ItemsViewModel::class.java]
        binding = FragmentItemsBinding.inflate(inflater, container, false)


        viewModel.results.observe(viewLifecycleOwner){
            Log.i("AJC", it.size.toString())
        }

        return binding.root
    }


}