package com.aula.livroreceita

import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import com.baoyz.swipemenulistview.SwipeMenuListView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var receitas:ArrayList<Receita>? = null
    private var receitaSelecionado:Receita? = null
    val MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10
    var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        lista.setOnItemClickListener { _, _, position, id ->
            val intent = Intent(this@MainActivity, ViewActivity::class.java)
            intent.putExtra("receita", receitas?.get(position))
            startActivity(intent)

        }


        lista.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, posicao, _ ->
            receitaSelecionado = receitas?.get(posicao)
            Toast.makeText(this, "Excluido", Toast.LENGTH_LONG).show()
            false
        }

        lista.setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener { position, menu, index ->
            Toast.makeText(this, "$position", Toast.LENGTH_LONG).show()
            when (index) {
                //editar
                0 -> {
                     val intent = Intent(this@MainActivity, ReceitaActivity::class.java)
                     intent.putExtra("receita", receitas?.get(position))
                     startActivity(intent)
                }
                //deletar
                1 -> {
                    AlertDialog.Builder(this@MainActivity)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar ?")
                        .setPositiveButton("Quero"
                        ) { _, _ ->
                            ReceitaRepository(this).delete(receitas?.get(position)!!.id)
                            carregaLista()
                        }.setNegativeButton("Nao", null).show()
                }
            }
            // false : close the menu; true : not close the menu
            false
        })
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.novo -> {
                val intent = Intent(this, ViewActivity::class.java)
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
      //  val adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, receitas!!)
        val adapter = ListaReceitaAdapater(applicationContext, receitas!!, assets)
        lista?.adapter = adapter
        adapter.notifyDataSetChanged()

       // val adapter = ListaReceitaAdapater(applicationContext, receitas!!, assets)
        //lista.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        receitas = ReceitaRepository(this).findAll()
        if(receitas != null){
            val adapter = ListaReceitaAdapater(applicationContext, receitas!!, assets)
            lista.adapter = adapter
            //swipe das linhas
            val creator: SwipeMenuCreator = object : SwipeMenuCreator {
                override fun create(menu: SwipeMenu) {
                    // cria a opção de editar item
                    val openItem = SwipeMenuItem(applicationContext)
                    // set item background
                    openItem.setBackground(ColorDrawable(Color.rgb(0x00, 0x66,0xff)))
                    // set item width
                    openItem.setWidth(170)
                    // set item title
                    openItem.setTitle("Editar")
                    // set item title fontsize
                    openItem.setTitleSize(18)
                    // set item title font color
                    openItem.setTitleColor(Color.WHITE)
                    // add to menu
                    menu.addMenuItem(openItem)

                    // cria a opção de deletar item
                    val deleteItem = SwipeMenuItem(applicationContext)
                    // background
                    deleteItem.setBackground(ColorDrawable(Color.rgb(0xF9,0x3F,0x25)))
                    // largura
                    deleteItem.setWidth(170)
                    deleteItem.setTitle("Deletar")
                    // icone
                    deleteItem.setIcon(R.drawable.ic_receber)
                    // add to menu
                    menu.addMenuItem(deleteItem)
                }
            }
            lista.setMenuCreator(creator)
        }
        registerForContextMenu(lista);
    }

}