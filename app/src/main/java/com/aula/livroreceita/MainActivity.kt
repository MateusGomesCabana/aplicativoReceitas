package com.aula.livroreceita

import android.content.BroadcastReceiver
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var receitas:ArrayList<Receita>? = null
    private var receitaSelecionado:Receita? = null
    val MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10
    var receiver: BroadcastReceiver? = null
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
        lista.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, posicao, _ ->
            receitaSelecionado = receitas?.get(posicao)
            Toast.makeText(this, "Excluido", Toast.LENGTH_LONG).show()
            false
        }
        setupPermissions()
        configureReceiver()


    }

    override fun onCreateContextMenu( menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.menu_receita_contexto, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.excluir -> {
                AlertDialog.Builder(this@MainActivity)
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
            R.id.enviasms -> {
                val intentSms = Intent(Intent.ACTION_VIEW)
                intentSms.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                intentSms.data = Uri.parse("sms:" + receitaSelecionado?.autor)
                intentSms.putExtra("sms_body", "Mensagem")
                item.intent = intentSms
                return false
            }

            R.id.enviaemail -> {
                val intentEmail = Intent(Intent.ACTION_SEND)
                intentEmail.type = "message/rfc822"
                intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(receitaSelecionado?.autor!!))
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Teste de email")
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Corpo da mensagem")
                //item.setIntent(intentEmail);
                startActivity(Intent.createChooser(intentEmail, "Selecione a sua aplicação de Email"))
                return false
            }

            R.id.share -> {
                val intentShare = Intent(Intent.ACTION_SEND)
                intentShare.type = "text/plain"
                intentShare.putExtra(Intent.EXTRA_SUBJECT, "Assunto que será compartilhado")
                intentShare.putExtra(Intent.EXTRA_TEXT, "Texto que será compartilhado")
                startActivity(Intent.createChooser(intentShare, "Escolha como compartilhar"))
                return false
            }

            R.id.ligar -> {
                val intentLigar = Intent(Intent.ACTION_DIAL)
                intentLigar.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                intentLigar.data = Uri.parse("tel:32225449" )
                item.intent = intentLigar
                return false
            }

            R.id.visualizarmapa -> {
                //val gmmIntentUri = Uri.parse("geo:0,0?q=" + receitaSelecionado?.endereco)
                val gmmIntentUri = Uri.parse("geo:0,0?q= R. Antônio Alves, 25-28 " )
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
                return false
            }



            else -> return super.onContextItemSelected(item)
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

    private fun carregaLista() {
        receitas = ReceitaRepository(this).findAll()
        val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, receitas!!)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    override fun onResume() {
//        super.onResume()
//        val contatos = ContatoRepository(this).findAll()
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contatos)
//        var listaContatos = lista
//        listaContatos.setAdapter(adapter);
        super.onResume()
        receitas = ReceitaRepository(this).findAll()
        if(receitas != null){
            lista.adapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1,receitas!!)
        }
        registerForContextMenu(lista);
    }
    private fun setupPermissions() {

        val list = listOf<String>(
            Manifest.permission.RECEIVE_SMS
        )

        ActivityCompat.requestPermissions(this,
            list.toTypedArray(), MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_SMS)

        if (permission != PackageManager.GET_SERVICES) {
            Log.i("aula", "Permission to record denied")
        }
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction("com.aula.livroreceita")
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        receiver = SMSReceiver()
        registerReceiver(receiver, filter)
    }
}