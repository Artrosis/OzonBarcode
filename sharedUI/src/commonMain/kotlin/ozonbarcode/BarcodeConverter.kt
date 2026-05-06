package ozonbarcode

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Barcode(
    modifier: Modifier = Modifier,
    text: String
)