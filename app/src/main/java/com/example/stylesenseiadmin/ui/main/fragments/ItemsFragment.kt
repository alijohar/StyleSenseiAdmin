package com.example.stylesenseiadmin.ui.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.adapter.ItemCustomView
import com.example.stylesenseiadmin.adapter.ItemAdapter
import com.example.stylesenseiadmin.adapter.ItemDetailAdapter
import com.example.stylesenseiadmin.databinding.FragmentItemsBinding
import com.example.stylesenseiadmin.model.ItemResults
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.collections.ArrayList

class ItemsFragment : Fragment(), AdapterView.OnItemLongClickListener {

    private lateinit var viewModel: ItemsViewModel
    private lateinit var binding: FragmentItemsBinding
    private lateinit var itemResults: List<ItemResults>
    private var sheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ItemsViewModel::class.java]
        binding = FragmentItemsBinding.inflate(inflater, container, false)


        viewModel.results.observe(viewLifecycleOwner) {
            handleGridView(it)
        }

        viewModel.fail.observe(viewLifecycleOwner) {
            handleFail(it)
        }

        return binding.root
    }

    private fun handleFail(it: String?) {
        binding.lottieLoading.visibility = View.INVISIBLE
        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
        binding.titleLoading.setTextColor(redColor)
        binding.titleLoading.text = it
        binding.retry.visibility = View.VISIBLE
        binding.retry.setOnClickListener {
            retry()
        }
    }

    private fun retry() {
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)
        binding.titleLoading.text = requireContext().resources.getString(R.string.wait)
        binding.lottieLoading.visibility = View.VISIBLE
        binding.titleLoading.setTextColor(blackColor)
        viewModel.getOnlineItems()
    }

    private fun handleGridView(it: List<ItemResults>?) {
        if (it != null) {
            this.itemResults = it
        }
        binding.progress.visibility = View.GONE
        val gridAdapter = ItemAdapter(requireContext(), it as ArrayList<ItemResults>)
        binding.gridView.onItemLongClickListener = this
        binding.gridView.adapter = gridAdapter
        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->
                val selectedIndex: Int = gridAdapter.selectedPositions.indexOf(position)
                if (selectedIndex > -1) {
                    gridAdapter.selectedPositions.remove(position)
                    (view as ItemCustomView).display(false)
                } else {

                    gridAdapter.selectedPositions.add(position)
                    (view as ItemCustomView).display(true)
                }

            }


    }

    override fun onItemLongClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long): Boolean {
        openSheet(p2)
        return true
    }

    private fun openSheet(p2: Int) {
        handleStyleSheet(p2)
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3F

        var item = itemResults[p2]

        handleDes(item)
        handleRecyclerView(item)
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bg.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            binding.bg.visibility = View.GONE
        }

    }

    private fun handleRecyclerView(item: ItemResults) {
        val imageArray = item.images.split(",")
        val gridAdapter = ItemDetailAdapter(requireContext(), imageArray as ArrayList<String>)
        binding.sheet.gridView.adapter = gridAdapter
    }

    private fun handleDes(item: ItemResults) {
        binding.sheet.title.text = String.format(
            Locale.getDefault(),
            "%s (%d)",
            item.name,
            item.id
        )
        binding.sheet.type.text =  if (item.type == 1) "shoes" else "clothes"
        binding.sheet.price.text = item.price
        binding.sheet.attributeCount.text = item.product_attributes.size.toString()
    }

    private fun handleStyleSheet(p2: Int) {
        sheetBehavior = BottomSheetBehavior.from(binding.sheet.bottomSheet)
    }


}