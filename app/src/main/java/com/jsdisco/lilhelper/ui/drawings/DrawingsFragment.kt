package com.jsdisco.lilhelper.ui.drawings

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.adapter.DrawingsAdapter
import com.jsdisco.lilhelper.databinding.FragmentDrawingsBinding
import java.util.*

class DrawingsFragment : Fragment() {

    private val viewModel: DrawingsViewModel by activityViewModels()
    private lateinit var binding: FragmentDrawingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDrawingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvDrawings = binding.rvDrawings
        val adapter = DrawingsAdapter(emptyList(), navToDrawing)
        rvDrawings.adapter = adapter

        viewModel.drawings.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
        })


        binding.fabAddDrawing.setOnClickListener {
            findNavController().navigate(DrawingsFragmentDirections.actionDrawingsFragmentToAddDrawingFragment())
        }
    }

    private val navToDrawing = {id: Long, path: String, title: String ->
        findNavController().navigate(DrawingsFragmentDirections.actionDrawingsFragmentToDrawingDetails(id, path, title))
    }

}