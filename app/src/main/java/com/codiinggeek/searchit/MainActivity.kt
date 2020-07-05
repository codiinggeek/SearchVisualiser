package com.codiinggeek.searchit

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.marginTop
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var numberArray = arrayListOf<Int>()
    var buttonArray = arrayListOf<Button>()

    lateinit var llcontent: LinearLayout
    lateinit var eEnterNumber: EditText
    lateinit var txtFound: TextView
    lateinit var btnSearch: Button
    lateinit var txtTitle: TextView
    lateinit var txtDescription: TextView

    lateinit var jobLinear: Job
    lateinit var jobBinary: Job

    lateinit var sharedPreferences: SharedPreferences

    val purple:String = "#9c27b0"
    val red:String = "#d50000"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)

        val opt = sharedPreferences.getInt("Option", -1)
        if(opt==-1){
            Toast.makeText(this, "Some Unexpected Error Occured", Toast.LENGTH_SHORT).show()
            finish()
        }
        llcontent = findViewById(R.id.llContent)
        eEnterNumber = findViewById(R.id.eEnterNumber)
        txtFound = findViewById(R.id.txtFound)
        btnSearch = findViewById(R.id.btnSearch)
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDescription)

        if(opt==1){
            txtTitle.text = "Linear Search"
            txtDescription.text = getString(R.string.txtLinearSearch)
            for(i in 0..9){
                numberArray.add((0..99).random())
            }
        }else{
            txtTitle.text = "Binary Search"
            txtDescription.text = getString(R.string.txtBinarySearch)
            var j = 9;
            var q = 0;
            for(i in 0..9){
                numberArray.add((q..j).random())
                j += 10
                q += 10
            }
        }
        jobLinear = GlobalScope.launch {  }
        jobBinary = GlobalScope.launch {  }
        title = "Search Algorithm"

        createButtonLayout()

        btnSearch.setOnClickListener {

            val s = eEnterNumber.text.toString()
            if(s!="") {
                val num = s.toInt()
                if(opt==1)
                    linearSearch(num)
                else
                    BinarySearch(num)
            }else{
                Toast.makeText(this,"Some Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
                jobLinear.cancel()
                jobBinary.cancel()
                createButtonLayout()
            }
        }
    }

    private fun BinarySearch(num: Int){
        txtFound.text = ""
        jobBinary = GlobalScope.launch(Dispatchers.Main) {
            var y = -1
            var ind = -1
            for(i in 0 until buttonArray.size){
                colorIt(buttonArray[i], "#bdbdbd")
            }
            var low = 0
            var high = numberArray.size-1
            while(low<=high){
                for(i in 0 until low){
                    colorIt(buttonArray[i], "#bdbdbd")
                }
                for(i in high+1 until buttonArray.size){
                    colorIt(buttonArray[i], "#bdbdbd")
                }
                for(i in low until high+1){
                    colorIt(buttonArray[i], "#ea80fc")
                }
                var mid = low+(high-low)/2
                colorIt(buttonArray[mid], purple)
                delay(500)
                if(numberArray[mid]==num){
                    colorIt(buttonArray[mid], red)
                    ind = mid
                    y = 1
                    break
                }else if(numberArray[mid]>num){
                    high = mid-1
                }else{
                    low = mid+1
                }
            }
            if(y==1){
                txtFound.text ="Number Found at index: $ind"
            }else{
                txtFound.text = "Not found"
            }
        }
    }

    private fun linearSearch(num: Int){
        txtFound.text = ""
        jobLinear = GlobalScope.launch(Dispatchers.Main) {
            var y = -1
            var ind = -1;
            for(i in 0 until buttonArray.size){
                colorIt(buttonArray[i], "#bdbdbd")
            }
            for(i in 0 until numberArray.size){
                if(i!=0){
                    colorIt(buttonArray[i-1], "#ea80fc")
                }
                val button = buttonArray[i]
                colorIt(button, purple)
                delay(500)
                if(numberArray[i]==num){
                    colorIt(button, red)
                    y = 1
                    ind = i
                    break
                }
            }
            if(y==1){
                txtFound.text ="Number Found at index: $ind"
            }else{
                txtFound.text = "Not found"
            }
        }
    }
    private suspend fun colorIt(btn: Button ,color: String){
        val job = GlobalScope.launch (Dispatchers.Main){
            btn.setBackgroundColor(Color.parseColor(color))
        }
        job.join()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createButtonLayout(){
        for(i in 0 until numberArray.size){
            val button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            button.id = i+1
            button.text = numberArray[i].toString()
            button.setTextColor(Color.parseColor("#000000"))
            button.setTypeface(button.typeface, Typeface.BOLD)
            button.setBackgroundColor(Color.parseColor("#bdbdbd"))

            llcontent.addView(button)
            buttonArray.add(button)
        }
    }

    override fun onBackPressed() {
        jobLinear.cancel()
        jobBinary.cancel()
        finish()
        super.onBackPressed()
    }
}