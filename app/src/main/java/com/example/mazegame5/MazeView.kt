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
    private val maze: Array<IntArray> = arrayOf(
        intArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
        intArrayOf(1, 0, 3, 1, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 1, 0, 1, 0, 1),
        intArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
        intArrayOf(1, 1, 1, 1, 0, 1, 1, 1),
        intArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
        intArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
        intArrayOf(1, 1, 1, 1, 1, 1, 2, 1)
    )

    private var playerX = 2
    private var playerY = 1
    private var prevPlayerX = playerX
    private var prevPlayerY = playerY
    private var cellSize: Float = 100f
    private var offsetX = 0f
    private var offsetY = 0f
    private var isLevelCompleted = false
    private val path = mutableListOf<Pair<Int, Int>>()

    private val pathPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val finishPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
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
    }

    private lateinit var textShader: Shader

    // Анимация игрока
    private val playerFrames = mutableListOf<Bitmap>() // Список кадров анимации
    private var currentFrameIndex = 0 // Текущий кадр
    private val handler = Handler(Looper.getMainLooper()) // Handler для анимации

    // Угол поворота игрока
    private var playerRotation = 0f
    private var isMirrored = false // Флаг для отзеркаливания

    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        path.add(Pair(playerX, playerY))

        // Загрузите кадры анимации
        loadPlayerFrames()
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
                        // Отзеркаливаем кадр по горизонтали
                        val mirroredFrame = mirrorBitmap(frame)
                        playerFrames.add(mirroredFrame)
                    } else {
                        throw RuntimeException("Не удалось загрузить frame_$i")
                    }
                } else {
                    throw RuntimeException("Файл frame_$i не найден в папке res/drawable")
                }
            }

            // Масштабируйте кадры до нужного размера
            playerFrames.replaceAll {
                Bitmap.createScaledBitmap(it, (cellSize / 0.85).toInt(), (cellSize / 1.1).toInt(), true)
            }
        } catch (e: Exception) {
            Log.e("MazeView", "Ошибка загрузки кадров: ${e.message}")
            throw e
        }
    }

    // Функция для отзеркаливания Bitmap по горизонтали
    private fun mirrorBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.setScale(-1f, 1f) // Отражаем по горизонтали
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun startAnimation() {
        val runnable = object : Runnable {
            override fun run() {
                currentFrameIndex = (currentFrameIndex + 1) % playerFrames.size
                invalidate() // Перерисовываем View
                handler.postDelayed(this, 100) // Смена кадра каждые 100 мс
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

        // Инициализация градиента для текста
        textShader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            Color.parseColor("#1E90FF"), // Голубой
            Color.parseColor("#00008B"), // Темно-синий
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Рисуем лабиринт
        for (row in maze.indices) {
            for (col in maze[row].indices) {
                val x = col * cellSize + offsetX
                val y = row * cellSize + offsetY

                when (maze[row][col]) {
                    1 -> {
                        paint.color = Color.BLUE
                        canvas.drawRect(x, y, x + cellSize, y + cellSize, paint)
                    }
                    2 -> canvas.drawCircle(x + cellSize / 2, y + cellSize / 2, cellSize / 4, finishPaint)
                    3 -> canvas.drawCircle(x + cellSize / 2, y + cellSize / 2, cellSize / 4, startPaint)
                }
            }
        }

        // Рисуем путь
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

        // Рисуем игрока (текущий кадр анимации)
        if (playerFrames.isNotEmpty()) {
            val playerXPos = playerX * cellSize + offsetX + cellSize / 2
            val playerYPos = playerY * cellSize + offsetY + cellSize / 2

            // Сохраняем состояние canvas
            canvas.save()

            // Применяем отзеркаливание, если нужно
            if (isMirrored) {
                canvas.scale(-1f, 1f, playerXPos, playerYPos)
            }

            // Поворачиваем canvas вокруг центра игрока
            canvas.rotate(playerRotation, playerXPos, playerYPos)

            // Рисуем изображение игрока
            canvas.drawBitmap(
                playerFrames[currentFrameIndex],
                playerXPos - (cellSize / 3),
                playerYPos - (cellSize / 3),
                null
            )

            // Восстанавливаем состояние canvas
            canvas.restore()
        }

        // Рисуем сообщение о завершении уровня
        if (isLevelCompleted) {
            textPaint.shader = textShader
            val text = "Уровень пройден!"
            canvas.drawText(text, width / 2f, height / 2f, textPaint)
        }
    }

    private fun movePlayer(newX: Int, newY: Int) {
        if (abs(playerX - newX) + abs(playerY - newY) == 1) {
            if (newX in maze[0].indices && newY in maze.indices && maze[newY][newX] != 1) {
                // Определяем направление движения
                when {
                    newX > playerX -> {
                        playerRotation = 0f // Вправо
                        isMirrored = true // Отзеркалить
                    }
                    newX < playerX -> {
                        playerRotation = 0f // Влево
                        isMirrored = false // Не отзеркаливать
                    }
                    newY > playerY -> {
                        playerRotation = 270f // Вниз
                        isMirrored = true // Отзеркалить
                    }
                    newY < playerY -> {
                        playerRotation = 45f // Вверх
                        isMirrored = true // Отзеркалить
                    }
                }

                val newPos = Pair(newX, newY)

                if (maze[newY][newX] == 2) {
                    isLevelCompleted = true
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLevelCompleted) return true
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
        handler.removeCallbacksAndMessages(null) // Остановка всех задач Handler
    }
}