package com.example.booksredonecompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.booksredonecompose.ui.theme.BooksRedoneComposeTheme
import com.example.booksredonecompose.ui.theme.Grey

import java.io.IOException
import java.io.File
import java.io.FileWriter
import java.time.format.TextStyle
import java.util.Scanner




class MainActivity : ComponentActivity() {
    val bookList = mutableStateListOf<Book>()
    var myPlace: String? = null
    var indexToDelete : Int = -1
    // code just below put in to allow additions via separate activity
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val data = activityResult.data
            val title = data?.getStringExtra("title") ?: ""
            val author = data?.getStringExtra("author") ?: ""
            val publicationYear = data?.getStringExtra("publicationYear") ?: ""
            bookList.add(Book(title, author, publicationYear))
            Log.d("BOOKSREDONE", "MAIN Back from second===== TITLE $title ================")
            //bookAdapter.notifyDataSetChanged()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myDir = this.getFilesDir()
            Log.d("BOOKSREDONE", "myDir = " + myDir + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            val myDirName = myDir.getAbsolutePath()
            myPlace = myDirName
            Log.d("BOOKSREDONE", "My absolute Dir Path = " + myDirName + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            readFile(bookList)
            //bookList.add(Book("Exodus", "Moses","-1000"))
            //bookList.add(Book("Repeated Novel", "Repeated J. Author","1958"))
            BooksRedoneComposeTheme {
                MyScreen(bookList,
                    {
                        startForResult.launch(Intent(this, AddBookActivity::class.java))
                    },
                    { writeFile() },
                    {Log.d("BOOKSREDONE", "MAIN ACTIVITY INDEX Selected $it")
                        indexToDelete = it
                        var myTitle = bookList[indexToDelete].title
                        Toast.makeText(this, "Selected Book $it $myTitle", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    // FUNCTION readFile - reads the file BOOKS.csv - populating full employee list

    fun readFile(bookList: MutableList<Book>) {
        Log.d("BOOKSREDONE", "readFile() entered")
        try {
            val f = File(myPlace + "/BOOKS.csv")
            f.createNewFile()
            Log.d("BOOKSREDONE", "To set up scanner")
            val myReader = Scanner(f)
            while (myReader.hasNextLine()) {
                val data = myReader.nextLine()
                Log.d("BOOKSREDONE", "LINE of input data: " + data)
                //println(data)
                val parts = data.split(",")
                bookList.add(Book(parts[0], parts[1], parts[2]))
            }
            myReader.close()
        } catch (e: IOException) {
            Log.d("BOOKSREDONE", "HIT EXCEP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e)
            println("An error occurred.")
            e.printStackTrace()
        }
    }

    // FUNCTION writeFile - writes the full employee list to file BOOKSREDONE.csv
    fun writeFile() {
        Log.d("BOOKSREDONE", "writeFile() entered")
        try {
            val f = File(myPlace + "/BOOKS.csv")
            if (f.exists()) {
                Log.d("BOOKSREDONE", "EXISTS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            } else {
                Log.d("BOOKSREDONE", "DOES NOT EXIST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            }
            val fw = FileWriter(f, false)  // do not append =- over write
            val count = bookList.size
            Log.d("BOOKSREDONE", "Count >>>>> =  " + count + ">>>>>>>>>>>>>>>>>>>>>>>>>>")
            // print the list
            for (i in 0..<count) {
                val s = bookList[i]?.convertOut() as String?
                Log.d("BOOKSREDONE", s + ">>>>>>>>>>>>>>>>>>>>>>>>>>")
                fw.write(s + "\n")
            }
            fw.flush()
            fw.close()
        } catch (iox: IOException) {
            Log.d("BOOKSREDONE", "HIT EXCEP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d("BOOKSREDONE", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> EXCEP " + iox)
        }
    }
}
@Composable
fun MyScreen(blist : List<Book>, adder: () -> Unit, saver: () -> Unit, onClick:(Int) -> Unit ) {
    Column() {
        Text("\nB O O K  L I S T\n",  modifier = Modifier.wrapContentHeight(), fontSize = 30.sp )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = adder)
            {
                Text("ADD BOOK")
            }
            Button(onClick = saver)
            {
                Text("SAVE LIST")
            }
        }
        LazyColumn {
            itemsIndexed(items = blist) { index, item ->
                Column( modifier = Modifier.padding(vertical = 7.dp)) {
                    Text(item.toString(),  Modifier
                        .clickable { onClick(index) }
                        .border(2.dp, Color.Blue)
                        .height(50.dp)
                        .background(Grey)
                        .fillMaxSize())
                }
            }
        }
    }
}
