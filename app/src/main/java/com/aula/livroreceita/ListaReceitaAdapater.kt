package com.aula.livroreceita

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.aula.db.Receita
import kotlinx.android.synthetic.main.activity_linha.view.*
import kotlinx.android.synthetic.main.activity_receita_insert.*
import java.io.*

class ListaReceitaAdapater(context: Context, receitas: ArrayList<Receita>, assetManager: AssetManager) :
    BaseAdapter() {

    private var receitas: ArrayList<Receita>
    private var inflator: LayoutInflater
    private var assetManager: AssetManager

    init {
        this.receitas = receitas
        this.inflator = LayoutInflater.from(context)
        this.assetManager = assetManager
    }

    override fun getCount(): Int {
        return this.receitas.size
    }

    override fun getItem(position: Int): Receita? {
        return this.receitas[position]
    }

    override fun getItemId(position: Int): Long {
        return this.receitas[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val receita = this.receitas[position]
        val view = this.inflator.inflate(R.layout.activity_linha, parent, false)
        view.nome.text = receita.nome
        var img = readBitmapFile(receita.foto.toString())
        view.foto.setImageBitmap(img)
        return view
    }

    //retorna a imagem referente a receita
    private fun readBitmapFile(path: String): Bitmap? {
        val assetManager = this.assetManager
        var istr: InputStream? = null
        val f = File(path)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        try {
            return BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: FileNotFoundException) {
            istr = assetManager.open("images/face_error.png")
            return BitmapFactory.decodeStream(istr)
        }
     //   return bitmap!!
    }

    private fun getBitmapFromAsset(id: Long): Bitmap {
        val assetManager = this.assetManager
        var istr: InputStream? = null
        try {
            istr = assetManager.open("images/img$id.png")
        } catch (e: IOException) {
            istr = assetManager.open("images/face_error.jpg")
        }
        return BitmapFactory.decodeStream(istr)
    }


}