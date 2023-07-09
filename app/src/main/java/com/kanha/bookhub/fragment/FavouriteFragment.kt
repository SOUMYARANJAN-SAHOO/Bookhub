package com.kanha.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kanha.bookhub.R
import com.kanha.bookhub.adapter.FavouriteRecyclerAdapter
import com.kanha.bookhub.database.BookDatabase
import com.kanha.bookhub.database.BookEntity
import com.kanha.bookhub.model.Book

class FavouriteFragment : Fragment() {

    //declaring the variables
    lateinit var recyclerFavourite: RecyclerView
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var layoutManager: GridLayoutManager

    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.favourite_fragment,container,false)

        //initialising the variables
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        recyclerFavourite = view.findViewById(R.id.FavRecyclerView)
        layoutManager = GridLayoutManager(activity as Context, 2)

        //accessing the database and getting the favourite book data
        val retrievedBooks  = retrieverFavourite(activity as Context).execute().get()

        if(retrievedBooks != null){
            //removing the progress layout
            progressLayout.visibility = View.GONE

            //initialising the adapter with the retrieved books
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, retrievedBooks)

            //attaching the respective components to the recycler
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }else{
            //handling the error
            Toast.makeText(
                activity as Context, "Some error occurred!!", Toast.LENGTH_LONG
            ).show()
        }
        return view
    }

    class retrieverFavourite(val context : Context) : AsyncTask<Void, Void, List<BookEntity>>(){

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            //creating the accessing the database
            val db = Room.databaseBuilder(context, BookDatabase::class.java , "book_db").build()
            return db.bookDao().getAllBooks()
        }
    }

}