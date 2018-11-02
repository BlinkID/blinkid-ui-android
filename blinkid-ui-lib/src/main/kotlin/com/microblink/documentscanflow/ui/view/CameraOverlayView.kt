package com.microblink.documentscanflow.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.microblink.documentscanflow.R

class CameraOverlayView(context : Context, attrs : AttributeSet?, styleAttrs : Int) : View(context, attrs, styleAttrs)  {

    private val cutPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val hookPaint = Paint()

    private val bgColor : Int = ContextCompat.getColor(context, R.color.mbBgCameraOverlay)

    private val hookLengthVertical = resources.getDimension(R.dimen.mb_hook_length_vertical).toInt()
    private val hookLengthHorizontal= resources.getDimension(R.dimen.mb_hook_length_horizontal).toInt()
    private val hookWidth = resources.getDimension(R.dimen.mb_hook_stroke_width)
    private val halfHookWidth = hookWidth / 2

    private var scanRect: RectF = RectF()

    init {
        cutPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        hookPaint.style = Paint.Style.STROKE
        hookPaint.color = ContextCompat.getColor(context, R.color.mbIconHook)
        hookPaint.strokeWidth = hookWidth
    }

    constructor(context : Context, attrs : AttributeSet) : this(context, attrs, 0)

    constructor(context : Context) : this(context, null, 0)

    fun setScanRect(scanRect: RectF) {
        this.scanRect = scanRect
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (scanRect.height() == 0f) {
            return
        }

        val w = width
        val h = height

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val internalCanvas = Canvas(bitmap!!)

        internalCanvas.drawColor(bgColor)

        internalCanvas.drawRect(scanRect, cutPaint)

        drawTopLeftHook(canvas, scanRect.left, scanRect.top)
        drawTopRightHook(canvas, scanRect.right, scanRect.top)
        drawBottomLefHook(canvas, scanRect.left, scanRect.bottom)
        drawBottomRightHook(canvas, scanRect.right, scanRect.bottom)

        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun drawTopLeftHook(canvas: Canvas, left : Float, top : Float) {
        drawHorizontalLine(canvas, left, top + halfHookWidth)
        drawVerticalLine(canvas, left + halfHookWidth, top)
    }

    private fun drawTopRightHook(canvas: Canvas, right : Float, top : Float) {
        drawHorizontalLine(canvas, right - hookLengthHorizontal, top + halfHookWidth)
        drawVerticalLine(canvas, right - halfHookWidth, top)
    }

    private fun drawBottomLefHook(canvas: Canvas, left: Float, bottom : Float) {
        drawHorizontalLine(canvas, left, bottom - halfHookWidth)
        drawVerticalLine(canvas, left + halfHookWidth, bottom - hookLengthVertical)
    }

    private fun drawBottomRightHook(canvas: Canvas, right: Float, bottom: Float) {
        drawHorizontalLine(canvas, right - hookLengthHorizontal, bottom - halfHookWidth)
        drawVerticalLine(canvas, right -halfHookWidth, bottom - hookLengthVertical)
    }

    private fun drawHorizontalLine(canvas: Canvas, x : Float, y : Float) {
        canvas.drawLine(
                x, y,
                x + hookLengthHorizontal, y,
                hookPaint
        )
    }

    private fun drawVerticalLine(canvas: Canvas, x : Float, y : Float) {
        canvas.drawLine(
                x, y,
                x, y + hookLengthVertical,
                hookPaint
        )
    }

}