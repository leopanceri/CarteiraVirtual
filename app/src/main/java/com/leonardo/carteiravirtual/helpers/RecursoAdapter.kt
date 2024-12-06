package com.leonardo.carteiravirtual.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leonardo.carteiravirtual.R
import com.leonardo.carteiravirtual.models.Recurso


class RecursoAdapter(
    private val recursos: List<Recurso>,
    private val context: Context
): RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    inner class RecursoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recursoNome = itemView.findViewById<TextView>(R.id.coin_name)
        val recursoSaldo = itemView.findViewById<TextView>(R.id.coin_saldo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return RecursoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recursos.size
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.recursoNome.text = recurso.nome
        holder.recursoSaldo.text = String.format("%.2f",recurso.saldo)
    }

}