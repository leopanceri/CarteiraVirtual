package com.leonardo.carteiravirtual.helpers

import android.content.Context
import android.content.SharedPreferences

class SaldoManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("CarteiraVirtual", Context.MODE_PRIVATE)

    companion object {
        private const val BRL = "BRL"
        private const val USD = "USD"
        private const val EUR = "EUR"
        private const val BTC = "BTC"
        private const val ETH = "ETH"
    }

    fun salvarSaldo(key: String, valor: Double) {
        preferences.edit().putFloat(key, valor.toFloat()).apply()
    }

    fun recuperarSaldo(key: String): Double {
        return preferences.getFloat(key, 0.0f).toDouble()
    }

    // salvar saldos individuais
    fun salvarSaldoReais(valor: Double) = salvarSaldo(BRL, valor)
    fun salvarSaldoDolar(valor: Double) = salvarSaldo(USD, valor)
    fun salvarSaldoEuro(valor: Double) = salvarSaldo(EUR, valor)
    fun salvarSaldoBitcoin(valor: Double) = salvarSaldo(BTC, valor)
    fun salvarSaldoEther(valor: Double) = salvarSaldo(ETH, valor)

    // recuperar saldos individuais
    fun recuperarSaldoReais(): Double = recuperarSaldo(BRL)
    fun recuperarSaldoDolar(): Double = recuperarSaldo(USD)
    fun recuperarSaldoEuro(): Double = recuperarSaldo(EUR)
    fun recuperarSaldoBitcoin(): Double = recuperarSaldo(BTC)
    fun recuperarSaldoEther(): Double = recuperarSaldo(ETH)

    // inicializar saldos  no primeiro uso
    fun inicializarSaldosPadrao() {
        if (preferences.contains(BRL)) return
        salvarSaldoReais(0.00)
        salvarSaldoDolar(0.00)
        salvarSaldoEuro(0.00)
        salvarSaldoBitcoin(0.00)
        salvarSaldoEther(0.00)
    }
}