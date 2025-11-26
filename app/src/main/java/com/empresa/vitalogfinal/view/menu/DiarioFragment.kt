package com.empresa.vitalogfinal.view.menu  // ajuste se necessário

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empresa.vitalogfinal.R
import com.empresa.vitalogfinal.credenciais.Credenciais
import com.empresa.vitalogfinal.model.diario.GrupoModel

import com.empresa.vitalogfinal.repository.DiarioRepository
import com.empresa.vitalogfinal.service.DiarioService
import com.empresa.vitalogfinal.view.menu.diario.CriarGrupoActivity
import com.empresa.vitalogfinal.view.menu.diario.DetalhesGrupoActivity
import com.empresa.vitalogfinal.view.menu.diario.DiarioAdapter
import com.empresa.vitalogfinal.view.menu.ui.DiarioViewModel
import com.empresa.vitalogfinal.view.menu.ui.DiarioViewModelFactory
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DiarioFragment : Fragment() {

    private lateinit var viewModel: DiarioViewModel
    private lateinit var adapter: DiarioAdapter

    private var selectedDate: LocalDate = LocalDate.now()
    private val usuarioId = 1 // depois você coloca o id real do usuário logado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPrev = view.findViewById<Button>(R.id.btn_prev_day)
        val btnNext = view.findViewById<Button>(R.id.btn_next_day)
        val btnAddGrupo = view.findViewById<Button>(R.id.btn_add_grupo)
        val txtDate = view.findViewById<TextView>(R.id.txt_selected_date)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_diario)

        adapter = DiarioAdapter(emptyList()) { grupo ->
            abrirDetalhesGrupo(grupo)
        }

        val cred = Credenciais()
        val retrofit = Retrofit.Builder()
            .baseUrl(cred.ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // ViewModel
        viewModel = ViewModelProvider(
            this,
            DiarioViewModelFactory(DiarioRepository(retrofit.create(DiarioService::class.java)))
        )[DiarioViewModel::class.java]

        // Observa os dados
        viewModel.grupos.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        // Carregar dia atual
        txtDate.text = selectedDate.toString()
        carregarDiaAtual()

        btnPrev.setOnClickListener {
            selectedDate = selectedDate.minusDays(1)
            txtDate.text = selectedDate.toString()
            carregarDiaAtual()
        }

        btnNext.setOnClickListener {
            selectedDate = selectedDate.plusDays(1)
            txtDate.text = selectedDate.toString()
            carregarDiaAtual()
        }

        // ➕ Botão de adicionar grupo
        btnAddGrupo.setOnClickListener {
            val intent = Intent(requireContext(), CriarGrupoActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun carregarDiaAtual() {
        lifecycleScope.launch {
            viewModel.carregarDiario(usuarioId, selectedDate.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val nomeGrupo = data?.getStringExtra("nome_grupo") ?: return

            lifecycleScope.launch {
                val novoGrupo = viewModel.criarGrupo(usuarioId, nomeGrupo)
                if (novoGrupo != null) {
                    Toast.makeText(requireContext(), "Grupo criado!", Toast.LENGTH_SHORT).show()
                    // Atualiza localmente sem recarregar
                    viewModel.adicionarGrupoLocal(novoGrupo)
                } else {
                    Toast.makeText(requireContext(), "Erro ao criar grupo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun abrirDetalhesGrupo(grupo: GrupoModel) {
        val intent = Intent(requireContext(), DetalhesGrupoActivity::class.java)
        intent.putExtra("grupoId", grupo.id)
        intent.putExtra("grupoNome", grupo.nome)
        startActivity(intent)
    }
}

