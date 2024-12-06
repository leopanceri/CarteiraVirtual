package com.leonardo.carteiravirtual

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leonardo.carteiravirtual.helpers.SaldoManager

class DepositarActivity : AppCompatActivity() {

    private lateinit var saldoManager: SaldoManager
    private lateinit var valorDepositoEditText: EditText
    private lateinit var depositarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depositar)

        saldoManager = SaldoManager(this)
        valorDepositoEditText = findViewById(R.id.valorEditText)
        depositarButton = findViewById(R.id.depositarButton)

        depositarButton.setOnClickListener {
            realizarDeposito()
        }
    }

    private fun realizarDeposito() {
        val valorTexto = valorDepositoEditText.text.toString()
        if (valorTexto.isBlank()) {
            Toast.makeText(this, "Digite um valor válido!", Toast.LENGTH_SHORT).show()
            return
        }

        val valorDeposito = valorTexto.toDoubleOrNull()
        if (valorDeposito == null || valorDeposito <= 0) {
            Toast.makeText(this, "Digite um valor maior que zero!", Toast.LENGTH_SHORT).show()
            return
        }

        val saldoAtual = saldoManager.recuperarSaldoReais()
        val novoSaldo = saldoAtual + valorDeposito
        saldoManager.salvarSaldoReais(novoSaldo)

        Toast.makeText(this, "Depósito realizado com sucesso!", Toast.LENGTH_SHORT).show()

        finish()
    }
}