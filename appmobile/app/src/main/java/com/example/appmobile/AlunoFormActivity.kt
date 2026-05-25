package com.example.appmobile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.io.IOException

class AlunoFormActivity : AppCompatActivity() {
    private val tag = "AlunoFormActivity"
    private val api = ApiClient.api

    private lateinit var layoutMatricula: TextInputLayout
    private lateinit var layoutNome: TextInputLayout
    private lateinit var inputMatricula: TextInputEditText
    private lateinit var inputNome: TextInputEditText
    private lateinit var btnSalvar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private lateinit var progressForm: ProgressBar

    private var isEdit = false
    private var matriculaAtual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aluno_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        layoutMatricula = findViewById(R.id.layoutMatricula)
        layoutNome = findViewById(R.id.layoutNome)
        inputMatricula = findViewById(R.id.inputMatricula)
        inputNome = findViewById(R.id.inputNome)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)
        progressForm = findViewById(R.id.progressForm)

        matriculaAtual = intent.getStringExtra(EXTRA_MATRICULA)
        val nomeAtual = intent.getStringExtra(EXTRA_NOME)
        isEdit = matriculaAtual != null

        if (isEdit) {
            inputMatricula.setText(matriculaAtual)
            inputMatricula.isEnabled = false
            inputNome.setText(nomeAtual)
            loadAluno(matriculaAtual!!)
        }

        btnSalvar.setOnClickListener { salvar() }
        btnCancelar.setOnClickListener { finish() }
    }

    private fun loadAluno(matricula: String) {
        lifecycleScope.launch {
            showLoading(true)
            try {
                val response = api.getAluno(matricula)
                if (response.isSuccessful) {
                    val aluno = response.body()
                    if (aluno != null) {
                        inputNome.setText(aluno.nome)
                    }
                } else {
                    showErrorByCode(response.code())
                }
            } catch (e: IOException) {
                Log.e(tag, "Erro de rede ao buscar aluno", e)
                showToast("Servidor indisponivel")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun salvar() {
        layoutMatricula.error = null
        layoutNome.error = null

        val matricula = inputMatricula.text?.toString()?.trim().orEmpty()
        val nome = inputNome.text?.toString()?.trim().orEmpty()

        if (!isEdit && matricula.isBlank()) {
            layoutMatricula.error = "Matricula obrigatoria"
            return
        }

        if (nome.isBlank()) {
            layoutNome.error = "Nome obrigatorio"
            return
        }

        lifecycleScope.launch {
            showLoading(true)
            try {
                val aluno = Aluno(matriculaAtual ?: matricula, nome)
                val response = if (isEdit) {
                    api.updateAluno(aluno.matricula, aluno)
                } else {
                    api.createAluno(aluno)
                }

                if (response.isSuccessful) {
                    finish()
                } else {
                    showErrorByCode(response.code())
                }
            } catch (e: IOException) {
                Log.e(tag, "Erro de rede ao salvar aluno", e)
                showToast("Servidor indisponivel")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressForm.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnSalvar.isEnabled = !isLoading
        btnCancelar.isEnabled = !isLoading
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

    companion object {
        const val EXTRA_MATRICULA = "extra_matricula"
        const val EXTRA_NOME = "extra_nome"
    }
}
