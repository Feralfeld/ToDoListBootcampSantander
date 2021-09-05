package com.example.todolistbootcamp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistbootcamp.ListActivity.Companion.EXTRA_CONTACT
import com.example.todolistbootcamp.adapters.ClickItemListListener
import com.example.todolistbootcamp.models.MyList
import com.example.todolistbootcamp.adapters.ListAdapter
import com.example.todolistbootcamp.application.ListApplication
import com.example.todolistbootcamp.models.ListaDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), ClickItemListListener {

    private var rvList: RecyclerView? = null

    private val adapter: ListAdapter = ListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvList = findViewById(R.id.rv_list)

        initToolbar()
        ListAdapter(this)
        fetchListContact()
        bindViews()
        initClickListeners()
    }

    private fun initToolbar(){

        val toolbar = findViewById<Toolbar>(R.id.toolber)

        toolbar.title = "Minhas Listas"

        setSupportActionBar(toolbar)

        iconDoneds.visibility = View.GONE

        //supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)

        supportActionBar?.setCustomView(R.layout.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initClickListeners() {

        addList.setOnClickListener {
            val intent = Intent(this, EditListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun clickItemList(myList: MyList) {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra(EXTRA_CONTACT,myList)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        dataChanged()
    }

    private fun dataChanged(){
        fetchListContact()
        updateList()
        adapter.notifyDataSetChanged()
    }

    private fun fetchListContact(){

        var listaFiltrada: kotlin.collections.List<ListaDTO> = mutableListOf()
        listaFiltrada = ListApplication.instance.helperDB?.buscarContatos("") ?: mutableListOf()

        var myList: MutableList<MyList> = mutableListOf();

        listaFiltrada.forEach{
                contatoV0 ->
            myList.add(MyList( contatoV0.nome, contatoV0.descricao   , contatoV0.id))
        }

        getInstanceSharedPreferences().edit {
            putString("contacts", Gson().toJson(myList))
            commit()
        }
    }

    private fun getListContacts(): kotlin.collections.List<MyList> {
        val list = getInstanceSharedPreferences().getString("contacts","{}")
        val turnsType = object : TypeToken<kotlin.collections.List<MyList>>() {}.type
        return Gson().fromJson(list,turnsType)
    }

    private fun updateList(){
        adapter?.updateList(getListContacts())
    }

    private fun bindViews(){
        rvList?.adapter = adapter
        rvList?.layoutManager = LinearLayoutManager(this)
        updateList()
    }


    private fun getInstanceSharedPreferences(): SharedPreferences {

        Log.i("log**", this.getSharedPreferences("br.com.bootcampkotlin.PREFERENCES", Context.MODE_PRIVATE).toString())

        return this.getSharedPreferences("br.com.bootcampkotlin.PREFERENCES", Context.MODE_PRIVATE)
    }

}