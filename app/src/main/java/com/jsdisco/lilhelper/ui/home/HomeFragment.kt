package com.jsdisco.lilhelper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.jsdisco.lilhelper.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(
            viewLifecycleOwner,
            Observer {
                when(it){
                    Status.LOADING -> {
                        binding.pbHome.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.pbHome.visibility = View.GONE
                        //binding.ivRecipesCloudOff.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.pbHome.visibility = View.GONE
                        binding.tvHomeTitle.visibility = View.VISIBLE
                        //binding.ivRecipesCloudOff.visibility = View.GONE
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }
}