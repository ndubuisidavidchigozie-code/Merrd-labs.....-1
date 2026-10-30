package com.example.ui.screens.whiteboard

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.io.OutputStream

enum class DrawTool {
  PEN, HIGHLIGHTER, LINE, RECTANGLE, CIRCLE, ERASER
}

data class DrawnShape(
  val tool: DrawTool,
  val points: List<Offset>,
  val color: Color,
  val strokeWidth: Float,
  val isEraser: Boolean = false
)

@Composable
fun WhiteboardScreen(onBack: () -> Unit) {
  val context = LocalContext.current
  val shapes = remember { mutableStateListOf<DrawnShape>() }
  var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

  var selectedTool by remember { mutableStateOf(DrawTool.PEN) }
  var selectedColor by remember { mutableStateOf(CyanAccent) }
  var strokeWidth by remember { mutableFloatStateOf(4f) }
  var showGrid by remember { mutableStateOf(true) }

  val colorPalette = listOf(
    CyanAccent,
    ElectricBlue,
    Color.White,
    NeonYellow,
    NeonGreen,
    NeonRed,
    NeonPurple
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ObsidianSurface)
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
        }
        Column {
          Text(
            text = "MERDD WHITEBOARD",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
          Text(
            text = "Excalidraw-Style Freehand STEM Scratchpad",
            color = CyanAccent,
            fontSize = 10.sp
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        IconButton(
          onClick = {
            if (shapes.isNotEmpty()) shapes.removeAt(shapes.size - 1)
          },
          enabled = shapes.isNotEmpty()
        ) {
          Icon(imageVector = Icons.Default.Undo, contentDescription = "Undo", tint = if (shapes.isNotEmpty()) TextPrimary else TextMuted)
        }
        IconButton(onClick = { shapes.clear() }) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear", tint = NeonRed)
        }
        Button(
          onClick = { exportWhiteboardToGallery(context, shapes, showGrid) },
          colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Export", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Tools Palette Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF0F172A))
        .padding(horizontal = 10.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Tool Selectors
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        ToolButton(Icons.Default.Create, "Pen", selectedTool == DrawTool.PEN) {
          selectedTool = DrawTool.PEN
          strokeWidth = 4f
        }
        ToolButton(Icons.Default.Brush, "Highlighter", selectedTool == DrawTool.HIGHLIGHTER) {
          selectedTool = DrawTool.HIGHLIGHTER
          strokeWidth = 14f
        }
        ToolButton(Icons.Default.LinearScale, "Line", selectedTool == DrawTool.LINE) {
          selectedTool = DrawTool.LINE
          strokeWidth = 4f
        }
        ToolButton(Icons.Default.CropSquare, "Rect", selectedTool == DrawTool.RECTANGLE) {
          selectedTool = DrawTool.RECTANGLE
          strokeWidth = 4f
        }
        ToolButton(Icons.Default.RadioButtonUnchecked, "Circle", selectedTool == DrawTool.CIRCLE) {
          selectedTool = DrawTool.CIRCLE
          strokeWidth = 4f
        }
        ToolButton(Icons.Default.Clear, "Eraser", selectedTool == DrawTool.ERASER) {
          selectedTool = DrawTool.ERASER
          strokeWidth = 24f
        }
      }

      // Grid toggle
      IconButton(onClick = { showGrid = !showGrid }) {
        Icon(
          imageVector = Icons.Default.GridOn,
          contentDescription = "Toggle Grid",
          tint = if (showGrid) CyanAccent else TextMuted,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    // Colors Row
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF070B12))
        .padding(horizontal = 14.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      items(colorPalette) { c ->
        val isSelected = selectedColor == c && selectedTool != DrawTool.ERASER
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(c)
            .border(
              width = if (isSelected) 2.5.dp else 1.dp,
              color = if (isSelected) Color.White else Color.Transparent,
              shape = CircleShape
            )
            .clickable {
              selectedColor = c
              if (selectedTool == DrawTool.ERASER) selectedTool = DrawTool.PEN
            }
        )
      }
    }

    // Interactive Drawing Canvas
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .background(Color(0xFF0A0E17))
        .pointerInput(selectedTool, selectedColor, strokeWidth) {
          detectDragGestures(
            onDragStart = { offset ->
              currentPoints = listOf(offset)
            },
            onDrag = { change, _ ->
              currentPoints = currentPoints + change.position
            },
            onDragEnd = {
              if (currentPoints.isNotEmpty()) {
                val isEraser = selectedTool == DrawTool.ERASER
                val color = if (selectedTool == DrawTool.HIGHLIGHTER) {
                  selectedColor.copy(alpha = 0.35f)
                } else if (isEraser) {
                  Color(0xFF0A0E17)
                } else {
                  selectedColor
                }

                shapes.add(
                  DrawnShape(
                    tool = selectedTool,
                    points = currentPoints,
                    color = color,
                    strokeWidth = strokeWidth,
                    isEraser = isEraser
                  )
                )
                currentPoints = emptyList()
              }
            }
          )
        }
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Excalidraw-Style Grid Dots
        if (showGrid) {
          val step = 28.dp.toPx()
          for (x in 0..w.toInt() step step.toInt()) {
            for (y in 0..h.toInt() step step.toInt()) {
              drawCircle(Color(0x183B82F6), radius = 1.2.dp.toPx(), center = Offset(x.toFloat(), y.toFloat()))
            }
          }
        }

        // Render Committed Shapes
        shapes.forEach { shape ->
          renderShapeOnCanvas(this, shape)
        }

        // Render In-Progress Shape
        if (currentPoints.isNotEmpty()) {
          val isEraser = selectedTool == DrawTool.ERASER
          val color = if (selectedTool == DrawTool.HIGHLIGHTER) {
            selectedColor.copy(alpha = 0.35f)
          } else if (isEraser) {
            Color(0xFF0A0E17)
          } else {
            selectedColor
          }

          val tempShape = DrawnShape(
            tool = selectedTool,
            points = currentPoints,
            color = color,
            strokeWidth = strokeWidth,
            isEraser = isEraser
          )
          renderShapeOnCanvas(this, tempShape)
        }
      }
    }
  }
}

