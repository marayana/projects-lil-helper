package com.jsdisco.lilhelper.ui.drawings

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jsdisco.lilhelper.R
import com.jsdisco.lilhelper.data.local.models.Note
import com.jsdisco.lilhelper.databinding.FragmentDrawingDetailsBinding

class DrawingDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDrawingDetailsBinding
    private val viewModel: DrawingsViewModel by activityViewModels()

    private var drawingId: Long = 0
    private var drawingTitle: String = ""
    private var drawingPath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        arguments?.let {
            drawingId = it.getLong("drawingId")
            drawingTitle = it.getString("drawingTitle").toString()
            drawingPath = it.getString("drawingPath").toString()
        }

        binding = FragmentDrawingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bmImg = BitmapFactory.decodeFile(drawingPath)
        binding.ivDrawingDetails.setImageBitmap(bmImg)

        binding.ibDrawingDetailsDelete.setOnClickListener {
            deleteDrawing(drawingId)
            findNavController().navigateUp()
        }
    }

    private fun showDialog(id: Long){
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title_drawings))
            .setMultiChoiceItems(arrayOf(getString(R.string.delete_dialog_dont_ask_again)), booleanArrayOf(false)){ _, _, _ ->
                viewModel.toggleSettings()
            }
            .setPositiveButton(getString(R.string.delete_dialog_positive_button)) { _, _ ->
                viewModel.deleteDrawing(id)
            }
            .setNegativeButton(getString(R.string.delete_dialog_negative_button)) { _, _ -> }
            .create()
        dialog.show()
    }

    private val deleteDrawing = { id: Long ->
        if (viewModel.askAgainDeleteDrawing.value == true){
            showDialog(id)
        } else {
            viewModel.deleteDrawing(id)
        }
    }
}