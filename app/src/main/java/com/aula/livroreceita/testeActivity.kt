package com.aula.livroreceita

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import kotlinx.android.synthetic.main.activity_grid.*
import kotlinx.android.synthetic.main.activity_item_grid.*
import kotlinx.android.synthetic.main.activity_main.*


class testeActivity : AppCompatActivity() {
    private var receitas:ArrayList<Receita>? = null
    private var receitaSelecionado:Receita? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        toolbar2.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        gridView.setOnItemClickListener { _, _, position, id ->
            val intent = Intent(this@testeActivity, ViewActivity::class.java)
            intent.putExtra("receita", receitas?.get(position))
            startActivity(intent)
        }

    }
    override fun onResume() {
        super.onResume()
        receitas = ReceitaRepository(this).findAll()
        if(receitas != null) {
            val adapter = GridAdapater(applicationContext, receitas!!, assets)
            gridView.adapter = adapter
        }
        registerForContextMenu(gridView);
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.novo -> {
                val intent = Intent(this, ReceitaActivity::class.java)
                startActivity(intent)
                return false

            }

            R.id.preferencias -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return false

            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //select
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.menu_receita_contexto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.excluir -> {
                AlertDialog.Builder(this@testeActivity)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("Deletar")
                    .setMessage("Deseja mesmo deletar ?")
                    .setPositiveButton("Quero"
                    ) { _, _ ->
                        ReceitaRepository(this).delete(this.receitaSelecionado!!.id)
                        carregaLista()
                    }.setNegativeButton("Nao", null).show()
                return false
            }

            R.id.enviaemail -> {
                val intentEmail = Intent(Intent.ACTION_SEND)
                intentEmail.type = "message/rfc822"
                intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(receitaSelecionado?.email!!))
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Mensagem para o autor")
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Corpo da mensagem")
                //item.setIntent(intentEmail);
                startActivity(Intent.createChooser(intentEmail, "Selecione a sua aplicação de Email"))
                return false
            }


            else -> return super.onContextItemSelected(item)
        }
    }

    private fun carregaLista() {
        receitas = ReceitaRepository(this).findAll()
        val adapter = GridAdapater(applicationContext, receitas!!, assets)
        gridView.adapter = adapter
        adapter.notifyDataSetChanged()

        // val adapter = ListaReceitaAdapater(applicationContext, receitas!!, assets)
        //lista.adapter = adapter
    }
}