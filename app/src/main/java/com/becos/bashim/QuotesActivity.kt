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
import com.becos.bashim.data.Quote


const val INTENT_NAME_NAME = "name"
const val INTENT_SITE_NAME = "site"

class QuotesActivity : AppCompatActivity() {

    @BindView(R.id.list)
    lateinit var listView: RecyclerView
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    lateinit var adapter: SourceOfQuotesAdapter

    private val list: MutableList<Quote> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm

        val site = intent.getStringExtra(INTENT_SITE_NAME)
        val name = intent.getStringExtra(INTENT_NAME_NAME)
        compositeDisposable.add(
            repository.searchQuotes(site, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    list.addAll(result)
                    listView.adapter = QuotesAdapter(list)
                    Log.d(tag, list.toString())
                }
        )
    }
}
