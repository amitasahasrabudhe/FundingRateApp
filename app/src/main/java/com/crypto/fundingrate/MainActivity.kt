package com.crypto.fundingrate

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crypto.fundingrate.data.remote.dto.formatNumberWithThousandsSeparator
import com.crypto.fundingrate.domain.model.FundingRate
import com.crypto.fundingrate.presentation.fundingratescreen.FundingRateScreenViewModel
import com.crypto.fundingrate.ui.theme.FundingRateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FundingRateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: FundingRateScreenViewModel by viewModels()
                    val state = viewModel.state
                    if (state.isLoading) {
                        LoadingScreen()
                    } else {
                        RateScreen(state.fundingRates)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableHeader(
    text: String,
    weight: Float
) {
    TableRow(text, weight, true)
}
@Composable
fun RowScope.TableRow(
    text: String,
    weight: Float,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Light,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onPrimary)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun RateScreen(coins: List<FundingRate>) {
    // Each cell of a column must have the same weight.
    val column1Weight = .30f // 20%
    val column2Weight = .4f // 30%
    val column3Weight = .4f // 30%

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer)) {
        // Here is the header
        item {
            Row(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                TableHeader(text = "Coin", weight = column1Weight)
                TableHeader(text = "Current", weight = column2Weight)
                TableHeader(text = "Volume", weight = column3Weight)
            }
        }
        // Here are all the lines of your table.
        items(coins) {
            val (coin, predicted, volume) = it
            Row(Modifier.fillMaxWidth()) {
                TableRow(text = coin, weight = column1Weight)
                TableRow(text = predicted, weight = column2Weight)
                TableRow(text = volume.formatNumberWithThousandsSeparator(), weight = column3Weight)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RateScreenPreview() {
    FundingRateTheme {
        val sampleData = listOf(
            FundingRate("BTC", "+0.0100%", 994612300),
            FundingRate("ETH", "+0.0100%", 894612300),
            FundingRate("BNB", "+0.0180%", 794612300)
        )
        RateScreen(sampleData)
    }
}