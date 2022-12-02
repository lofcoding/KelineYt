package com.example.kelineyt.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Category
import com.example.kelineyt.data.Product
import com.example.kelineyt.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _categoriesProducts =
        MutableStateFlow<CategoriesProducts>(CategoriesProducts())
    val categoriesProducts: StateFlow<CategoriesProducts> = _categoriesProducts

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun getProductsByCategory(category: Category) {
        viewModelScope.launch { _loading.emit(true) }
        getProducts(category.category) { data, exception ->
            viewModelScope.launch { _loading.emit(false) }
            exception?.let {
                viewModelScope.launch {
                    _error.emit(exception.message.toString())
                }
            }
            viewModelScope.launch {
                when (category) {
                    is Category.Chair -> {
                        _categoriesProducts.emit(
                            categoriesProducts.value.copy(
                                chairProducts = FragmentProducts(bestProducts = data!!)
                            )
                        )
                    }
                    is Category.Cupboard -> {
                        _categoriesProducts.emit(
                            categoriesProducts.value.copy(
                                cupboardProducts = FragmentProducts(bestProducts = data!!)
                            )
                        )
                    }
                    is Category.Table -> {
                        _categoriesProducts.emit(
                            categoriesProducts.value.copy(
                                tableProducts = FragmentProducts(bestProducts = data!!)
                            )
                        )
                    }
                    is Category.Accessory -> {
                        _categoriesProducts.emit(
                            categoriesProducts.value.copy(
                                accessoryProducts = FragmentProducts(bestProducts = data!!)
                            )
                        )
                    }
                    is Category.Furniture -> {
                        _categoriesProducts.emit(
                            categoriesProducts.value.copy(
                                furnitureProducts = FragmentProducts(bestProducts = data!!)
                            )
                        )
                    }
                }
            }
        }
    }

    fun getProducts(
        category: String,
        onResult: (List<Product>?, Exception?) -> Unit
    ) {
        firestore.collection("Products").whereEqualTo("category", category).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                onResult(products, null)
            }.addOnFailureListener {
                onResult(null, it)
            }
    }
}


data class CategoriesProducts(
    val chairProducts: FragmentProducts? = null,
    val cupboardProducts: FragmentProducts? = null,
    val tableProducts: FragmentProducts? = null,
    val accessoryProducts: FragmentProducts? = null,
    val furnitureProducts: FragmentProducts? = null,
)

data class FragmentProducts(
    val topListProducts: List<Product> = emptyList(),
    val bestProducts: List<Product> = emptyList(),
)