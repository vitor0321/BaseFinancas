package com.example.financas.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financas.R
import com.example.financas.adapter.BalanceAdapter
import com.example.financas.entity.MovimentEntity
import com.example.financas.extensions.getResume
import com.example.financas.storage.FinancasStorage
import kotlinx.android.synthetic.main.activity_balance.*

class BalanceActivity : AppCompatActivity() {

    private val storage: FinancasStorage by lazy { FinancasStorage(this) }

    private lateinit var data: MutableList<MovimentEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)
        init()
    }

    private fun init() {
        // Substituimos o Action Bar pelo Toolbar
        setSupportActionBar(toolbar)
        // Habilitamos o Menu Voltar no Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        data = storage.getMoviments()!!
        fillData()
    }

    // Buscar Movimentos Salvos no Banco de Dados
    private fun fillData(sortType: SortBalanceType = SortBalanceType.DATE_DESC) =
        sortData(sortType)?.let {
            balance.text = storage.getMoviments()?.getResume()
            //Havendo Movimento Salvos irá preencher o nosso RecyclerView (Lista de Movimentos)
            balances.apply {
                layoutManager = LinearLayoutManager(this@BalanceActivity)
                // Criamos o Adapter para preencher nosso RecyclerView
                adapter = BalanceAdapter(it) {
                    // Parte responsável por obter o item a qual foi clicado no RecyclerView
                    val result = Intent()
                    result.putExtra(RESULT_DATA, it)
                    setResult(Activity.RESULT_OK, result)
                    finish()
                }
            }
        } ?: run {
            //TODO EXIBIR MENSAGEM DE SEM MOVIMENTOS
        }

    /**
     * Método Responsável por capturar o click dos itens de Menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.sort_value_asc -> fillData(SortBalanceType.VALUE_ASC)
            R.id.sort_value_desc -> fillData(SortBalanceType.VALUE_DESC)
            R.id.sort_date_asc -> fillData(SortBalanceType.DATE_ASC)
            R.id.sort_date_desc -> fillData(SortBalanceType.DATE_DESC)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Método onde fazemos ordenação de acordo com o tipo de ordenação
     */
    private fun sortData(sortType: SortBalanceType): List<MovimentEntity>? {
        return when (sortType) {
            SortBalanceType.DATE_ASC -> data.sortedBy { it.date }
            SortBalanceType.DATE_DESC -> data.sortedBy { -it.date }
            SortBalanceType.VALUE_ASC -> data.sortedBy { it.value }
            SortBalanceType.VALUE_DESC -> data.sortedBy { -it.value }
        }
    }

    /**
     * Método para criação do Menu de nossa tela
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_balance, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {

        const val REQ_CODE = 100
        const val RESULT_DATA = "RESULT_DATA"

        fun newIntent(context: Context) = Intent(context, BalanceActivity::class.java)
    }

    enum class SortBalanceType {
        DATE_ASC,
        DATE_DESC,
        VALUE_ASC,
        VALUE_DESC
    }

}
