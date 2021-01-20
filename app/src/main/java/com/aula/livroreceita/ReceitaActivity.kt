package com.aula.livroreceita

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import kotlinx.android.synthetic.main.activity_receita_insert.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ReceitaActivity : AppCompatActivity() {
    private var mCurrentPhotoPath: String? = null
    val REQUEST_CODE_GALLERY = 999
    var count = 1
    private var receita: Receita? = null
    var editTexts: ArrayList<EditText> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita_insert)

        setSupportActionBar(toolbar_child)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnCadastro.setOnClickListener {
            receita?.foto = mCurrentPhotoPath
            receita?.nome = txtNome.text?.toString()
            receita?.autor = txtAutor.text?.toString()
            receita?.email = txtEmail.text?.toString()
            receita?.descricao = txtDescricao.text?.toString()
            var text = txtIngreditente.text?.toString()
            for (i in 0..(editTexts.size-1)){
                if (editTexts.get(i).text.isNotEmpty()){
                    text = "${text};${editTexts.get(i).text}"
                }
            }
            receita?.ingredientes = text
            receita?.modopreparo = txtModoPreparo.text?.toString()

            if (receita?.id == 0L) {
                ReceitaRepository(this).create(receita!!)
            } else {
                ReceitaRepository(this).update(receita!!)
            }


            //  ReceitaRepository(this).create(receita)
           // Toast.makeText(this, "Receita incluido", Toast.LENGTH_LONG).show()
            finish()


//            var dados = "[${txtNome.text} : ${txtEndereco.text} : ${txtEmail.text} : ${txtTelefone.text} : ${txtSite.text} : ${txtDatanascimento.text}]"
//            Toast.makeText(this, dados, Toast.LENGTH_LONG).show()
        }
        btnAddIngreditentes.setOnClickListener {
            criaLinha()
        }


        imgContato.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                ActivityCompat.requestPermissions(
                    this@ReceitaActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_GALLERY
                )
            }
        })

    }
    fun criaLinha(){
        val layout = linear
        val editTextOne = EditText(this)
        editTextOne.setId(count)
        count++
        layout.addView(editTextOne);
        editTexts.add(editTextOne)
    }

    //função que verifca a permissão
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_GALLERY)
            } else {
                Toast.makeText(
                    applicationContext,
                    "You don't have permission to access file location!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //Função que pega a imagem selecionada de upload e coloca ela na view
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri? = data.data
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imgContato.setImageBitmap(bitmap)
                storeImage(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(IOException::class)
    private fun storeImage(image: Bitmap) {

        val pictureFile = createImageFile()
        if (pictureFile == null) {
            Log.d("ERRO", "Error creating media file, check storage permissions: ")// e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d("ERRO", "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d("ERRO", "Error accessing file: " + e.message)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
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
        imgContato.setImageBitmap(bitmap)
    }
    //
    override fun onResume() {
        super.onResume()
        receita = intent?.getSerializableExtra("receita") as Receita?
        if (receita != null) {
            receita as Receita
            txtNome.setText(receita?.nome)
            txtAutor.setText(receita?.autor)
            txtEmail.setText(receita?.email)
            txtDescricao.setText(receita?.descricao)
            val strs = receita?.ingredientes?.split(";")?.toTypedArray()
            if (strs != null) {
                for (i in 0..(strs.size-1)){
                    if (i == 0){
                        txtIngreditente.setText(strs.get(i))
                    }else{
                        criaLinha()
                        editTexts.get(i-1).setText(strs.get(i))
                    }
                }
            }
            txtModoPreparo.setText(receita?.modopreparo)
            if(receita?.foto != null){
                readBitmapFile(receita?.foto!!);
            }
        }else{
            receita = Receita()
        }
    }
}