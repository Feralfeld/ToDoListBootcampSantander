package com.example.todolistbootcamp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.todolistbootcamp.application.ListApplication
import com.example.todolistbootcamp.models.ItemVO
import com.example.todolistbootcamp.models.MyList
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_item.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class ItemActivity : BaseActivity() {

    private var idContato: Int = -1

    private var contact: MyList? = null

//    private lateinit var binding:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)



        getExtras()
        setupContato()
        bindViews()
        initToolbar()
        insertListeners()
        onClickDatedCheckBox()
        datedCheckBox.setOnClickListener { onClickDatedCheckBox() }
        btnSalvarItem.setOnClickListener { onClickSalvarContato() }
        btnExcluirItem.setOnClickListener{ onClickExcluirContato() }
    }

    private fun onClickDatedCheckBox() {
        if(datedCheckBox.isChecked){
            dateLayout.setVisibility(View.VISIBLE)
        }else{
            dateLayout.setVisibility(View.GONE)
        }
    }

    private fun initToolbar(){

        val toolbar = findViewById<Toolbar>(R.id.toolber)
        var title = "Editando Item"

        if (idContato == -1){
            title = "Novo Item"
        }

        setupToolBar( toolbar, title, true, false)
        // setSupportActionBar(toolbar)

        //supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)

        //supportActionBar?.setCustomView(R.layout.toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getExtras(){
        contact = intent.getParcelableExtra(ListActivity.EXTRA_CONTACT)
    }

    private fun bindViews(){
    }

    private fun setupContato(){
        idContato = intent.getIntExtra("index",-1)

        if (idContato == -1){
            btnExcluirItem.visibility = View.GONE
            return
        }

        var idLista = contact?.id

        if (idLista == null ) {
            idLista = 0
        }

        var lista = ListApplication.instance.helperItemDB?.buscarItens("$idContato",idLista, true) ?: return
        var contato = if(lista.isNotEmpty()){
            lista.get(0)
        }else{
            var lista = ListApplication.instance.helperItemDB?.buscarDoneds("$idContato",idLista, true) ?: return
            if(lista.isNotEmpty()){
                lista.get(0)
            }else{
                return
            }
        }

        etNomeItem.setText(contato.nome)
        etDescricaoItem.setText(contato.descricao)
        datedCheckBox.setChecked(contato.isDated)
        selectData.setText(contato.valorData)
        selectHour.setText(contato.valorHora)

    }

    private fun onClickSalvarContato(){
        val nome = etNomeItem.text.toString()
        val telefone = etDescricaoItem.text.toString()
        var idLista = contact?.id

        if (idLista == null ) {
            idLista = 0
        }

        var valData = selectData.getText().toString()
        var valHour = selectHour.getText().toString()

        if(!datedCheckBox.isChecked){
            valData = ""
            valHour = ""

        }

        val item = ItemVO(
            idContato,
            idLista ,
            nome.lowercase(),
            telefone.lowercase(),
            datedCheckBox.isChecked,
            valData,
            valHour
        )

        if(idContato == -1) {
            ListApplication.instance.helperItemDB?.salvarItens(item)
        }else{
            ListApplication.instance.helperItemDB?.updateItem(item)
        }

        finish()
    }

    fun onClickExcluirContato() {
        var idLista = contact?.id

        if (idLista == null ) {
            idLista = 0
        }


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                if(idContato > -1){
                    ListApplication.instance.helperItemDB?.deletarItem(idContato,idLista)
                    finish()
                }
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()




    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun insertListeners(){
        selectData.setOnClickListener {
            val dataPicker = MaterialDatePicker.Builder.datePicker().build()

            dataPicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                val data = Date(it + offset)

                val localDate = Instant.ofEpochMilli(data.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()

                var valday =  localDate.dayOfMonth.toString()
                var valmonth = localDate.monthValue.toString()

                if(localDate.dayOfMonth < 10){
                    valday = "0${localDate.dayOfMonth}"
                }

                if(localDate.monthValue < 10){
                    valmonth = "0${localDate.monthValue}"
                }

                selectData.setText("$valday/$valmonth/${localDate.year}")
            }
            dataPicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        selectHour.setOnClickListener {
            val dataPicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H).build()

            dataPicker.addOnPositiveButtonClickListener {
                var valHour =  dataPicker.hour.toString()
                var valMinutes = dataPicker.minute.toString()

                if(dataPicker.hour < 10){
                        valHour = "0${dataPicker.hour}"
                }

                if(dataPicker.minute < 10){
                    valMinutes = "0${dataPicker.minute}"
                }

                selectHour.setText("$valHour:$valMinutes")
            }

            dataPicker.show(supportFragmentManager, "HOUR_PICKER_TAG")
        }


    }
}