package com.jsdisco.lilhelper.ui.drawings

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.RequiresApi
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class DrawingView(
    context: Context,
    private val viewModel: DrawingsViewModel
) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = viewModel.strokeWidth
    }
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currX = 0f
    private var currY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::extraBitmap.isInitialized){
            extraBitmap.recycle()
        }

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        //extraCanvas.drawColor(resources.getColor(R.color.blue_200))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchStart(){
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currX = motionTouchEventX
        currY = motionTouchEventY
    }

    private fun touchMove(){
        val dx = Math.abs(motionTouchEventX - currX)
        val dy = Math.abs(motionTouchEventY - currY)
        if (dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(currX, currY, (motionTouchEventX + currX) / 2, (motionTouchEventY + currY) / 2)
            currX = motionTouchEventX
            currY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun touchUp(){
        path.reset()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDrawing() : String{
        val filePath = context.getDir("drawings", Context.MODE_PRIVATE)

        val currentTime = LocalDateTime.now()
        val formatted = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss.SSS"))
        val fileName = "drawing_$formatted.png"
        val file = File(filePath, fileName).writeBitmap(extraBitmap, Bitmap.CompressFormat.PNG, 100)

        return "$filePath/$fileName"
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int){
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }

    fun selectColour(rgb: List<Int>){
        paint.setARGB(255,rgb[0], rgb[1], rgb[2])
    }



}

