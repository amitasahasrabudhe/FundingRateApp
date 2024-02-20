package com.crypto.fundingrate.ui.fundingratescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crypto.fundingrate.ui.theme.FundingRateTheme

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.secondary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(
            modifier = Modifier
                .width(128.dp)
                .height(128.dp),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
        )
    }

}

@Preview
@Composable
fun LoadingScreenPreview() {
    FundingRateTheme {
        LoadingScreen()
    }
}