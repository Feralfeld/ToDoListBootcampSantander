package com.example.todolistbootcamp.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolistbootcamp.models.ItemVO
import com.example.todolistbootcamp.models.TodoItem

class HelperItensDB (
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL){

    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 8
    }

    val TABLE_NAME = "itens"
    val COLUMNS_ID = "ID"
    val COLUMNS_ID_LISTA = "idlista"
    val COLUMNS_NOME = "nome"
    val COLUMNS_DESCRICAO = "descricao"
    val COLUMNS_DATA = "data"
    val COLUMNS_HORA = "hora"
    val COLUMNS_DATED = "isdated"
    val COLUMNS_AGENDA = "isagenda"
    val COLUMNS_ATIVO = "ativo"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ("+
            "$COLUMNS_ID INTEGER NOT NULL,"+
            "$COLUMNS_ID_LISTA INTEGER NOT NULL,"+
            "$COLUMNS_ATIVO BOOLEAN NOT NULL,"+
            "$COLUMNS_NOME TEXT NOT NULL,"+
            "$COLUMNS_DESCRICAO TEXT NOT NULL,"+
            "$COLUMNS_DATA TEXT ,"+
            "$COLUMNS_HORA TEXT ,"+
            "$COLUMNS_DATED BOOLEAN NOT NULL,"+
            "$COLUMNS_AGENDA BOOLEAN NOT NULL,"+
            ""+
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)"+
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun buscarItens(busca: String, idLista: Int, isBuscaPorId: Boolean = false) : List<TodoItem>{
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<TodoItem>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorId){
             where = "$COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ? AND $COLUMNS_ATIVO = 1"
             args = arrayOf("$busca", "$idLista")
        }else{
             where = "$COLUMNS_NOME LIKE ? AND $COLUMNS_ID_LISTA = ? AND $COLUMNS_ATIVO = 1"
             args = arrayOf("%$busca%", "$idLista")
        }
        var cursor = db.query(TABLE_NAME,null,where, args,null,null,null)
        if(cursor == null){
            db.close()
            return mutableListOf()
        }

        while(cursor.moveToNext()){
            var item = TodoItem(
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRICAO)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_DATED)) > 0 ,
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATA)),
//                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID_LISTA)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HORA)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_AGENDA)) > 0
                )
            lista.add(item)
        }
        db.close()
        return lista
    }

    fun salvarItens(contato: ItemVO){
        val db = writableDatabase ?: return

        var content =  ContentValues()
        content.put(COLUMNS_NOME, contato.nome)
        content.put(COLUMNS_DESCRICAO, contato.descricao)
        content.put(COLUMNS_ID_LISTA, contato.idLista)
        content.put(COLUMNS_ATIVO, true)
        content.put(COLUMNS_DATED, contato.isDated)
        content.put(COLUMNS_DATA, contato.valorData)
        content.put(COLUMNS_HORA, contato.valorHora)
        content.put(COLUMNS_AGENDA, contato.isAgenda)

        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun deletarItem(id: Int, idLista: Int){
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ?"
        val args: Array<String> = arrayOf("$id", "$idLista")
        db.execSQL(sql,args)
        db.close()
    }

    fun updateItem(contato: ItemVO){
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ?, $COLUMNS_DESCRICAO = ?, $COLUMNS_DATED = ?, $COLUMNS_DATA = ?, $COLUMNS_HORA =? " +
                " WHERE $COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ?"
        val arg = arrayOf(contato.nome,  contato.descricao,contato.isDated, contato.valorData, contato.valorHora , contato.id , contato.idLista)
        db.execSQL(sql,arg)
        db.close()
    }


    fun doneItem(idItem: Int, idLista: Int){
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_ATIVO = ? WHERE $COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ?"
        val arg = arrayOf(0, idItem , idLista )
        db.execSQL(sql,arg)
        db.close()
    }

    fun unDoneItem(idItem: Int, idLista: Int){
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_ATIVO = ? WHERE $COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ?"
        val arg = arrayOf(1, idItem , idLista )
        db.execSQL(sql,arg)
        db.close()
    }

    fun buscarDoneds(busca: String, idLista: Int, isBuscaPorId: Boolean = false) : List<TodoItem>{
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<TodoItem>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorId){
            where = "$COLUMNS_ID = ? AND $COLUMNS_ID_LISTA = ? AND $COLUMNS_ATIVO = 0"
            args = arrayOf("$busca", "$idLista")
        }else{
            where = "$COLUMNS_NOME LIKE ? AND $COLUMNS_ID_LISTA = ? AND $COLUMNS_ATIVO = 0"
            args = arrayOf("%$busca%", "$idLista")
        }
        var cursor = db.query(TABLE_NAME,null,where, args,null,null,null)
        if(cursor == null){
            db.close()
            return mutableListOf()
        }
        while(cursor.moveToNext()){
            var item = TodoItem(
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRICAO)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_DATED)) > 0 ,
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATA)),
//                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID_LISTA)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HORA)),
                cursor.getInt(cursor.getColumnIndex(COLUMNS_AGENDA)) > 0
            )
            lista.add(item)
        }
        db.close()
        return lista
    }
}