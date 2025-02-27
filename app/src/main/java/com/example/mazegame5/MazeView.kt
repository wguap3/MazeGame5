package com.example.mazegame5

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class MazeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val levels = listOf(
        // Уровень 1
        arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            intArrayOf(1, 0, 3, 1, 0, 0, 0, 1), // Стартовая позиция (3) на (2, 1)
            intArrayOf(1, 0, 1, 1, 0, 1, 0, 1),
            intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
            intArrayOf(1, 1, 1, 1, 0, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
            intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 2, 1)  // Финиш (2)
        ),
        // Уровень 2
        arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 1, 0, 3, 1), // Стартовая позиция (3) на (1, 1)
            intArrayOf(1, 0, 1, 0, 1, 0, 1, 1),
            intArrayOf(1, 0, 1, 0, 0, 0, 1, 1),
            intArrayOf(1, 0, 1, 1, 1, 0, 0, 1),
            intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
            intArrayOf(1, 0, 1, 1, 0, 0, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 2, 1, 1)  // Финиш (2)
        ),
        // Уровень 3
        arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 0, 0, 1, 1), // Стартовая позиция (3) на (2, 1)
            intArrayOf(1, 0, 1, 1, 1, 0, 1, 1),
            intArrayOf(1, 0, 0, 0, 0, 0, 3, 1),
            intArrayOf(1, 0, 1, 1, 1, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
            intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 2, 1)  // Финиш (2)
        )
    )

    private var currentLevel = 0
    private var maze: Array<IntArray> = levels[currentLevel]
    private var playerX = 0
    private var playerY = 0
    private var prevPlayerX = playerX
    private var prevPlayerY = playerY
    private var cellSize: Float = 100f
    private var offsetX = 0f
    private var offsetY = 0f
    private var isLevelCompleted = false
    private var isGameCompleted = false
    private val path = mutableListOf<Pair<Int, Int>>()

    private val pathPaint = Paint().apply {
        color = Color.parseColor("#8B4513")
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val startPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        textSize = 80f
        style = Paint.Style.FILL
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
        setShadowLayer(10f, 5f, 5f, Color.BLACK)
        color = Color.YELLOW
    }

    private lateinit var textShader: Shader

    private val playerFrames = mutableListOf<Bitmap>()
    private var currentFrameIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    private var playerRotation = 0f
    private var isMirrored = false

    private lateinit var bananaBitmap: Bitmap

    init {
        paint.color = Color.parseColor("#8B4513")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true

        // Находим стартовую позицию и устанавливаем начальные координаты игрока
        val (startX, startY) = findStartPosition()
        playerX = startX
        playerY = startY
        path.add(Pair(playerX, playerY))

        // Устанавливаем начальное направление обезьяны (влево)
        playerRotation = 360f // Смотрит влево
        isMirrored = false // Не отзеркаливать

        loadPlayerFrames()
        bananaBitmap = loadBananaImage()
    }

    private fun findStartPosition(): Pair<Int, Int> {
        for (row in maze.indices) {
            for (col in maze[row].indices) {
                if (maze[row][col] == 3) { // Ищем стартовую позицию (3)
                    return Pair(col, row) // Возвращаем координаты (x, y)
                }
            }
        }
        throw RuntimeException("Стартовая позиция не найдена в лабиринте!")
    }

    private fun loadBananaImage(): Bitmap {
        return try {
            val resourceId = resources.getIdentifier(
                "png_clipart_banana_banana_removebg_preview",
                "drawable",
                context.packageName
            )
            if (resourceId != 0) {
                BitmapFactory.decodeResource(resources, resourceId)
            } else {
                throw RuntimeException("Файл банана не найден в папке res/drawable")
            }
        } catch (e: Exception) {
            Log.e("MazeView", "Ошибка загрузки изображения банана: ${e.message}")
            throw e
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    private fun loadPlayerFrames() {
        try {
            for (i in 1..17) {
                val resourceId = resources.getIdentifier("frame_$i", "drawable", context.packageName)
                if (resourceId != 0) {
                    val frame = BitmapFactory.decodeResource(resources, resourceId)
                    if (frame != null) {
                        val mirroredFrame = mirrorBitmap(frame)
                        playerFrames.add(mirroredFrame)
                    } else {
                        throw RuntimeException("Не удалось загрузить frame_$i")
                    }
                } else {
                    throw RuntimeException("Файл frame_$i не найден в папке res/drawable")
                }
            }

            playerFrames.replaceAll {
                Bitmap.createScaledBitmap(it, (cellSize / 0.85).toInt(), (cellSize / 1.1).toInt(), true)
            }
        } catch (e: Exception) {
            Log.e("MazeView", "Ошибка загрузки кадров: ${e.message}")
            throw e
        }
    }

    private fun mirrorBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.setScale(-1f, 1f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun startAnimation() {
        val runnable = object : Runnable {
            override fun run() {
                currentFrameIndex = (currentFrameIndex + 1) % playerFrames.size
                invalidate()
                handler.postDelayed(this, 100)
            }
        }
        handler.post(runnable)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellSize = minOf(w / maze[0].size.toFloat(), h / maze.size.toFloat())

        val mazeWidth = maze[0].size * cellSize
        val mazeHeight = maze.size * cellSize
        offsetX = (w - mazeWidth) / 2f
        offsetY = (h - mazeHeight) / 2f

        textShader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            Color.parseColor("#FFD700"),
            Color.parseColor("#FFA500"),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (row in maze.indices) {
            for (col in maze[row].indices) {
                val x = col * cellSize + offsetX
                val y = row * cellSize + offsetY

                when (maze[row][col]) {
                    1 -> {
                        paint.color = Color.parseColor("#8B4513")
                        canvas.drawRect(x, y, x + cellSize, y + cellSize, paint)
                    }
                    2 -> {
                        val bananaWidth = cellSize * 0.8f
                        val bananaHeight = cellSize * 0.8f
                        val left = x + (cellSize - bananaWidth) / 2
                        val top = y + (cellSize - bananaHeight) / 2
                        canvas.drawBitmap(
                            bananaBitmap,
                            null,
                            RectF(left, top, left + bananaWidth, top + bananaHeight),
                            null
                        )
                    }
                    3 -> canvas.drawCircle(x + cellSize / 2, y + cellSize / 2, cellSize / 4, startPaint)
                }
            }
        }

        if (path.isNotEmpty()) {
            val pathToDraw = Path()
            val startX = path[0].first * cellSize + cellSize / 2 + offsetX
            val startY = path[0].second * cellSize + cellSize / 2 + offsetY
            pathToDraw.moveTo(startX, startY)

            for (i in 1 until path.size) {
                val (px, py) = path[i]
                val currentX = px * cellSize + cellSize / 2 + offsetX
                val currentY = py * cellSize + cellSize / 2 + offsetY
                pathToDraw.lineTo(currentX, currentY)
            }

            canvas.drawPath(pathToDraw, pathPaint)
        }

        if (playerFrames.isNotEmpty()) {
            val playerXPos = playerX * cellSize + offsetX + cellSize / 2
            val playerYPos = playerY * cellSize + offsetY + cellSize / 2

            canvas.save()

            if (isMirrored) {
                canvas.scale(-1f, 1f, playerXPos, playerYPos)
            }

            canvas.rotate(playerRotation, playerXPos, playerYPos)

            canvas.drawBitmap(
                playerFrames[currentFrameIndex],
                playerXPos - (cellSize / 3),
                playerYPos - (cellSize / 3),
                null
            )

            canvas.restore()
        }

        if (isGameCompleted) {
            textPaint.shader = textShader
            val text = "Все уровни пройдены!"
            canvas.drawText(text, width / 2f, height / 2f, textPaint)
        }
    }

    private fun movePlayer(newX: Int, newY: Int) {
        if (abs(playerX - newX) + abs(playerY - newY) == 1) {
            if (newX in maze[0].indices && newY in maze.indices && maze[newY][newX] != 1) {
                when {
                    newX > playerX -> {
                        playerRotation = 0f
                        isMirrored = true
                    }
                    newX < playerX -> {
                        playerRotation = 0f
                        isMirrored = false
                    }
                    newY > playerY -> {
                        playerRotation = 270f
                        isMirrored = true
                    }
                    newY < playerY -> {
                        playerRotation = 45f
                        isMirrored = true
                    }
                }

                val newPos = Pair(newX, newY)

                if (maze[newY][newX] == 2) {
                    isLevelCompleted = true
                    handler.postDelayed({
                        nextLevel()
                    }, 500) // Задержка перед переходом на следующий уровень
                }

                if (path.contains(newPos)) {
                    val index = path.indexOf(newPos)
                    path.subList(index + 1, path.size).clear()
                } else {
                    path.add(newPos)
                }

                prevPlayerX = playerX
                prevPlayerY = playerY
                playerX = newX
                playerY = newY
                invalidate()
            }
        }
    }

    private fun nextLevel() {
        if (currentLevel < levels.size - 1) {
            currentLevel++
            maze = levels[currentLevel]
            val (startX, startY) = findStartPosition()
            playerX = startX
            playerY = startY
            path.clear()
            path.add(Pair(playerX, playerY))
            playerRotation = 360f
            isMirrored = false
            isLevelCompleted = false
            invalidate()
        } else {
            // Все уровни пройдены
            isGameCompleted = true
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLevelCompleted || isGameCompleted) return true
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            val touchX = ((event.x - offsetX) / cellSize).toInt()
            val touchY = ((event.y - offsetY) / cellSize).toInt()
            if (touchX in maze[0].indices && touchY in maze.indices) {
                movePlayer(touchX, touchY)
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }
}