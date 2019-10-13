package com.demo.belltwittertest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.demo.belltwittertest.R

class TweetSearchFragment:Fragment() {
    companion object {
        fun newInstance() = TweetSearchFragment()
    }

    private lateinit var searchView: SearchView
    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view=inflater.inflate(R.layout.search_tweet_fragment, container, false)

        searchView=view.findViewById(R.id.searchView)

        searchView.doOnLayout {
            searchView.requestFocus()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        recyclerView=view.findViewById(R.id.recyclerView)

        return view
    }
}