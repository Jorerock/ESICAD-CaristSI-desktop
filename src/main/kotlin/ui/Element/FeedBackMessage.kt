package ui.Element

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackMessage(message: String, isError: Boolean) {
    val backgroundColor = if (isError) Color(0xFFFF6B6B) else Color(0xFF4CAF50)
    val textColor = Color.White

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = message,
            color = textColor,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.body1
        )
    }
}