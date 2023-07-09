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
import com.kanha.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(private val context:Context, private val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    //implementing the view-holder class
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val book_name : TextView = view.findViewById(R.id.txtRecyclerRowItemBookTitle)
        val author_name : TextView = view.findViewById(R.id.txtRecyclerRowItemBookAuthorName)
        val book_price : TextView = view.findViewById(R.id.txtRecyclerRowItemBookPrice)
        val book_rating : TextView = view.findViewById(R.id.txtRecyclerRowItemBookRating)
        val book_cover : ImageView = view.findViewById(R.id.imgRecycleRowItemBookLogo)

        //binding the container layout to set the onclick listener
        val container : CardView = view.findViewById(R.id.cardContainer)
    }

    //responsible of creating initial 10 or more items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        // two parameters given one for the layout second for the position the last boolean parameter is about attach to root
        return DashboardViewHolder(view)
    }

    //simple function that returns the size/number of the data present
    override fun getItemCount(): Int {
        return itemList.size
    }

    //it is responsible for the recycling and reusing of the view-holder
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val item = itemList[position]
        holder.book_name.text = item.bookName
        holder.author_name.text = item.authorName
        holder.book_price.text = item.bookPrice
        holder.book_rating.text = item.bookRating
        Picasso.get().load(item.bookCover).error(R.drawable.book_app_icon).into(holder.book_cover)

        holder.container.setOnClickListener {
            val description_intent = Intent(context, DescriptionActivity::class.java)
            description_intent.putExtra("book_id", item.bookId)
            context.startActivity(description_intent)
        }
    }
}