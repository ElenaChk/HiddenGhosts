package com.example.hiddenghosts.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hiddenghosts.MainViewModel
import com.example.hiddenghosts.R
import com.example.hiddenghosts.ui.components.DefaultButton

@Composable
fun ResultScreen(viewModel: MainViewModel, onNextLevelClick: () -> Unit) {

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.score.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.score),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        DefaultButton(
            modifier = Modifier
                .padding(all = 36.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.next_level),
            onClick = onNextLevelClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
        )
    }
}