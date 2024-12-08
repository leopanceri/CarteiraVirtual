package com.leonardo.carteiravirtual

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.leonardo.carteiravirtual.api.CurrencyApi
import com.leonardo.carteiravirtual.helpers.SaldoManager
import com.leonardo.carteiravirtual.models.Currency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConverterRecursosActivity : AppCompatActivity() {

    private lateinit var saldoManager: SaldoManager
    private lateinit var moedaOrigemSpinner: Spinner
    private lateinit var moedaDestinoSpinner: Spinner
    private lateinit var valorEditText: EditText
    private lateinit var converterButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var currencyApi: CurrencyApi
    private val moedas = listOf("BRL", "USD", "EUR", "BTC", "ETH")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter_recursos)

        saldoManager = SaldoManager(this)

        moedaOrigemSpinner = findViewById(R.id.origemSpinner)
        moedaDestinoSpinner = findViewById(R.id.destinoSpinner)
        valorEditText = findViewById(R.id.valorEditText)
        converterButton = findViewById(R.id.converterButton)
        progressBar = findViewById(R.id.progressBar)

        configurarSpinners()
        configurarBotaoConverter()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        currencyApi = retrofit.create(CurrencyApi::class.java)
    }

    private fun configurarSpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moedas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        moedaOrigemSpinner.adapter = adapter
        moedaDestinoSpinner.adapter = adapter
    }

    private fun configurarBotaoConverter() {
        converterButton.setOnClickListener {
            val moedaOrigem = moedaOrigemSpinner.selectedItem.toString()
            val moedaDestino = moedaDestinoSpinner.selectedItem.toString()
            val valor = valorEditText.text.toString().toDoubleOrNull()

            if (moedaOrigem == moedaDestino) {
                Toast.makeText(this, "Selecione moedas diferentes para conversão.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (valor == null || valor <= 0) {
                Toast.makeText(this, "Digite um valor válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val saldoOrigem = saldoManager.recuperarSaldo(moedaOrigem)
            if (valor > saldoOrigem) {
                Toast.makeText(this, "Saldo insuficiente na moeda de origem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            converterMoeda(moedaOrigem, moedaDestino, valor)
        }
    }

    private fun converterMoeda(moedaOrigem: String, moedaDestino: String, valor: Double) {
        progressBar.visibility = View.VISIBLE
            val pares = "$moedaDestino-$moedaOrigem"
            currencyApi.obterCotacao(pares).enqueue(object : Callback<List<Currency>> {
                override fun onResponse(call: Call<List<Currency>>, response: Response<List<Currency>>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val cotacao = response.body()
                        if (!cotacao.isNullOrEmpty()) {
                            val taxa = cotacao[0].bid.toDoubleOrNull()
                            if(taxa != null){
                                val result = valor / taxa
                                atualizarInterface(result, moedaDestino)
                                atualizarSaldos(moedaOrigem, moedaDestino, valor, result)
                            }else{
                                Toast.makeText(this@ConverterRecursosActivity, "Erro TAXA de conversão", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@ConverterRecursosActivity, "Erro resposta API", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@ConverterRecursosActivity, "Erro consulta API", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ConverterRecursosActivity,
                        "Erro de conexão: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun atualizarInterface(resultado: Double, destino: String) {
        Toast.makeText(
            this,
            "Valor convertido: %.2f %s".format(resultado, destino),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun atualizarSaldos(moedaOrigem: String, moedaDestino: String, valorOrigem: Double, valorConvertido: Double) {
        val saldoOrigem = saldoManager.recuperarSaldo(moedaOrigem) - valorOrigem
        val saldoDestino = saldoManager.recuperarSaldo(moedaDestino) + valorConvertido

        saldoManager.salvarSaldo(moedaOrigem, saldoOrigem)
        saldoManager.salvarSaldo(moedaDestino, saldoDestino)
    }
}