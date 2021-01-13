package com.aula.livroreceita

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var receitas:ArrayList<Receita>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // val contatos = arrayOf("Maria", "José", "Carlos")
//        val contatos = ContatoRepository(this).findAll()
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
//        var listaContatos = lista
//        listaContatos.setAdapter(adapter);
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        lista.setOnItemClickListener { _, _, position, id ->
            val intent = Intent(this@MainActivity, ReceitaActivity::class.java)
            intent.putExtra("receita", receitas?.get(position))
            startActivity(intent)

        }

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

            R.id.sincronizar -> {
                Toast.makeText(this, "Enviar", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.receber -> {
                Toast.makeText(this, "Receber", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.mapa -> {
                Toast.makeText(this, "Mapa", Toast.LENGTH_LONG).show()
                return false
            }

            R.id.preferencias -> {
                Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show()
                return false
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
//        super.onResume()
//        val contatos = ContatoRepository(this).findAll()
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
//        var listaContatos = lista
//        listaContatos.setAdapter(adapter);
        super.onResume()
        receitas = ReceitaRepository(this).findAll()
        lista.adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,receitas!!)
    }
}