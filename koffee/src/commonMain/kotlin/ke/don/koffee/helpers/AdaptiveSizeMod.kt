package ke.don.koffee.helpers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val COMPACT_BREAK_POINT = 600.dp
val MAX_NON_COMPACT_WIDTH = 420.dp

fun sizeMod(isCompact: Boolean): Modifier {
    return if (isCompact) {
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // outer margin on mobile
    } else {
        Modifier
            .wrapContentWidth()
            .widthIn(max = MAX_NON_COMPACT_WIDTH) // cap on larger screens
    }
}