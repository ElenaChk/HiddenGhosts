package com.example.hiddenghosts.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hiddenghosts.R
import com.example.hiddenghosts.ui.components.DefaultButton
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    onStartButtonClick: () -> Unit
) {
    var isLoading by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
    }
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = {
            fadeIn(animationSpec = tween(500, delayMillis = 90)) +
                    scaleIn(initialScale = 0.92f, animationSpec = tween(500, delayMillis = 90)) with
                    fadeOut(animationSpec = tween(500))
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (it) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null
                )
            } else {
                DefaultButton(
                    modifier = Modifier
                        .padding(horizontal = 60.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.start_game),
                    onClick = onStartButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    paddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                )
            }
        }
    }
}