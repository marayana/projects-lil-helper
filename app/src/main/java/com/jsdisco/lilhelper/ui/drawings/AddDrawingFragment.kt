package com.jsdisco.lilhelper.ui.drawings


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

import androidx.fragment.app.activityViewModels
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.databinding.FragmentAddDrawingBinding
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddDrawingFragment : Fragment() {

    private val viewModel: DrawingsViewModel by activityViewModels()
    private lateinit var binding: FragmentAddDrawingBinding

    private lateinit var drawingView: DrawingView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawingView = DrawingView(requireContext(), viewModel)
        binding.flAdddrawing.addView(drawingView)

        binding.ibDrawingBlack.setOnClickListener {
            drawingView.selectColour(Colours.black)
        }
        binding.ibDrawingWhite.setOnClickListener {
            drawingView.selectColour(Colours.white)
        }
        binding.ibDrawingRed.setOnClickListener {
            drawingView.selectColour(Colours.red)
        }
        binding.ibDrawingGreen.setOnClickListener {
            drawingView.selectColour(Colours.green)
        }
        binding.ibDrawingBlue.setOnClickListener {
            drawingView.selectColour(Colours.blue)
        }
        binding.ibDrawingYellow.setOnClickListener {
            drawingView.selectColour(Colours.yellow)
        }
        binding.ibDrawingViolet.setOnClickListener {
            drawingView.selectColour(Colours.violet)
        }
        binding.ibDrawingTeal.setOnClickListener {
            drawingView.selectColour(Colours.teal)
        }

        binding.ibAdddrawingClear.setOnClickListener {
            clearCanvas()
        }

        binding.btnAdddrawingSave.setOnClickListener {
            try {
                val path = drawingView.saveDrawing()
                val title = path.split("/").last()
                viewModel.saveDrawing(path, title)
                clearCanvas()
            } catch(e: Exception){
                Log.e("AddDrawingFragment", "Error saving drawing: $e")
            }

        }
    }

    private fun clearCanvas(){
        binding.flAdddrawing.removeView(drawingView)
        val newDrawingView = DrawingView(requireContext(), viewModel)
        binding.flAdddrawing.addView(newDrawingView)
        drawingView = newDrawingView
    }
}