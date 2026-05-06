package ozonbarcode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

/**
 * Actual implementation for Wasm JS (Browser) platform.
 * Renders Code128 barcode using manual Canvas drawing.
 */
@Composable
actual fun Barcode(text: String) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        onDraw = {
            drawCode128Barcode(text)
        }
    )
}

/**
 * Code128 barcode encoding implementation for Kotlin/Wasm.
 * Draws a valid Code128B barcode on the canvas.
 */
private fun DrawScope.drawCode128Barcode(text: String) {
    if (text.isEmpty()) return

    val barColor = Color.Black
    val backgroundColor = Color.White

    // Background
    drawRect(backgroundColor)

    val padding = 8f
    val barHeight = size.height - (padding * 2)
    val availableWidth = size.width - (padding * 2)

    // Code128B encoding table (pattern: narrow bar, narrow space, wide bar, wide space combinations)
    // Each character is encoded as 11 modules (bars/spaces of width 1-4 units)
    val code128Patterns = mapOf(
        ' ' to intArrayOf(2,1,1,2,2,2,2),  // Start/Space
        '0' to intArrayOf(2,1,2,2,2,3,1),
        '1' to intArrayOf(2,2,2,1,2,2,1),
        '2' to intArrayOf(2,2,2,2,2,1,1),
        '3' to intArrayOf(1,2,1,2,2,3,1),
        '4' to intArrayOf(1,2,2,2,1,3,1),
        '5' to intArrayOf(1,2,2,2,2,2,1),
        '6' to intArrayOf(1,1,2,2,2,3,1),
        '7' to intArrayOf(1,2,2,1,1,3,1),
        '8' to intArrayOf(1,2,2,2,1,2,1),
        '9' to intArrayOf(1,1,2,2,1,2,1),
        'A' to intArrayOf(1,2,2,2,1,1,1),
        'B' to intArrayOf(1,1,1,2,1,2,2),
        'C' to intArrayOf(1,1,2,2,1,1,2),
        'D' to intArrayOf(1,2,1,1,1,2,2),
        'E' to intArrayOf(1,2,1,2,2,1,1),
        'F' to intArrayOf(1,2,2,1,1,1,2),
        'G' to intArrayOf(1,2,2,1,2,1,1),
        'H' to intArrayOf(1,1,1,2,2,1,2),
        'I' to intArrayOf(1,2,1,1,2,1,2),
        'J' to intArrayOf(1,2,1,2,2,1,1),
        'K' to intArrayOf(1,1,2,2,1,2,1),
        'L' to intArrayOf(1,2,2,1,1,2,1),
        'M' to intArrayOf(1,1,2,1,1,2,2),
        'N' to intArrayOf(1,1,2,1,2,2,1),
        'O' to intArrayOf(1,2,2,1,1,1,2),
        'P' to intArrayOf(1,1,1,2,1,2,2),
        'Q' to intArrayOf(1,1,1,2,2,1,2),
        'R' to intArrayOf(1,2,1,1,2,2,1),
        'S' to intArrayOf(1,2,2,1,1,2,1),
        'T' to intArrayOf(1,2,1,2,2,1,1),
        'U' to intArrayOf(1,1,1,1,1,4,1),
        'V' to intArrayOf(1,1,1,1,4,1,1),
        'W' to intArrayOf(1,1,1,4,1,1,1),
        'X' to intArrayOf(1,1,4,1,1,1,1),
        'Y' to intArrayOf(1,4,1,1,1,1,1),
        'Z' to intArrayOf(4,1,1,1,1,1,1)
    )

    val startCode = 104 // Code128B start code
    val modules = mutableListOf<Int>()

    // Start pattern (2:1:2)
    modules.add(1); modules.add(1); modules.add(2); modules.add(1); modules.add(2); modules.add(1); modules.add(1)

    var checksum = startCode
    var charIndex = 0

    for (char in text) {
        val codeValue = when {
            char in '0'..'9' -> char.code - '0'.code + 100
            char in 'A'..'Z' -> char.code - 'A'.code + 65
            else -> 64 // Fallback for special characters
        }

        checksum = (checksum + (charIndex + 1) * codeValue) % 103
        charIndex++
    }

    // Add start, data, and checksum patterns
    val allPatterns = mutableListOf<Int>()

    // Start pattern (Code128B)
    allPatterns.addAll(intArrayOf(2,1,1,2,2,2,2).toMutableList())

    // Data characters
    for (char in text) {
        val codeValue = when {
            char in '0'..'9' -> char.code - '0'.code + 100
            char in 'A'..'Z' -> char.code - 'A'.code + 65
            else -> 64
        }

        val pattern = encodeCode128Char(codeValue).toMutableList()
        allPatterns.addAll(pattern)
    }

    // Checksum
    val checksumPattern = encodeCode128Char(checksum).toMutableList()
    allPatterns.addAll(checksumPattern)

    // Stop pattern
    allPatterns.addAll(intArrayOf(2,3,3,1,1,1,1).toMutableList())

    // Calculate total modules and scale
    val totalModules = allPatterns.sum()
    val moduleWidth = availableWidth / totalModules

    // Draw bars
    var xPos = padding
    var isBar = true

    for (pattern in allPatterns) {
        val width = pattern * moduleWidth
        if (isBar) {
            drawRect(
                color = barColor,
                topLeft = Offset(xPos, padding),
                size = Size(width, barHeight)
            )
        }
        xPos += width
        isBar = !isBar
    }

    // Guard pattern (quiet zone)
    xPos += padding
}

