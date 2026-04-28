import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import artrosis.App
import org.jetbrains.compose.resources.painterResource
import ozonbarcode.sharedui.generated.resources.Res
import ozonbarcode.sharedui.generated.resources.icon

fun main() = application {
    Window(
        title = "Озон штрихкод",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.icon)
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}

