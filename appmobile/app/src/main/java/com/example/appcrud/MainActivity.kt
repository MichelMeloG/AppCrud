package com.example.appmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val api = ApiClient.api

    private lateinit var adapter: AlunoAdapter
    private lateinit var recyclerAlunos: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private lateinit var btnAdd: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerAlunos = findViewById(R.id.recyclerAlunos)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.emptyView)
        btnAdd = findViewById(R.id.btnAdd)

        adapter = AlunoAdapter(
            onItemClick = { aluno -> openForm(aluno) },
            onDeleteClick = { aluno -> deleteAluno(aluno) }
        )

        recyclerAlunos.layoutManager = LinearLayoutManager(this)
        recyclerAlunos.adapter = adapter

        btnAdd.setOnClickListener { openForm(null) }
    }

    override fun onResume() {
        super.onResume()
        loadAlunos()
    }

    private fun openForm(aluno: Aluno?) {
        val intent = Intent(this, AlunoFormActivity::class.java)
        if (aluno != null) {
            intent.putExtra(AlunoFormActivity.EXTRA_MATRICULA, aluno.matricula)
            intent.putExtra(AlunoFormActivity.EXTRA_NOME, aluno.nome)
        }
        startActivity(intent)
    }

    private fun loadAlunos() {
        lifecycleScope.launch {
            showLoading(true)
            try {
                val response = api.listAlunos()
                if (response.isSuccessful) {
                    val alunos = response.body().orEmpty()
                    adapter.setItems(alunos)
                    emptyView.visibility = if (alunos.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    showErrorByCode(response.code())
                }
            } catch (e: IOException) {
                Log.e(tag, "Erro de rede ao listar alunos", e)
                showToast("Servidor indisponivel")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun deleteAluno(aluno: Aluno) {
        lifecycleScope.launch {
            showLoading(true)
            try {
                val response = api.deleteAluno(aluno.matricula)
                if (response.isSuccessful) {
                    adapter.removeItem(aluno)
                    emptyView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
                } else {
                    showErrorByCode(response.code())
                }
            } catch (e: IOException) {
                Log.e(tag, "Erro de rede ao excluir aluno", e)
                showToast("Servidor indisponivel")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorByCode(code: Int) {
        val message = when (code) {
            404 -> "Aluno nao encontrado"
            409 -> "Matricula ja cadastrada"
            else -> "Falha na operacao"
        }
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}