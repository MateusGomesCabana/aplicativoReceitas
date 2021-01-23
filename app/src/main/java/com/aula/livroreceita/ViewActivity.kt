package com.aula.livroreceita

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.aula.db.Receita
import kotlinx.android.synthetic.main.activity_receita.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ViewActivity : AppCompatActivity() {
    private var receita: Receita? = null
    private var emailAutor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_view)
        setContentView(R.layout.activity_receita)
        toolbarView.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbarView)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        buttonEnviarEmail.setOnClickListener{
            val intentEmail = Intent(Intent.ACTION_SEND)
            intentEmail.type = "message/rfc822"
            intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(emailAutor!!))
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Mensagem para o autor")
            intentEmail.putExtra(Intent.EXTRA_TEXT, "Corpo da mensagem")
            //item.setIntent(intentEmail);
            startActivity(Intent.createChooser(intentEmail, "Selecione a sua aplicação de Email"))

        }
    }

    override fun onResume() {
        super.onResume()
        receita = intent?.getSerializableExtra("receita") as Receita?
        if (receita != null) {
            receita as Receita
            textNome.setText(receita?.nome)
            //txtAutor.setText(receita?.autor)
            //txtEmail.setText(receita?.email)
            emailAutor = receita?.email
            textDescricao.setText(receita?.descricao)
            val strs = receita?.ingredientes?.split(";")?.toTypedArray()
            var text = ""
            if (strs != null) {
                for (i in 0..(strs.size-1)){
                    text = text + strs.get(i).toString() + "\n"
                }
            }
            ingredientes.setText(text)
            modoPreparo.setText(receita?.modopreparo)
            if(receita?.foto != null){
                readBitmapFile(receita?.foto!!);
            }
        }else{
            textNome.setText("")
            textDescricao.setText("")
            ingredientes.setText("")
            modoPreparo.setText("")
            emailAutor = ""
            readBitmapFile("");
        }
    }

    private fun readBitmapFile(path: String) {
        var bitmap: Bitmap? = null
        val f = File(path)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        try {
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        fotoReceita.setImageBitmap(bitmap)
    }
}