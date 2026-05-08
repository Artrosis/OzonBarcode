package ozonbarcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ozonbarcode.sharedui.generated.resources.Res
import ozonbarcode.sharedui.generated.resources.download

@Composable
actual fun DesktopLink(
    modifier: Modifier
) {
    val uriHandler = LocalUriHandler.current
    TextButton(
        modifier = modifier,
        onClick = { uriHandler.openUri("https://github.com/Artrosis/OzonBarcode/releases/download/0.0.1/OzonBarcode.7z") },
    ) {
        Image(
            painterResource(Res.drawable.download),
            null,
            modifier = Modifier
                .size(40.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Настольная версия")
    }
}