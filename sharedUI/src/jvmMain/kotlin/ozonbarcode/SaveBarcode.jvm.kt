package ozonbarcode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import qrgenerator.barcodepainter.BarcodeFormat
import qrgenerator.barcodepainter.rememberBarcodePainter
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import androidx.compose.material3.Button
import androidx.compose.material3.Text

@Composable
actual fun SaveBarcode(text: String) {

    val barCodePainter = rememberBarcodePainter(
        content = text,
        format = BarcodeFormat.Code128,
        brush = SolidColor(Color.Black)
    )

    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                // Размер для сохранения (можно настроить)
                val imageSize = Size(400f, 200f)
                val imageBitmap = barCodePainter.toImageBitmap(imageSize)

                withContext(Dispatchers.IO) {
                    try {
                        val downloadDir = File(System.getProperty("user.home"), "Downloads")
                        val outputFile = File(downloadDir, "ozonBarcode.png")

                        if (outputFile.exists()) {
                            outputFile.delete()
                        }

                        imageBitmap.saveToFile(outputFile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    ) {
        Text("Сохранить")
    }
}

fun ImageBitmap.saveToFile(file: File, format: String = "png") {
    // Конвертируем ImageBitmap в BufferedImage для JVM
    val bufferedImage = this.toBufferedImage()
    // Сохраняем
    ImageIO.write(bufferedImage, format, file)
}

// Расширение для конвертации ImageBitmap в BufferedImage
fun ImageBitmap.toBufferedImage(): BufferedImage {
    val width = this.width
    val height = this.height
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val pixels = IntArray(width * height)

    this.readPixels(pixels, 0, 0, width, height, 0, width)
    bufferedImage.setRGB(0, 0, width, height, pixels, 0, width)
    return bufferedImage
}

fun Painter.toImageBitmap(
    size: Size,
    density: Density = Density(1f),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
): ImageBitmap {
    // Создаём пустое растровое изображение
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    // Рисуем Painter на этом полотне
    CanvasDrawScope().draw(density, layoutDirection, canvas, size) {
        draw(size)
    }
    return bitmap
}
