package com.example.stylesenseiadmin.ui.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
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
    private lateinit var adapter: ItemAdapter
    private lateinit var viewModel: ItemsViewModel
    private lateinit var binding: FragmentItemsBinding
    private lateinit var itemResults: List<ItemResults>
    private var attrs: Map<String, List<String>>? = null
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
        viewModel.attrs.observe(viewLifecycleOwner) {
            if (it != null){
                binding.filter.visibility = View.VISIBLE
                binding.filter.setOnClickListener {
                    openAttrsSheet()
                }
                attrs = it
            }else {
                binding.filter.visibility = View.GONE
            }
        }
        viewModel.addAttrResult.observe(viewLifecycleOwner) {
            binding.addAttrSheet.sendingCountainer.visibility = View.VISIBLE
            binding.addAttrSheet.doneTitle.text = it
        }
        binding.retry.setOnClickListener {
            retry()
        }



        binding.addAttr.setOnClickListener {
            val selected = adapter.selectedPositions
            openAddAttrSheet(selected)

        }


        return binding.root
    }

    private fun openAttrsSheet() {
        handleAttrSheet()
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3F
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bg.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            binding.bg.visibility = View.GONE
        }
    }

    private fun openAddAttrSheet(selected: ArrayList<Int>) {
        handleAddAttrSheet()
        val array = ArrayList<Int>()
        for (item in selected){
            array.add(itemResults[item].id)
        }
        handleDes(array)

        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3F
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bg.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            binding.bg.visibility = View.GONE
        }

        binding.addAttrSheet.submit.setOnClickListener {
            if (binding.addAttrSheet.key.text!!.isNotEmpty() && binding.addAttrSheet.value.text!!.isNotEmpty()){
                viewModel.addAttr(array, binding.addAttrSheet.key.text.toString(), binding.addAttrSheet.value.text.toString())
            }
        }
    }

    private fun handleFail(it: String?) {
        binding.lottieLoading.visibility = View.INVISIBLE
        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
        binding.titleLoading.setTextColor(redColor)
        binding.titleLoading.text = it
        binding.retry.visibility = View.VISIBLE

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
        adapter = ItemAdapter(requireContext(), it as ArrayList<ItemResults>)
        binding.gridView.onItemLongClickListener = this
        binding.gridView.adapter = adapter
        binding.gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->

                val selectedIndex: Int = adapter.selectedPositions.indexOf(position)
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(position)
                    (view as ItemCustomView).display(false)
                } else {
                    adapter.selectedPositions.add(position)
                    (view as ItemCustomView).display(true)
                }
                if (adapter.selectedPositions.isNotEmpty()){
                    binding.addAttr.visibility = View.VISIBLE
                }else{
                    binding.addAttr.visibility = View.GONE
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

    private fun handleDes(selected: ArrayList<Int>) {
        binding.addAttrSheet.title.text = String.format(
            Locale.getDefault(),
            "%s (%s)",
            requireContext().resources.getString(R.string.selected),
            selected.joinToString()
        )
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
    private fun handleAddAttrSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.addAttrSheet.bottomSheet)
    }
    private fun handleAttrSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.attrSheet.bottomSheet)
    }

}