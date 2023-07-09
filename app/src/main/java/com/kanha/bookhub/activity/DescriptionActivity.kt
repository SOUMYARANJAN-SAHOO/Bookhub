package com.kanha.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kanha.bookhub.R
import com.kanha.bookhub.database.BookDatabase
import com.kanha.bookhub.database.BookEntity
import com.kanha.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    //declaring all the variables
    lateinit var desc_book_logo: ImageView
    lateinit var desc_book_name: TextView
    lateinit var desc_book_author: TextView
    lateinit var desc_book_price: TextView
    lateinit var desc_book_rating: TextView
    lateinit var desc_book_about: TextView

    lateinit var progress_layout: RelativeLayout
    lateinit var progress_bar: ProgressBar

    lateinit var add_to_favourite: Button

    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    var book_id: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.description_activity)

        toolbar = findViewById(R.id.toolbarDescriptionPage)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Description"

        desc_book_logo = findViewById(R.id.imgDescriptionBookLogo)
        desc_book_name = findViewById(R.id.txtDescriptionBookTitle)
        desc_book_author = findViewById(R.id.txtDescriptionAuthorName)
        desc_book_price = findViewById(R.id.txtDescriptionBookPrice)
        desc_book_rating = findViewById(R.id.txtDescriptionBookRating)
        desc_book_about = findViewById(R.id.txtDescriptionBookAbout)

        progress_layout = findViewById(R.id.progressLayout)
        progress_bar = findViewById(R.id.progressBar)
        progress_layout.visibility = View.VISIBLE

        add_to_favourite = findViewById(R.id.btnFavourite)

        if (intent != null) {
            book_id = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (book_id == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        if (ConnectionManager().checkConnection(this@DescriptionActivity)) {
            var request_jsonObject = JSONObject()
            request_jsonObject.put("book_id", book_id)

            val jsonRequest = @RequiresApi(Build.VERSION_CODES.M)
            object : JsonObjectRequest(Method.POST, url, request_jsonObject,
                Response.Listener {
                    //handling the json error if any
                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            val jsonObject = it.getJSONObject("book_data")

                            //removing the progress layout
                            progress_layout.visibility = View.GONE

                            val imageUrl = jsonObject.getString("image")
                            desc_book_name.text = jsonObject.getString("name")
                            desc_book_author.text = jsonObject.getString("author")
                            desc_book_price.text = jsonObject.getString("price")
                            desc_book_rating.text = jsonObject.getString("rating")
                            desc_book_about.text = jsonObject.getString("description")
                            Picasso.get().load(imageUrl)
                                .error(R.drawable.book_app_icon).into(desc_book_logo)

                            val bookEntity = BookEntity(
                                book_id?.toInt() as Int,
                                desc_book_name.text.toString(),
                                desc_book_author.text.toString(),
                                desc_book_price.text.toString(),
                                desc_book_rating.text.toString(),
                                desc_book_about.text.toString(),
                                imageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()
//
                            if(isFav){
                                add_to_favourite.text = getString(R.string.remove_from_favourites)
                                add_to_favourite.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.favourite_clicked), null, null, null)
                                add_to_favourite.setBackgroundColor(this.getColor(R.color.colourFavourite))
                            }else{
                                add_to_favourite.text = getString(R.string.add_to_favourite)
                                add_to_favourite.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.favourite_unclicked), null, null, null)
                                add_to_favourite.setBackgroundColor(this.getColor(R.color.light_primary))
                            }
//
                            add_to_favourite.setOnClickListener {
                                if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()){
                                    val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity, "Book added to Favourites", Toast.LENGTH_SHORT).show()
                                        add_to_favourite.text = getString(R.string.remove_from_favourites)
                                        add_to_favourite.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.favourite_clicked), null, null, null)
                                        add_to_favourite.setBackgroundColor(this.getColor(R.color.colourFavourite))
                                    }else{
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()

                                    if(result){
                                        Toast.makeText(this@DescriptionActivity, "Book removed to Favourites", Toast.LENGTH_SHORT).show()
                                        add_to_favourite.text = getString(R.string.add_to_favourite)
                                        add_to_favourite.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.favourite_unclicked), null, null, null)
                                        add_to_favourite.setBackgroundColor(this.getColor(R.color.light_primary))
                                    }else{
                                        Toast.makeText(this@DescriptionActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some unexpected error occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some unexpected json error occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some unexpected volley error occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "40a6f63c2347f9"
                    return headers
                }
            }

            queue.add(jsonRequest)
        } else {
            // handling the no internet issue with an dialog
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open setting") { text, listener ->
                val setting_intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting_intent)
                this@DescriptionActivity.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }

    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode:Int) : AsyncTask<Void, Void, Boolean>(){

        /*
        1 -> check DB if the book is favourite or not
        2 -> save the book to DB as favourite
        3 -> remove the favourite book
         */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "book_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1 -> {
                    //check DB if the book is favourite or not
                    val book : BookEntity = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
                    //save the book to DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}