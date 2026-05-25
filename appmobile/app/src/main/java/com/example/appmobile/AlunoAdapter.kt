package com.example.appmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class AlunoAdapter(
    private val onItemClick: (Aluno) -> Unit,
    private val onDeleteClick: (Aluno) -> Unit
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    private val items = mutableListOf<Aluno>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aluno, parent, false)
        return AlunoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<Aluno>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(aluno: Aluno) {
        val index = items.indexOfFirst { it.matricula == aluno.matricula }
        if (index >= 0) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNome: TextView = itemView.findViewById(R.id.textNome)
        private val textMatricula: TextView = itemView.findViewById(R.id.textMatricula)
        private val btnExcluir: MaterialButton = itemView.findViewById(R.id.btnExcluir)

        fun bind(aluno: Aluno) {
            textNome.text = aluno.nome
            textMatricula.text = aluno.matricula

            itemView.setOnClickListener { onItemClick(aluno) }
            btnExcluir.setOnClickListener { onDeleteClick(aluno) }
        }
    }
}
