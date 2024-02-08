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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crypto.fundingrate.domain.FundingRate
import com.crypto.fundingrate.ui.theme.FundingRateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: FundingRateScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FundingRateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.state
                    RateScreen(state.fundingRates)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableRow(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.onPrimary)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun LoadingScreen() {
    
}

@Composable
fun RateScreen(coins: List<FundingRate>) {
    // Each cell of a column must have the same weight.
    val column1Weight = .20f // 20%
    val column2Weight = .4f // 40%
    val column3Weight = .4f // 40%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)) {
        // Here is the header
        item {
            Row(Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                TableRow(text = "Coin", weight = column1Weight)
                TableRow(text = "Current", weight = column2Weight)
                TableRow(text = "Predicted", weight = column3Weight)
            }
        }
        // Here are all the lines of your table.
        items(coins) {
            val (coin, current, predicted) = it
            Row(Modifier.fillMaxWidth()) {
                TableRow(text = coin, weight = column1Weight)
                TableRow(text = current, weight = column2Weight)
                TableRow(text = predicted, weight = column3Weight)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RateScreenPreview() {
    FundingRateTheme {
        val sampleData = listOf(
            FundingRate("BTC", "+0.0100%", "+0.0100%"),
            FundingRate("ETH", "+0.0100%", "+0.0100%"),
            FundingRate("BNB", "+0.0169%", "+0.0180%")
        )
        RateScreen(sampleData)
    }
}