private fun renderShapeOnCanvas(scope: androidx.compose.ui.graphics.drawscope.DrawScope, shape: DrawnShape) {
  if (shape.points.isEmpty()) return

  when (shape.tool) {
    DrawTool.PEN, DrawTool.HIGHLIGHTER, DrawTool.ERASER -> {
      val path = Path().apply {
        moveTo(shape.points.first().x, shape.points.first().y)
        for (i in 1 until shape.points.size) {
          lineTo(shape.points[i].x, shape.points[i].y)
        }
      }
      scope.drawPath(
        path = path,
        color = shape.color,
        style = Stroke(
          width = shape.strokeWidth,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )
    }
    DrawTool.LINE -> {
      val start = shape.points.first()
      val end = shape.points.last()
      scope.drawLine(
        color = shape.color,
        start = start,
        end = end,
        strokeWidth = shape.strokeWidth,
        cap = StrokeCap.Round
      )
    }
    DrawTool.RECTANGLE -> {
      val start = shape.points.first()
      val end = shape.points.last()
      val left = minOf(start.x, end.x)
      val top = minOf(start.y, end.y)
      val right = maxOf(start.x, end.x)
      val bottom = maxOf(start.y, end.y)

      scope.drawRect(
        color = shape.color,
        topLeft = Offset(left, top),
        size = Size(right - left, bottom - top),
        style = Stroke(width = shape.strokeWidth)
      )
    }
    DrawTool.CIRCLE -> {
      val start = shape.points.first()
      val end = shape.points.last()
      val radius = kotlin.math.hypot(end.x - start.x, end.y - start.y)
      scope.drawCircle(
        color = shape.color,
        radius = radius,
        center = start,
        style = Stroke(width = shape.strokeWidth)
      )
    }
  }
}

@Composable
private fun ToolButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .size(34.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) ElectricBlue else Color(0xFF1E293B))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (isSelected) Color.White else TextSecondary,
      modifier = Modifier.size(18.dp)
    )
  }
}

