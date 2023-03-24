package com.example.hiddenghosts.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButton(
    text: String,
    colors: ButtonColors,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        contentPadding = paddingValues,
        shape = shape,
        colors = colors,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}