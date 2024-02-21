package com.crypto.fundingrate.domain.model

enum class CryptoExchange(val exchangeNumber: Int) {
    BYBIT(0),
    BINANCE(1),
    OKX(2);

    companion object {
        fun fromInt(value: Int) = CryptoExchange.values().first { it.exchangeNumber == value }
    }
}