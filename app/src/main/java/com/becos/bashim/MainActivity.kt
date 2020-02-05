package com.becos.bashim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.BindView
import androidx.recyclerview.widget.RecyclerView
import com.becos.bashim.data.SearchRepository
import com.becos.bashim.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.becos.bashim.data.SourceOfQuotes
import android.content.Intent
import android.widget.ListView

const val tag: String = "MainActivity"

class MainActivity : AppCompatActivity(), ChangeSourceListener {

    override fun sourceChanged(position: Int) {
        Log.d(tag,"from main = ${adapter[position]}")
        val intent = Intent(applicationContext, QuotesActivity::class.java)
        intent.putExtra(INTENT_NAME_NAME, adapter[position].name)
        intent.putExtra(INTENT_SITE_NAME, adapter[position].site)
        startActivity(intent)
    }

    @BindView(R.id.list)
    lateinit var listView: RecyclerView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter:SourceOfQuotesAdapter

    private val list: MutableList<SourceOfQuotes> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation=LinearLayoutManager.VERTICAL
        listView.layoutManager = llm

        compositeDisposable.add(
            repository.searchSourcesOfQuotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    result.forEach {
                        list.addAll(it)
                    }
                    adapter = SourceOfQuotesAdapter(list)
                    adapter.addListener(this)
                    listView.adapter = adapter

                    Log.d(tag, list.toString())
                }
        )
    }
}
