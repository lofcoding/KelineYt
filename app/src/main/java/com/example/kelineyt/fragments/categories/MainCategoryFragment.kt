package com.example.kelineyt.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kelineyt.R
import com.example.kelineyt.databinding.FragmentHomeBinding
import com.example.kelineyt.databinding.FragmentMainCategoryBinding

class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

}