package com.example.todolistbootcamp

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.todolistbootcamp.models.MyList
import com.example.todolistbootcamp.application.ListApplication
import com.example.todolistbootcamp.models.ListaDTO
import kotlinx.android.synthetic.main.activity_edit_list.*

import android.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar.*


class EditListActivity : BaseActivity() {

    private var idContato: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_list)

        //val toolbar = findViewById<Toolbar>(R.id.toolber)

        //setupToolBar( toolbar,"New List", true, false)

        setupLista()
        initToolbar()
        btnSalvarLista.setOnClickListener { onClickSalvarContato() }
        btnExcluirLista.setOnClickListener { onClickExcluirContato() }
    }

    private fun initToolbar(){

        val toolbar = findViewById<Toolbar>(R.id.toolber)

        if (idContato == -1) {
            toolbar.title = "Nova Lista"
        }else{
            toolbar.title = "Editando Lista"
        }
        setSupportActionBar(toolbar)

        iconDoneds.visibility = View.GONE

        supportActionBar?.setCustomView(R.layout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //val toolbar = findViewById<Toolbar>(R.id.toolber)

      //  setupToolBar( toolbar,"New List", true, true)
        // setSupportActionBar(toolbar)

        //supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)

        //supportActionBar?.setCustomView(R.layout.toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun setupLista() {

         idContato = intent.getIntExtra("index",-1)

          if (idContato == -1){
              btnExcluirLista.visibility = View.GONE
             return
         }
         var lista = ListApplication.instance.helperDB?.buscarContatos("$idContato",true) ?: return
         var contato = lista.getOrNull(0) ?: return

         etNome.setText(contato.nome)
         etTelefone.setText(contato.descricao)
    }

    private fun onClickSalvarContato() {
         val nome = etNome.text.toString().uppercase()
           val telefone = etTelefone.text.toString().lowercase()

           val contato = ListaDTO(
                idContato,
                nome,
                telefone
            )
            if(idContato == -1) {
              ListApplication.instance.helperDB?.salvarContato(contato)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }else{

                ListApplication.instance.helperDB?.updateContato(contato)
                val intent = Intent(this, ListActivity::class.java)
                intent.putExtra(ListActivity.EXTRA_CONTACT, MyList(contato.nome, contato.descricao, idContato))
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent)
                finish()
            }
    }


    fun onClickExcluirContato() {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    if (idContato > -1) {
                        ListApplication.instance.helperDB?.deletarContato(idContato)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
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
}