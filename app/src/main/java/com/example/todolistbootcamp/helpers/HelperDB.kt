package com.example.todolistbootcamp.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolistbootcamp.models.ListaDTO

class HelperDB (
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL){

    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 8
    }

    val TABLE_NAME = "lista"
    val COLUMNS_ID = "ID"
    val COLUMNS_NOME = "nome"
    val COLUMNS_DESCRICAO_LISTA = "descricao"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ("+
            "$COLUMNS_ID INTEGER NOT NULL,"+
            "$COLUMNS_NOME TEXT NOT NULL,"+
            "$COLUMNS_DESCRICAO_LISTA TEXT NOT NULL,"+
            ""+
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)"+
            ")"


    val TABLE_NAME2 = "itens"
    val COLUMNS_ID2 = "ID"
    val COLUMNS_ID_LISTA = "idlista"
    val COLUMNS_NOME2 = "nome"
    val COLUMNS_DESCRICAO = "descricao"
    val COLUMNS_ATIVO = "ativo"
    val COLUMNS_DATA = "data"
    val COLUMNS_HORA = "hora"
    val COLUMNS_DATED = "isdated"
    val COLUMNS_AGENDA = "isagenda"
    val DROP_TABLE2 = "DROP TABLE IF EXISTS $TABLE_NAME2"
    val CREATE_TABLE2 = "CREATE TABLE $TABLE_NAME2 ("+
            "$COLUMNS_ID2 INTEGER NOT NULL,"+
            "$COLUMNS_ATIVO BOOLEAN NOT NULL,"+
            "$COLUMNS_ID_LISTA INTEGER NOT NULL,"+
            "$COLUMNS_NOME2 TEXT NOT NULL,"+
            "$COLUMNS_DESCRICAO TEXT NOT NULL,"+
            "$COLUMNS_DATA TEXT ,"+
            "$COLUMNS_HORA TEXT ,"+
            "$COLUMNS_DATED BOOLEAN NOT NULL,"+
            "$COLUMNS_AGENDA BOOLEAN NOT NULL,"+
            ""+
            "PRIMARY KEY ($COLUMNS_ID2 AUTOINCREMENT)"+
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
        db?.execSQL(CREATE_TABLE2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
            db?.execSQL(DROP_TABLE2)
        }
        onCreate(db)
    }

    fun buscarContatos(busca: String, isBuscaPorId: Boolean = false) : List<ListaDTO>{
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<ListaDTO>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorId){
             where = "$COLUMNS_ID = ?"
             args = arrayOf("$busca")
        }else{
             where = "$COLUMNS_NOME LIKE ?"
             args = arrayOf("%$busca%")
        }
       // var where = "$COLUMNS_NOME LIKE ?"
       // var buscaComPercentual = arrayOf("%$busca%")
        var cursor = db.query(TABLE_NAME,null,where, args,null,null,null)
      //  val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE ?"
      //  var buscaComPercentual = "%$busca%"
      //  var cursor = db.rawQuery(sql, arrayOf(buscaComPercentual)) ?: return mutableListOf()
        if(cursor == null){
            db.close()
            return mutableListOf()
        }
        while(cursor.moveToNext()){
            var contato = ListaDTO(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRICAO_LISTA))
                )
            lista.add(contato)
        }
        db.close()
        return lista
    }

    fun salvarContato(contato: ListaDTO){
        val db = writableDatabase ?: return

        var content =  ContentValues()
        content.put(COLUMNS_NOME, contato.nome)
        content.put(COLUMNS_DESCRICAO_LISTA, contato.descricao)
        db.insert(TABLE_NAME, null, content)
        //val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME,$COLUMNS_TELEFONE) VALUES (?,?)"
        //var array = arrayOf(contato.nome, contato.telefone)
        //db.execSQL(sql, array)
        db.close()
    }

    fun deletarContato(id: Int){
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val args: Array<String> = arrayOf("$id")
        db.execSQL(sql,args)
        //   val where = "id = ?"
      //  val arg = arrayOf("$id")
       // db.delete(TABLE_NAME,where,arg)
        db.close()
    }

    fun updateContato(contato: ListaDTO){
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ?, $COLUMNS_DESCRICAO_LISTA = ? WHERE $COLUMNS_ID = ?"
        val arg = arrayOf(contato.nome,contato.descricao, contato.id)
        db.execSQL(sql,arg)
        //val content = ContentValues()
        //content.put(COLUMNS_NOME, contato.nome)
        //content.put(COLUMNS_TELEFONE, contato.telefone)
        //val where = "id = ?"
        //var arg = arrayOf("${contato.id}")
        //db.update(TABLE_NAME,content,where,arg)
        db.close()
    }
}