package ozonbarcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import qrgenerator.barcodepainter.BarcodeFormat
import qrgenerator.barcodepainter.rememberBarcodePainter

@Composable
actual fun Barcode(text: String) {

    val barCodePainter = rememberBarcodePainter(
        content = text,
        format = BarcodeFormat.Code128,
        brush = SolidColor(Color.Black)
    )

    Image(
        painter = barCodePainter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}