package com.crypto.fundingrate

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crypto.fundingrate.data.remote.dto.formatNumberWithThousandsSeparator
import com.crypto.fundingrate.domain.model.FundingRate
import com.crypto.fundingrate.presentation.fundingratescreen.FundingRateScreenViewModel
import com.crypto.fundingrate.ui.theme.FundingRateTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
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
                        RateScreen(state.fundingRates, state.isLoading, viewModel::getFundingRates)
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

object ColumnConfig {
    // Each cell of a column must have the same weight.
    const val COLUMN_1_WEIGHT = .3f // 20%
    const val COLUMN_2_WEIGHT = .4f // 30%
    const val COLUMN_3_WEIGHT = .4f // 30%
}

@Composable
fun RateScreen(coins: List<FundingRate>, isLoading: Boolean, onRefresh: () -> Unit) {

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isLoading), onRefresh = onRefresh) {
        // The LazyColumn will be our table. Notice the use of the weights below
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)) {
            // Here is the header
            item {
                Row(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                    TableHeader(text = "Coin", weight = ColumnConfig.COLUMN_1_WEIGHT)
                    TableHeader(text = "Current", weight = ColumnConfig.COLUMN_2_WEIGHT)
                    TableHeader(text = "Volume", weight = ColumnConfig.COLUMN_3_WEIGHT)
                }
            }
            // Here are all the lines of your table.
            items(coins) {
                val (coin, predicted, volume) = it
                Row(Modifier.fillMaxWidth()) {
                    TableRow(text = coin, weight = ColumnConfig.COLUMN_1_WEIGHT)
                    TableRow(text = predicted, weight = ColumnConfig.COLUMN_2_WEIGHT)
                    TableRow(text = volume.formatNumberWithThousandsSeparator(), weight = ColumnConfig.COLUMN_3_WEIGHT)
                }
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
        RateScreen(sampleData, false) {}
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    FundingRateTheme {
        LoadingScreen()
    }
}