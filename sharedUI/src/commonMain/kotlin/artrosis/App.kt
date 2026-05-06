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
import ozonbarcode.sharedui.generated.resources.IndieFlower_Regular
import ozonbarcode.sharedui.generated.resources.Res
import ozonbarcode.SaveBarcode

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
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

        if (!convertedBarcode.isEmpty())
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
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
                        Barcode(convertedBarcode)
                        Text(
                            text = convertedBarcode,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                SaveBarcode(convertedBarcode)
            }
        }
    }
}

fun prepareBarcode(barcode: String): String = barcode.replace(" ", "")