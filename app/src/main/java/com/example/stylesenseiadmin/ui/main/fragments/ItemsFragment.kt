package com.example.stylesenseiadmin.ui.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.adapter.CustomViewSelectStyle
import com.example.stylesenseiadmin.adapter.StyleCategoryGridAdapter
import com.example.stylesenseiadmin.databinding.ActivityMainBinding
import com.example.stylesenseiadmin.databinding.FragmentItemsBinding
import com.example.stylesenseiadmin.model.ItemResults

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
            handleGridView(it)
        }

        return binding.root
    }

    private fun handleGridView(it: List<ItemResults>?) {
        val gridAdapter = StyleCategoryGridAdapter(requireContext(), it as ArrayList<ItemResults>)
        binding.gridView.adapter = gridAdapter
        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->
                val selectedIndex: Int = gridAdapter.selectedPositions.indexOf(position)
                if (selectedIndex > -1) {
                    gridAdapter.selectedPositions.remove(position)
                    (view as CustomViewSelectStyle).display(false)
                } else {

                    gridAdapter.selectedPositions.add(position)
                    (view as CustomViewSelectStyle).display(true)
                }

            }
    }


}