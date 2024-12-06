package com.leonardo.carteiravirtual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.leonardo.carteiravirtual.helpers.SaldoManager

class MainActivity : AppCompatActivity() {
    private var saldoReais = 0.0
    private lateinit var saldoManager: SaldoManager
    private lateinit var saldoTextView: TextView
    /*
    // Registradores para as Activities
    private val depositarLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val novoSaldo = result.data?.getDoubleExtra("novoSaldo", saldo) ?: saldo
            saldo = novoSaldo
            updateSaldoTextView()
        }
    }

    private val converterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val novoSaldo = result.data?.getDoubleExtra("novoSaldo", saldo) ?: saldo
            saldo = novoSaldo
            updateSaldoTextView()
        }
    }

     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saldoTextView = findViewById(R.id.saldoTextView)
        saldoManager = SaldoManager(this)
        saldoManager.inicializarSaldosPadrao()


        val depositarButton = findViewById<Button>(R.id.depositarButton)
        val listarButton = findViewById<Button>(R.id.listarButton)
        val converterButton = findViewById<Button>(R.id.converterButton)
        atualizarSaldoReais()


        depositarButton.setOnClickListener {
            val intent = Intent(this, DepositarActivity::class.java)
            startActivity(intent)
        }

        listarButton.setOnClickListener {
            val intent = Intent(this, ListarRecursosActivity::class.java)
            startActivity(intent)
        }

        converterButton.setOnClickListener {
            val intent = Intent(this, ConverterRecursosActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        atualizarSaldoReais()
    }

    private fun atualizarSaldoReais() {
        val saldoReais = saldoManager.recuperarSaldo("BRL")
        saldoTextView.text = "Saldo: R$ %.2f".format(saldoReais)
    }

}
