package com.crypto.fundingrate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crypto.fundingrate.domain.model.CryptoExchange
import com.crypto.fundingrate.presentation.fundingratescreen.FundingRateScreenViewModel
import com.crypto.fundingrate.ui.fundingratescreen.RateScreen
import com.crypto.fundingrate.ui.theme.FundingRateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
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
                    val coroutineScope = rememberCoroutineScope()

                    val tabIndex by remember {
                        derivedStateOf {
                            viewModel.state.exchange
                        }
                    }

                    val pagerState = rememberPagerState(pageCount = {
                        CryptoExchange.values().size
                    })

                    LaunchedEffect(pagerState) {
                        // Collect from the a snapshotFlow reading the currentPage
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            // Do something with each page change, for example:
                            Log.d("Page change", "Page changed to $page")
                            viewModel.updateExchange(CryptoExchange.fromInt(page))
                        }
                    }
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top) {
                        TabHeaders(tabIndex, pagerState, coroutineScope)
                        HorizontalPager(state = pagerState) { page ->
                            Log.d("MyPager", "Loading page $page")
                            FundingRatePager(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabHeaders(tabIndex: CryptoExchange, pagerState: PagerState, coroutineScope: CoroutineScope) {
    TabRow(
        selectedTabIndex = pagerState.currentPage
    ) {
        val tabResources: List<Pair<String, Painter>> = listOf (
            Pair(R.string.bybit_exchange_name, R.drawable.bybit_logo),
            Pair(R.string.binance_exchange_name, R.drawable.binance_exchange_logo),
            Pair(R.string.okx_exchange_name, R.drawable.okx_logo)
        ).map {  Pair(stringResource(id = it.first), painterResource(id = it.second)) }


        tabResources.forEachIndexed { index, tab ->
            TabHeader(
                selected = tabIndex.exchangeNumber == index,
                title = tab.first,
                painter = tab.second
            ) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        }
    }
}

@Composable
fun TabHeader(
    selected: Boolean,
    title: String,
    painter: Painter,
    onClick: () -> Unit
) {
    Tab(
        selected,
        onClick = onClick,
       // modifier = Modifier.weight(0.33f)
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .height(50.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
           /* Box(
                Modifier
                    .width(40.dp)
                    .height(20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null
                )
            }*/
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun FundingRatePager(viewModel: FundingRateScreenViewModel) {
    val state = viewModel.state
        RateScreen(
            state.getFundingRates(),
            state.isLoading
        ) {
            viewModel.getFundingRates()
        }
}
