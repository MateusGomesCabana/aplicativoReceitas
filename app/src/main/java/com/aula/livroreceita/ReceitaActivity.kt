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

        btnCadastro.setOnClickListener{
            val receita = Receita(
                    nome = txtNome.text?.toString(),
                    autor = txtEndereco.text?.toString(),
                    ingredientes = txtIngreditente.text?.toString(),
                    modopreparo = editTextTextMultiLine.text?.toString(),
                    data = cal.timeInMillis
            )
            ReceitaRepository(this).create(receita)
            Toast.makeText(this, "Receita incluido", Toast.LENGTH_LONG).show()
            finish()


//            var dados = "[${txtNome.text} : ${txtEndereco.text} : ${txtEmail.text} : ${txtTelefone.text} : ${txtSite.text} : ${txtDatanascimento.text}]"
//            Toast.makeText(this, dados, Toast.LENGTH_LONG).show()
        }
        btnAddIngreditentes.setOnClickListener{
            count++
            val layout = linear
            val editTextOne = EditText(this)
            editTextOne.setId(count)
            layout.addView(editTextOne);
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy : HH:mm:ss" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        txtDatanascimento.text = sdf.format(cal.getTime())
    }
//
    override fun onResume() {
        super.onResume()
        val receita = intent?.getSerializableExtra("receita")
        if(receita != null){
            receita as Receita
            txtNome.setText(receita?.nome)
        }

    }

}