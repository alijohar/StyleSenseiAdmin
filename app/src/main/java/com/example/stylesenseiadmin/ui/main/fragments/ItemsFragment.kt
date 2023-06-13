package com.example.stylesenseiadmin.ui.main.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stylesenseiadmin.R
import com.example.stylesenseiadmin.adapter.*
import com.example.stylesenseiadmin.adapter.ItemCustomView
import com.example.stylesenseiadmin.databinding.FragmentItemsBinding
import com.example.stylesenseiadmin.model.ExpandableGroup
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
    var map: MutableMap<String, MutableList<String>> = mutableMapOf()
    var attrsString = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ItemsViewModel::class.java]
        binding = FragmentItemsBinding.inflate(inflater, container, false)


        viewModel.results.observe(viewLifecycleOwner) {
            if (binding.filter.visibility == View.GONE) {
                binding.progressBar.visibility = View.VISIBLE
            }
            handleGridView(it)
        }

        viewModel.fail.observe(viewLifecycleOwner) {
            handleFail(it)
        }
        viewModel.attrs.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.progressBar.visibility = View.GONE
                binding.filter.visibility = View.VISIBLE
                attrs = it
                binding.filter.setOnClickListener {
                    openAttrsSheet(attrs!!)
                }
            } else {
                binding.filter.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE

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

    private fun openAttrsSheet(attrs: Map<String, List<String>>) {
        handleAttrSheet()
        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3F
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        binding.attrSheet.close.setOnClickListener {
            hideSheet()
        }
        // Disable scroll down
        sheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    // Disable scroll-down behavior
                    sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }else if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    attrsString = ""
                    for ((groupName, children) in map) {
                        for (child in children) {
                            attrsString = "$attrsString\"$groupName\":\"$child\","
                        }
                    }
                    Log.i("AJC", attrsString)

                    if (attrsString.isNotEmpty() && attrsString.last() == ',') {
                        attrsString = attrsString.dropLast(1)
                    }
                    binding.progress.visibility = View.VISIBLE
                    clearGridViewItems()
                    viewModel.getOnlineItems(attrsString)
                    map.clear()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Optional: handle sliding behavior
            }
        })

        binding.bg.setOnClickListener {
            hideSheet()
        }
        val expandableList: List<ExpandableGroup> = attrs.map { (headers, items) ->
            ExpandableGroup(headers, items)
        }

        val adapter = ExpandableListAdapter(expandableList)
        adapter.setOnChildClickListener(object : ExpandableListAdapter.OnChildClickListener {
            override fun onChildClick(groupPosition: Int, childPosition: Int) {
                val group = adapter.getGroup(groupPosition) as ExpandableGroup
                val child = adapter.getChild(groupPosition, childPosition) as String
                val existingChildren = map[group.groupName]
                if (existingChildren != null) {
                    existingChildren.add(child)
                } else {
                    map[group.groupName] = mutableListOf(child)
                }

            }
        })



        binding.attrSheet.expandableListView.setAdapter(adapter)
        adapter.attachChildClickListener(binding.attrSheet.expandableListView)
        binding.attrSheet.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
    }


    private fun openAddAttrSheet(selected: ArrayList<Int>) {
        handleAddAttrSheet()
        val array = ArrayList<Int>()
        for (item in selected) {
            array.add(itemResults[item].id)
        }
        handleDes(array)

        binding.bg.visibility = View.VISIBLE;
        binding.bg.alpha = 0.3F
        sheetBehavior?.peekHeight = 440
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bg.setOnClickListener {
            hideSheet()
        }

        binding.addAttrSheet.submit.setOnClickListener {
            if (binding.addAttrSheet.key.text!!.isNotEmpty() && binding.addAttrSheet.value.text!!.isNotEmpty()) {
                viewModel.addAttr(
                    array,
                    binding.addAttrSheet.key.text.toString(),
                    binding.addAttrSheet.value.text.toString()
                )
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
        Log.i("AJC", it?.size.toString())
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
                if (adapter.selectedPositions.isNotEmpty()) {
                    binding.addAttr.visibility = View.VISIBLE
                } else {
                    binding.addAttr.visibility = View.GONE
                }

            }


    }
    // Function to clear items of GridView
    private fun clearGridViewItems() {
        adapter.clear()
        adapter.notifyDataSetChanged()
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
            hideSheet()
        }

    }

    private fun hideSheet() {

        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        binding.bg.visibility = View.GONE
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
        binding.sheet.type.text = if (item.type == 1) "shoes" else "clothes"
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