package com.empresa.vitalogfinal.view.menu.diario

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.empresa.vitalogfinal.R
import com.empresa.vitalogfinal.credenciais.Credenciais
import com.empresa.vitalogfinal.model.diario.FoodModel
import com.empresa.vitalogfinal.model.diario.GrupoModel
import com.empresa.vitalogfinal.repository.GrupoRepository
import com.empresa.vitalogfinal.service.GrupoService
import com.empresa.vitalogfinal.view.menu.ui.DetalhesGrupoViewModel
import com.empresa.vitalogfinal.view.menu.ui.DetalhesGrupoViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetalhesGrupoActivity : AppCompatActivity() {

    private lateinit var viewModel: DetalhesGrupoViewModel
    private lateinit var adapter: AlimentosAdapter

    private var grupoId = 0
    private var grupoNome = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_grupo)

        // pegar dados da intent
        grupoId = intent.getIntExtra("grupoId", 0)
        grupoNome = intent.getStringExtra("grupoNome") ?: ""

        val txtNomeGrupo = findViewById<TextView>(R.id.txt_nome_grupo)
        val recycler = findViewById<RecyclerView>(R.id.recycler_alimentos)
        val btnAdd = findViewById<Button>(R.id.btn_add_alimento)
        val btnApagarGrupo = findViewById<Button>(R.id.btn_apagar_grupo)

        val btnVoltar = findViewById<Button>(R.id.btn_voltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        txtNomeGrupo.text = grupoNome

        adapter = AlimentosAdapter(emptyList()) { alimento ->
            editarAlimento(alimento)
        }
        val cred = Credenciais()
        val retrofit = Retrofit.Builder()
            .baseUrl(cred.ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(
            this,
            DetalhesGrupoViewModelFactory(GrupoRepository(retrofit.create(
                GrupoService::class.java)))
        )[DetalhesGrupoViewModel::class.java]

        viewModel.alimentos.observe(this) { lista ->
            adapter.update(lista)
        }


        // Carregar dados
        lifecycleScope.launch {
            viewModel.carregar(grupoId)
        }


        btnAdd.setOnClickListener {
            adicionarAlimento()
        }

        btnApagarGrupo.setOnClickListener {
            lifecycleScope.launch {
                val ok = viewModel.apagarGrupo(grupoId)
                if (ok) {
                    Toast.makeText(this@DetalhesGrupoActivity, "Grupo apagado!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun editarAlimento(alimento: FoodModel) {
        Toast.makeText(this, "Editar ${alimento.nome}", Toast.LENGTH_SHORT).show()
        // abrir EditAlimentoActivity (criamos já já)
    }

    private fun adicionarAlimento() {
        Toast.makeText(this, "Adicionar alimento", Toast.LENGTH_SHORT).show()
        // abrir AddAlimentoActivity (próximo passo)
    }
}