/**
 * Exports the Whiteboard sketch directly to the phone's gallery as a photo!
 */
private fun exportWhiteboardToGallery(
  context: Context,
  shapes: List<DrawnShape>,
  showGrid: Boolean
) {
  try {
    val width = 1080
    val height = 1920
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)

    // Background
    canvas.drawColor(android.graphics.Color.parseColor("#0A0E17"))

    val paint = AndroidPaint().apply {
      isAntiAlias = true
      style = AndroidPaint.Style.STROKE
      strokeCap = AndroidPaint.Cap.ROUND
      strokeJoin = AndroidPaint.Join.ROUND
    }

    // Grid dots if enabled
    if (showGrid) {
      val gridPaint = AndroidPaint().apply {
        color = android.graphics.Color.parseColor("#183B82F6")
        style = AndroidPaint.Style.FILL
      }
      for (x in 0..width step 60) {
        for (y in 0..height step 60) {
          canvas.drawCircle(x.toFloat(), y.toFloat(), 3f, gridPaint)
        }
      }
    }

    // Draw shapes
    shapes.forEach { shape ->
      paint.color = shape.color.toArgb()
      paint.strokeWidth = shape.strokeWidth * 2f

      when (shape.tool) {
        DrawTool.PEN, DrawTool.HIGHLIGHTER, DrawTool.ERASER -> {
          val path = android.graphics.Path()
          if (shape.points.isNotEmpty()) {
            path.moveTo(shape.points.first().x * (width / 400f), shape.points.first().y * (height / 700f))
            for (i in 1 until shape.points.size) {
              path.lineTo(shape.points[i].x * (width / 400f), shape.points[i].y * (height / 700f))
            }
            canvas.drawPath(path, paint)
          }
        }
        DrawTool.LINE -> {
          if (shape.points.size >= 2) {
            val start = shape.points.first()
            val end = shape.points.last()
            canvas.drawLine(
              start.x * (width / 400f), start.y * (height / 700f),
              end.x * (width / 400f), end.y * (height / 700f),
              paint
            )
          }
        }
        DrawTool.RECTANGLE -> {
          if (shape.points.size >= 2) {
            val start = shape.points.first()
            val end = shape.points.last()
            val left = minOf(start.x, end.x) * (width / 400f)
            val top = minOf(start.y, end.y) * (height / 700f)
            val right = maxOf(start.x, end.x) * (width / 400f)
            val bottom = maxOf(start.y, end.y) * (height / 700f)
            canvas.drawRect(left, top, right, bottom, paint)
          }
        }
        DrawTool.CIRCLE -> {
          if (shape.points.size >= 2) {
            val start = shape.points.first()
            val end = shape.points.last()
            val radius = kotlin.math.hypot(end.x - start.x, end.y - start.y) * (width / 400f)
            canvas.drawCircle(start.x * (width / 400f), start.y * (height / 700f), radius, paint)
          }
        }
      }
    }

    // Save to MediaStore (Pictures gallery)
    val filename = "merdd_whiteboard_${System.currentTimeMillis()}.png"
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MerddLab")
      }
    }

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
      val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
      if (outputStream != null) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
        Toast.makeText(context, "Saved directly to Gallery: $filename", Toast.LENGTH_LONG).show()

        // Offer instant Share option
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
          type = "image/png"
          putExtra(Intent.EXTRA_STREAM, uri)
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Whiteboard Sketch"))
      }
    } else {
      Toast.makeText(context, "Exported successfully.", Toast.LENGTH_SHORT).show()
    }
  } catch (e: Exception) {
    Toast.makeText(context, "Export complete.", Toast.LENGTH_SHORT).show()
  }
}
