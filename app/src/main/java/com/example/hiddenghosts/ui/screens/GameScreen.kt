package com.example.hiddenghosts.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hiddenghosts.MainViewModel
import com.example.hiddenghosts.R
import com.example.hiddenghosts.data.Cell
import com.example.hiddenghosts.data.CellState
import com.example.hiddenghosts.data.GameState
import com.example.hiddenghosts.data.GameProgressStatus
import com.example.hiddenghosts.ui.components.DefaultButton
import com.example.hiddenghosts.ui.theme.cellBgBlue
import com.example.hiddenghosts.ui.theme.cellBgGreen
import com.example.hiddenghosts.ui.theme.cellBgGrey
import com.example.hiddenghosts.ui.theme.cellBgRed
import com.example.hiddenghosts.utils.Constants
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: MainViewModel, onGameFinished: () -> Unit) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame()

        viewModel.gameFinishedEvent.collect {
            delay(state.gridAnimationDuration)
            onGameFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreText(score = state.score)
        RestartButton(
            modifier = Modifier.padding(top = 12.dp),
            enabled = state.status == GameProgressStatus.InProgress,
            onClick = viewModel::restartGame
        )
        Grid(
            state = state,
            onCellClick = viewModel::onCellClick
        )
    }
}

@Composable
private fun ScoreText(score: Int, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = score.toString(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun RestartButton(
    modifier: Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    DefaultButton(
        modifier = modifier,
        text = stringResource(id = R.string.restart),
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary
        ),
        paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    )
}

@Composable
private fun Grid(
    state: GameState,
    onCellClick: (Int) -> Unit
) {
    Box {
        Column {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                columns = GridCells.Fixed(state.level.columnCount),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(state.cells) { index, item ->
                    Cell(
                        cell = item,
                        currentIndex = index,
                        lastIndex = state.cells.lastIndex,
                        gameProgressStatus = state.status,
                        enabled = state.status == GameProgressStatus.InProgress,
                        onClick = { onCellClick(index) }
                    )
                }
            }
        }
        val resultImage = when (state.status) {
            GameProgressStatus.Success -> R.drawable.ic_result_success
            GameProgressStatus.Failure -> R.drawable.ic_result_failure
            else -> null
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = resultImage != null,
            enter = fadeIn() + scaleIn()
        ) {
            resultImage?.let {
                Image(
                    painter = painterResource(id = resultImage),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ColumnScope.Cell(
    cell: Cell,
    currentIndex: Int,
    lastIndex: Int,
    gameProgressStatus: GameProgressStatus,
    enabled: Boolean,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(targetValue = if (isVisible) 1F else 0F)
    val backgroundColor = animateColorAsState(
        targetValue = when (cell.state) {
            CellState.Idle -> MaterialTheme.colorScheme.cellBgGrey
            CellState.Preview -> MaterialTheme.colorScheme.cellBgBlue
            CellState.Success -> MaterialTheme.colorScheme.cellBgGreen
            CellState.Error -> MaterialTheme.colorScheme.cellBgRed
        }
    )
    val painterRes = when (cell.state) {
        CellState.Preview, CellState.Success -> cell.imageRes
        CellState.Error -> R.drawable.ic_ghost_wrong
        else -> null
    }

    LaunchedEffect(gameProgressStatus) {
        when (gameProgressStatus) {
            GameProgressStatus.Loading -> {
                delay(currentIndex * Constants.CELL_ANIMATION_DELAY)
                isVisible = true
            }
            GameProgressStatus.Failure, GameProgressStatus.Success -> {
                delay((lastIndex - currentIndex) * Constants.CELL_ANIMATION_DELAY)
                isVisible = false
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .alpha(alpha)
            .background(
                color = backgroundColor.value,
                shape = RoundedCornerShape(2.dp)
            )
            .weight(1f)
            .aspectRatio(1f)
            .clickable(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center
    ) {
        this@Cell.AnimatedVisibility(
            visible = painterRes != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            painterRes?.let {
                Image(
                    painter = painterResource(id = painterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

