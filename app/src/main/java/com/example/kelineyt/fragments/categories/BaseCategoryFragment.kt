package com.example.kelineyt.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kelineyt.R
import com.example.kelineyt.adapters.BestProductsAdapter
import com.example.kelineyt.data.Category
import com.example.kelineyt.databinding.FragmentBaseCategoryBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseCategoryFragment(private val category: Category) :
    Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    private val viewModel by activityViewModels<CategoriesViewModel>()

    private lateinit var bestProductsAdapter: BestProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductsByCategory(category)

        setupBestProducts()

        lifecycleScope.launch {
            viewModel.error.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        //Collect products
        lifecycleScope.launchWhenStarted {
            viewModel.categoriesProducts.collectLatest { productsCategories ->
                binding.offerProductsProgressBar.visibility = View.GONE
                when (category) {
                    is Category.Chair -> {
                        bestProductsAdapter.differ.submitList(productsCategories.chairProducts?.bestProducts)
                    }
                    is Category.Cupboard -> {
                        bestProductsAdapter.differ.submitList(productsCategories.cupboardProducts?.bestProducts)
                    }
                    is Category.Table -> {
                        bestProductsAdapter.differ.submitList(productsCategories.tableProducts?.bestProducts)
                    }
                    is Category.Accessory -> {
                        bestProductsAdapter.differ.submitList(productsCategories.accessoryProducts?.bestProducts)
                    }
                    is Category.Furniture -> {
                        bestProductsAdapter.differ.submitList(productsCategories.furnitureProducts?.bestProducts)
                    }
                }
            }
        }

        //Collect loading
        lifecycleScope.launchWhenStarted {
            viewModel.loading.collectLatest {
                binding.offerProductsProgressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupBestProducts() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }
}

