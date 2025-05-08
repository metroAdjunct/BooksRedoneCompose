package com.example.booksredonecompose
data class Book(val title: String?, val author: String?, val publicationYear: String) {
    override fun toString(): String {
        return "TITLE = " + title + "\n AUTHOR = " + author + "\n YEAR = " + publicationYear
    }
    fun convertOut(): String {
        return title + "," + author + "," + this.publicationYear
    }
}