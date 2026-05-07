package ozonbarcode

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement
import ozonbarcode.jsinterop.BarcodeGenerator

/**
 * Actual implementation for Wasm JS (Browser) platform.
 * Renders Code128 barcode using JsBarcode JavaScript library.
 */
@OptIn(ExperimentalWasmJsInterop::class, ExperimentalComposeUiApi::class)
@Composable
actual fun Barcode(
    modifier: Modifier,
    text: String
) {
    Box(
        modifier = modifier
    ) {
        WebElementView(
            factory = {
                (document.createElement("canvas")
                        as HTMLCanvasElement)
            },
            modifier = Modifier.fillMaxSize(),
            update = { canvas ->
                BarcodeGenerator.generateCode128(
                    element = canvas,
                    value = text,
                    width = 2,
                    height = 100
                )
            }
        )
    }
}