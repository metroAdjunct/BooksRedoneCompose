package com.example.booksredonecompose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booksredonecompose.ui.theme.BooksRedoneComposeTheme


class AddBookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BooksRedoneComposeTheme {
                BookScreen({  x, y, z ->
                    Log.d("BOOKSREDONE", "AddBookAct callBack 1 ===== TITLE $x ================")
                    var bookInfoIntent = Intent()
                    Log.d("BOOKSREDONE", "AddBookAct callBack 2  ===== AUTHOR $y ================")
                    bookInfoIntent.putExtra("title", x)
                    bookInfoIntent.putExtra("author", y)
                    bookInfoIntent.putExtra("publicationYear", z)
                    setResult(Activity.RESULT_OK, bookInfoIntent)
                    finish()
                })
            }
        }
    }
}
@Composable
fun BookScreen(gotInfo: (String, String, String) -> Unit) {
    var myTitle:String = ""
    var myAuthor:String = ""
    var myYear:String = ""
    BookEnter(title = "", author = "", year = "",
        gotInfo = { x, y, z -> gotInfo(x,y,z)
        }
    )
}
@Composable
fun BookEnter(title:String,  author:String, year:String, gotInfo: (String, String, String) -> Unit) {

    var title:String by remember { mutableStateOf("") }
    var author:String  by remember { mutableStateOf("") }
    var year:String by remember { mutableStateOf("") }
    Column( modifier = Modifier.padding(16.dp) ) {

        OutlinedTextField(
            value = title,
            onValueChange= { title=it },
            label={Text("Enter book title")}
        )
        OutlinedTextField(
            value = author,
            onValueChange= {author=it },
            label={Text("Enter book author")}
        )
        OutlinedTextField(
            value = year,
            onValueChange= {year=it },
            label={Text("Enter book pub year")}
        )
        Button(onClick = {
            Log.d("ENTBOOK", "BUTTON: ")
            gotInfo(title, author, year)

        }) {
            Text("SUBMIT")
        }
        Text(
            text = "BOOK: $title",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}