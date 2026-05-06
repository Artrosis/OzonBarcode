package ozonbarcode

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import qrgenerator.barcodepainter.BarcodeFormat
import qrgenerator.barcodepainter.rememberBarcodePainter

@Composable
actual fun Barcode(
    modifier: Modifier,
    text: String
) {
    val barCodePainter = rememberBarcodePainter(
        content = text,
        format = BarcodeFormat.Code128,
        brush = SolidColor(Color.Black)
    )

    Image(
        painter = barCodePainter,
        contentDescription = null,
        modifier = modifier
    )
}