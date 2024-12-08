package com.leonardo.carteiravirtual

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leonardo.carteiravirtual.helpers.RecursoAdapter
import com.leonardo.carteiravirtual.helpers.SaldoManager
import com.leonardo.carteiravirtual.models.Recurso

class ListarRecursosActivity : AppCompatActivity() {
    private lateinit var recursosRecyclerView: RecyclerView
    private lateinit var saldoManager: SaldoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_recursos)

        saldoManager = SaldoManager(this)
        recursosRecyclerView = findViewById(R.id.listaRecursos)
        recursosRecyclerView.adapter = RecursoAdapter(this.obterRecursos(), this)
        recursosRecyclerView.layoutManager = LinearLayoutManager(this)
        recursosRecyclerView.setHasFixedSize(true)
        recursosRecyclerView.addItemDecoration(
            DividerItemDecoration(this, RecyclerView.VERTICAL)
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun obterRecursos(): List<Recurso> {
        val moedas = listOf("BRL", "USD", "EUR", "BTC", "ETH")
        val recursos = mutableListOf<Recurso>()
        moedas.forEach { moeda ->
            val saldo = saldoManager.recuperarSaldo(moeda)
            if(saldo != 0.0){
                recursos.add(Recurso(moeda, saldo))
            }
        }
        return recursos
    }
}