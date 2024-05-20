package com.ia.diariodenoticias.android.screens.articles


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.ia.diariodenoticias.articles.usecase.Article
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import org.koin.androidx.compose.koinViewModel
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

@Composable
fun ArticlesScreen(
    onAboutButtonClick: () -> Unit,
    onSourcesButtonClick: () -> Unit,
    articlesViewModel: ArticlesViewModel = koinViewModel(),
) {
    val articlesState = articlesViewModel.articlesState.collectAsState()

    Column {
        AppBar(onAboutButtonClick, onSourcesButtonClick)

        if (articlesState.value.error != null)
            ErrorMessage(articlesState.value.error!!)
        if (articlesState.value.articles.isNotEmpty())
            ArticlesListView(articlesViewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onAboutButtonClick: () -> Unit,
    onSourcesButtonClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "Articles", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp // Larger size
        ),   modifier = Modifier
            .fillMaxWidth()
            .padding(end = 60.dp, top = 16.dp)
            .wrapContentWidth(Alignment.End)) },
        actions = {
            IconButton(onClick = onSourcesButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.List,
                    contentDescription = "Sources Button",
                )
            }
            IconButton(onClick = onAboutButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "About Device Button",
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticlesListView(viewModel: ArticlesViewModel) {

    val pullRefreshState = rememberPullRefreshState(viewModel.articlesState.value.loading, { viewModel.getArticles(true) })

    Box(modifier = Modifier
        .pullRefresh(pullRefreshState)) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.articlesState.value.articles) { article ->
                ArticleItemView(article = article)
            }
        }

        PullRefreshIndicator(refreshing = viewModel.articlesState.value.loading, state = pullRefreshState, Modifier.align(Alignment.TopCenter))

    }
}


@Composable
fun ArticleItemView(article: Article) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp) // Define a elevação para o efeito de cartão
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = article.imageUrl,
                contentDescription = null
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                    CircularProgressIndicator()
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.desc)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.date,
                style = TextStyle(color = Color.Gray),
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
        )
    }
}
