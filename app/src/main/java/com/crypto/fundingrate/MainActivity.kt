package com.crypto.fundingrate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.crypto.fundingrate.presentation.fundingratescreen.FundingRateScreenViewModel
import com.crypto.fundingrate.domain.model.CryptoExchange
import com.crypto.fundingrate.ui.fundingratescreen.LoadingScreen
import com.crypto.fundingrate.ui.fundingratescreen.RateScreen
import com.crypto.fundingrate.ui.theme.FundingRateTheme
import dagger.hilt.android.AndroidEntryPoint

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
                    val pagerState = rememberPagerState(pageCount = {
                        3
                    })
                    LaunchedEffect(pagerState) {
                        // Collect from the a snapshotFlow reading the currentPage
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            // Do something with each page change, for example:
                            // viewModel.sendPageSelectedEvent(page)
                            Log.d("Page change", "Page changed to $page")
                            viewModel.updateExchange(CryptoExchange.fromInt(page))
                        }
                    }

                    HorizontalPager(state = pagerState) { page ->
                        if (page == 2) {
                            Text("Work in progress on page $page")
                        } else {
                            Log.d("MyPager", "Loading page $page")
                            FundingRatePagerScreen(viewModel = viewModel)
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun FundingRatePagerScreen(viewModel: FundingRateScreenViewModel) {
    val state = viewModel.state
    if (state.isLoading) {
        LoadingScreen()
    } else {
        RateScreen(
            state.getFundingRates(),
            state.isLoading
        ) {
            viewModel.getFundingRates()
        }
    }
}
