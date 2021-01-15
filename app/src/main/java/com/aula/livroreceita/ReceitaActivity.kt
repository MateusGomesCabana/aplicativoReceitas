package com.aula.livroreceita

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aula.db.Receita
import com.aula.db.ReceitaRepository
import kotlinx.android.synthetic.main.activity_receita.*
import java.text.SimpleDateFormat
import java.util.*


class ReceitaActivity : AppCompatActivity() {
    var cal = Calendar.getInstance()
    var count = 1
    private val myFormat = "dd/MM/yyyy : HH:mm:ss" // mention the format you need
    private val sdf = SimpleDateFormat(myFormat, Locale.US)
    private var receita: Receita? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita)

        setSupportActionBar(toolbar_child)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        txtDatanascimento.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@ReceitaActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        btnCadastro.setOnClickListener {

            receita?.nome = txtNome.text?.toString()
            receita?.autor = txtEndereco.text?.toString()
            receita?.ingredientes = txtIngreditente.text?.toString()
            receita?.modopreparo = txtModoPreparo.text?.toString()
            receita?.data = cal.timeInMillis

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
            count++
            val layout = linear
            val editTextOne = EditText(this)
            editTextOne.setId(count)
            layout.addView(editTextOne);
        }
    }

    private fun updateDateInView() {

        txtDatanascimento.text = sdf.format(cal.getTime())
    }

    //
    override fun onResume() {
        super.onResume()
        receita = intent?.getSerializableExtra("receita") as Receita?
        if (receita != null) {
            receita as Receita
            txtNome.setText(receita?.nome)
            txtEndereco.setText(receita?.autor)
            txtEndereco.setText(receita?.autor)
            txtIngreditente.setText(receita?.ingredientes)
            txtModoPreparo.setText(receita?.modopreparo)
            if (receita!!.data != null) {
                txtDatanascimento.text = sdf.format(receita?.data)
            } else {
                txtDatanascimento.text = sdf.format(Date())
            }
        }else{
            receita = Receita()
        }

    }

}