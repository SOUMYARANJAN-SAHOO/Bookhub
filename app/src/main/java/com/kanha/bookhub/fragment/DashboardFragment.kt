package com.kanha.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kanha.bookhub.R
import com.kanha.bookhub.adapter.DashboardRecyclerAdapter
import com.kanha.bookhub.model.Book
import com.kanha.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.Collections

class DashboardFragment : Fragment() {

    //declaring the views as well as the book list container
    val bookInfoList = arrayListOf<Book>()
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    //initialising the comparator for the sort menu item
    var ratingComparator = Comparator<Book> { book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating, true) == 0){
            book1.bookName.compareTo(book2.bookName, true)
        }else{
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.dashboard_fragment, container, false)

        //initialising the dashboard and the layout manager
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)

        //showing the menu items
        setHasOptionsMenu(true)

        //covering the UI with the progress layout
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE


        //defining necessary variable for the sending request to the server
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        //checking if the app has internet connection hence handling the network error
        if (ConnectionManager().checkConnection(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    //handling the json error if any
                    try {
                        //removing the progress layout
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")

                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                            }
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)

                            //attaching the respective components to the recycler
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager

                        } else {
                            //handling the error
                            Toast.makeText(
                                activity as Context, "Some error occurred!!", Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: JSONException) {
                        //handling the JSON error
                        Toast.makeText(
                            activity as Context, "JSON error occurred!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    //handling the volley error
                    Toast.makeText(
                        activity as Context, "Volley error occurred!", Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "40a6f63c2347f9"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            // handling the no internet issue with an dialog
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open setting") { text, listener ->
                val setting_intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting_intent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id == R.id.action_sort){
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
        }

        //notifing the adapter about the changed data
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}
