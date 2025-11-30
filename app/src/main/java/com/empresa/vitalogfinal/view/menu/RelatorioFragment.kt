package com.empresa.vitalogfinal.view.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.empresa.vitalogfinal.R
import com.empresa.vitalogfinal.credenciais.Credenciais
import com.empresa.vitalogfinal.service.UsuarioService
// Importe a Activity correta de Edição
import com.empresa.vitalogfinal.view.menu.perfil.EditarDadosActivity
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RelatorioFragment : Fragment() {

    private lateinit var layoutFaltando: LinearLayout
    private lateinit var layoutConteudo: ScrollView
    private lateinit var txtImc: TextView
    private lateinit var txtClassImc: TextView
    private lateinit var txtAguaIdeal: TextView

    private var usuarioId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relatorio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        usuarioId = prefs.getInt("user_id", 0)

        layoutFaltando = view.findViewById(R.id.layout_dados_faltando)
        layoutConteudo = view.findViewById(R.id.layout_relatorio_conteudo)
        txtImc = view.findViewById(R.id.txt_valor_imc)
        txtClassImc = view.findViewById(R.id.txt_class_imc)
        txtAguaIdeal = view.findViewById(R.id.txt_agua_ideal)

        val btnCompletar = view.findViewById<Button>(R.id.btn_completar_cadastro)

        // CORREÇÃO: Agora aponta para a tela correta de Editar Dados
        btnCompletar.setOnClickListener {
            startActivity(Intent(requireContext(), EditarDadosActivity::class.java))
        }

        // Verifica dados ao abrir
        verificarPerfil()
    }

    override fun onResume() {
        super.onResume()
        verificarPerfil() // Recarrega caso tenha voltado da tela de edição
    }

    private fun verificarPerfil() {
        val cred = Credenciais()
        val retrofit = Retrofit.Builder()
            .baseUrl(cred.ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceUser = retrofit.create(UsuarioService::class.java)

        lifecycleScope.launch {
            try {
                val response = serviceUser.getPerfil(usuarioId)
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Verifica se tem peso e altura cadastrados
                        if (user.peso != null && user.altura != null && user.peso > 0 && user.altura > 0) {
                            // TEM DADOS -> MOSTRA RELATÓRIO
                            layoutFaltando.visibility = View.GONE
                            layoutConteudo.visibility = View.VISIBLE
                            calcularEstatisticas(user.peso, user.altura)
                            carregarHistorico7Dias()
                        } else {
                            // NÃO TEM -> MOSTRA BOTÃO COMPLETAR
                            layoutFaltando.visibility = View.VISIBLE
                            layoutConteudo.visibility = View.GONE
                        }
                    }
                }
            } catch (e: Exception) {
                // Tratamento de erro silencioso ou log
                e.printStackTrace()
            }
        }
    }

    private fun calcularEstatisticas(peso: Double, altura: Double) {
        // 1. Água Ideal (Peso * 35ml)
        val aguaIdeal = peso * 35
        txtAguaIdeal.text = "${String.format("%.0f", aguaIdeal)} ml"

        // 2. IMC (Peso / Altura²)
        val imc = peso / (altura * altura)
        txtImc.text = String.format("%.1f", imc)

        val classificacao = when {
            imc < 18.5 -> "Abaixo do peso"
            imc < 24.9 -> "Peso normal"
            imc < 29.9 -> "Sobrepeso"
            else -> "Obesidade"
        }
        txtClassImc.text = classificacao
    }

    private fun carregarHistorico7Dias() {
        // Lógica futura para o gráfico/lista de 7 dias
        // Aqui você chamará o RelatorioService.getSemanal(usuarioId)
    }
}