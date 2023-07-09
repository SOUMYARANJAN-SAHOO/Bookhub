package com.kanha.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kanha.bookhub.R
import com.kanha.bookhub.activity.DescriptionActivity
import com.kanha.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(private val context: Context, private val bookList: List<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    //implementing the view-holder class
    class FavouriteViewHolder( view: View) : RecyclerView.ViewHolder(view){
        val book_name : TextView = view.findViewById(R.id.txtRecyclerFavRowBookName)
        val author_name : TextView = view.findViewById(R.id.txtRecyclerFavRowBookAuthor)
        val book_price : TextView = view.findViewById(R.id.txtRecyclerFavRowBookPrice)
        val book_rating : TextView = view.findViewById(R.id.txtRecyclerFavRowBookRating)
        val book_cover : ImageView = view.findViewById(R.id.imgRecyclerFavRowBookLogo)

        //binding the container layout to set the onclick listener
        val container : CardView = view.findViewById(R.id.cardContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.book_name.text = book.bookName
        holder.author_name.text = book.bookAuthor
        holder.book_price.text = book.bookPrice
        holder.book_rating.text = book.bookRating
        Picasso.get().load(book.bookCover).error(R.drawable.book_app_icon).into(holder.book_cover)

        holder.container.setOnClickListener {
            val descriptionIntent = Intent(context,DescriptionActivity::class.java)
            descriptionIntent.putExtra("book_id",book.book_id.toString())
            context.startActivity(descriptionIntent)
        }
    }
}