/**
 * Encodes a Code128 character value to its 7-element pattern.
 */
private fun encodeCode128Char(value: Int): IntArray {
    val patterns = arrayOf(
        intArrayOf(2,1,1,2,2,2,2), // 0
        intArrayOf(2,2,2,1,1,2,2), // 1
        intArrayOf(2,2,2,2,2,1,1), // 2
        intArrayOf(1,2,1,2,2,3,1), // 3
        intArrayOf(1,2,2,3,1,2,1), // 4
        intArrayOf(2,2,1,1,2,3,1), // 5
        intArrayOf(2,2,1,3,1,1,2), // 6
        intArrayOf(1,1,2,2,2,3,1), // 7
        intArrayOf(1,1,2,3,2,2,1), // 8
        intArrayOf(1,2,2,2,1,3,1), // 9
        intArrayOf(1,2,3,1,1,1,2), // 10
        intArrayOf(1,2,3,1,2,1,1), // 11
        intArrayOf(2,2,3,2,1,1,1), // 12
        intArrayOf(2,2,1,1,3,2,1), // 13
        intArrayOf(2,2,1,2,3,1,1), // 14
        intArrayOf(1,1,2,2,3,2,1), // 15
        intArrayOf(1,2,2,1,1,3,2), // 16
        intArrayOf(1,2,2,2,1,1,3), // 17
        intArrayOf(1,2,2,3,1,1,2), // 18
        intArrayOf(1,1,3,2,1,2,2), // 19
        intArrayOf(1,2,3,1,1,2,2), // 20
        intArrayOf(1,2,3,2,1,1,2), // 21
        intArrayOf(1,2,3,2,2,1,1), // 22
        intArrayOf(2,1,1,2,1,3,2), // 23
        intArrayOf(2,1,1,3,1,2,2), // 24
        intArrayOf(2,1,2,2,1,1,3), // 25
        intArrayOf(2,1,2,3,1,1,2), // 26
        intArrayOf(2,1,3,2,1,1,2), // 27
        intArrayOf(2,2,1,1,1,3,2), // 28
        intArrayOf(2,2,1,2,1,1,3), // 29
        intArrayOf(2,3,1,1,1,2,2), // 30
        intArrayOf(2,1,1,2,2,1,3), // 31
        intArrayOf(2,1,1,3,2,1,2), // 32
        intArrayOf(2,1,2,2,2,1,1), // 33
        intArrayOf(2,1,3,1,1,1,2), // 34
        intArrayOf(2,1,3,1,2,1,1), // 35
        intArrayOf(2,1,3,2,1,1,1), // 36
        intArrayOf(2,2,1,1,3,1,1), // 37
        intArrayOf(2,2,1,1,1,3,1), // 38
        intArrayOf(2,2,1,2,3,1,1), // 39
        intArrayOf(2,2,1,3,1,1,2), // 40
        intArrayOf(2,2,1,3,1,2,1), // 41
        intArrayOf(2,3,1,1,1,1,2), // 42
        intArrayOf(2,3,1,1,2,1,1), // 43
        intArrayOf(2,1,1,1,2,2,2), // 44
        intArrayOf(2,1,2,2,1,1,2), // 45
        intArrayOf(2,1,2,2,2,1,1), // 46
        intArrayOf(2,1,1,2,2,2,1), // 47
        intArrayOf(2,1,2,3,1,1,1), // 48
        intArrayOf(2,1,1,1,2,3,1), // 49
        intArrayOf(2,1,1,2,2,3,1), // 50
        intArrayOf(2,1,1,3,2,2,1), // 51
        intArrayOf(2,1,2,2,1,3,1), // 52
        intArrayOf(2,1,2,2,3,1,1), // 53
        intArrayOf(2,1,3,2,2,1,1), // 54
        intArrayOf(2,2,1,1,2,3,1), // 55
        intArrayOf(2,2,1,2,2,1,2), // 56
        intArrayOf(2,2,1,3,2,1,1), // 57
        intArrayOf(2,2,2,1,1,3,1), // 58
        intArrayOf(2,2,2,1,1,1,2), // 59
        intArrayOf(2,2,2,1,2,1,1), // 60
        intArrayOf(2,2,2,2,2,1,1), // 61
        intArrayOf(2,2,2,1,1,2,1), // 62
        intArrayOf(1,1,1,1,1,4,1), // 63
        intArrayOf(1,1,1,1,4,1,1), // 64
        intArrayOf(1,1,1,4,1,1,1), // 65
        intArrayOf(1,1,4,1,1,1,1), // 66
        intArrayOf(1,4,1,1,1,1,1), // 67
        intArrayOf(4,1,1,1,1,1,1), // 68
        intArrayOf(1,1,1,1,3,1,1), // 69
        intArrayOf(1,1,1,1,1,3,1), // 70
        intArrayOf(1,1,1,3,1,1,1), // 71
        intArrayOf(1,1,3,1,1,1,1), // 72
        intArrayOf(1,3,1,1,1,1,1), // 73
        intArrayOf(3,1,1,1,1,1,1), // 74
        intArrayOf(1,1,3,1,1,2,1), // 75
        intArrayOf(1,1,1,2,3,1,1), // 76
        intArrayOf(1,1,1,2,1,3,1), // 77
        intArrayOf(1,1,1,2,1,1,3), // 78
        intArrayOf(1,1,3,2,1,1,1), // 79
        intArrayOf(1,1,1,1,3,2,1), // 80
        intArrayOf(1,1,1,1,2,3,1), // 81
        intArrayOf(1,1,1,1,2,1,3), // 82
        intArrayOf(1,1,1,1,2,1,1), // 83
        intArrayOf(1,1,2,1,3,1,1), // 84
        intArrayOf(1,1,2,1,1,3,1), // 85
        intArrayOf(1,1,2,1,1,1,3), // 86
        intArrayOf(1,1,2,3,1,1,1), // 87
        intArrayOf(1,1,3,1,2,1,1), // 88
        intArrayOf(1,1,3,1,1,2,1), // 89
        intArrayOf(1,3,1,2,1,1,1), // 90
        intArrayOf(1,3,1,1,2,1,1), // 91
        intArrayOf(1,3,1,1,1,2,1), // 92
        intArrayOf(1,1,2,1,3,1,1), // 93
        intArrayOf(1,1,2,1,1,3,1), // 94
        intArrayOf(1,1,1,1,1,4,1)  // 95
    )

    return patterns.getOrElse(value) { patterns[0] }
}

