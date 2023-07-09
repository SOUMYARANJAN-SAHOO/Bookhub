package com.kanha.bookhub.model

import com.squareup.picasso.RequestCreator

data class Book(
    val bookId: String,
    val bookName: String,
    val authorName: String,
    val bookRating: String,
    val bookPrice: String,
    val bookCover: String
)
