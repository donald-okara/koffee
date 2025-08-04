package ke.don.koffee_demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.koffee.domain.Koffee

@Composable
fun TestToasts(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                Koffee.show("Hello!", "This is your first Koffee toast.")
            }
        ) {
            Text("Show Hello")
        }
        Button(
            onClick = {
                Koffee.show("Aye aye!", "This is him tho.")
            }
        ) {
            Text("Show What's up")
        }
        Button(
            onClick = {
                Koffee.show("Goodbye!", "See you later.")
            }
        ) {
            Text("Show goodbye")
        }
    }
}