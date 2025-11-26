package com.empresa.vitalogfinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.empresa.vitalogfinal.credenciais.Credenciais
import com.empresa.vitalogfinal.model.usuario.LoginResponse
import com.empresa.vitalogfinal.model.usuario.Usuario
import com.empresa.vitalogfinal.service.UsuarioService
import com.empresa.vitalogfinal.view.menu.MenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class LoginActivity : AppCompatActivity() {

    private lateinit var btnVoltar: Button
    private lateinit var btnLogin: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtSenha: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVoltar = findViewById(R.id.btnVoltar)
        btnLogin = findViewById(R.id.btnLogin)
        edtEmail = findViewById(R.id.edtEmail)
        edtSenha = findViewById(R.id.edtSenha)

        btnVoltar.setOnClickListener { finish() }

        btnLogin.setOnClickListener {
            realizarLogin()
        }
    }
    val cred = Credenciais()
    private fun realizarLogin() {
        val email = edtEmail.text.toString().trim()
        val senha = edtSenha.text.toString()

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha email e senha!", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(cred.ip)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(UsuarioService::class.java)


        val call = service.login(email, senha)


        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val usuario: Usuario? = body?.user

                    if (usuario != null) {
                        Toast.makeText(this@LoginActivity, "Bem-vindo!", Toast.LENGTH_LONG).show()


                        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        prefs.edit()
                            .putInt("user_id", usuario.id)
                            .putString("user_nome", usuario.nome)
                            .putString("user_email", usuario.email)
                            .apply()


                        val it = Intent(this@LoginActivity, MenuActivity::class.java)
                        startActivity(it)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, body?.message ?: "Resposta inesperada", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    when (response.code()) {
                        401 -> Toast.makeText(this@LoginActivity, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                        else -> {

                            val erro = response.errorBody()?.string()
                            Toast.makeText(this@LoginActivity, "Erro ${response.code()}: ${erro ?: "Erro no servidor"}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Falha ao conectar: ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("Retrofit",t.message.toString())
            }
        })
    }


    private fun enableEdgeToEdge() {

    }
}