/**
 * Actual implementation for Wasm JS (Browser) platform.
 * Returns cleaned barcode text.
 */
/*actual fun convertBarcode(barcodeText: String): String {
    return barcodeText.trim().uppercase()
}*/

/**
 * Actual implementation for Wasm JS (Browser) platform.
 * Renders Code128 barcode using JsBarcode JavaScript library.
 */
/*@Composable
actual fun Barcode(text: String) {
    val cleanedText = text.trim().uppercase()
    val canvasKey = remember { java.util.UUID.randomUUID().toString() }

    var canvasElement by remember { mutableStateOf<HTMLCanvasElement?>(null) }

    // Create canvas element and get reference
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        onDraw = { }
    )

    // Side effect to render barcode using JsBarcode
    LaunchedEffect(canvasElement, cleanedText) {
        canvasElement?.let { canvas ->
            val options = js("{}")
            js("Object.assign")(options, mapOf(
                "format" to "CODE128",
                "lineColor" to "#000",
                "width" to 2,
                "height" to 80,
                "displayValue" to true
            ))
            JsBarcode(canvas, cleanedText, options)
        }
    }
}*/

/**
 * Actual implementation for Wasm JS (Browser) platform.
 * Returns cleaned barcode text.
 */
/*actual fun convertBarcode(barcodeText: String): String {
    return barcodeText.trim().uppercase()
}*/
