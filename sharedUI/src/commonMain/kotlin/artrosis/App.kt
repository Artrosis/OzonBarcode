package artrosis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import artrosis.theme.AppTheme
import org.jetbrains.compose.resources.Font
import ozonbarcode.Barcode
import ozonbarcode.DesktopLink
import ozonbarcode.SaveBarcode
import ozonbarcode.hasCaption
import ozonbarcode.sharedui.generated.resources.IndieFlower_Regular
import ozonbarcode.sharedui.generated.resources.Res

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Генератор ШК Озон",
                fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
                style = MaterialTheme.typography.displayLarge
            )

            var barcodeText by remember { mutableStateOf("") }

            OutlinedTextField(
                value = barcodeText,
                onValueChange = {
                    barcodeText = it
                },
                label = { Text("Вставьте Озон код") },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp)
            )

            val convertedBarcode = prepareBarcode(barcodeText)

            if (!convertedBarcode.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                        border = BorderStroke(width = 1.dp, color = Color.Gray),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Штрих-код:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Barcode(
                                modifier = Modifier
                                    .sizeIn(maxWidth = 700.dp)
                                    .height(300.dp)
                                    .padding(16.dp),
                                convertedBarcode
                            )
                            if (hasCaption()) {
                                Text(
                                    text = convertedBarcode,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                    SaveBarcode(convertedBarcode)
                }
            }
        }

        DesktopLink(
            modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .widthIn(min = 200.dp)
        )
    }
}

fun prepareBarcode(barcode: String): String = barcode.replace(" ", "")