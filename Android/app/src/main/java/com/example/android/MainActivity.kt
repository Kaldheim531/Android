package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    var number :String?=null

    var fristNumber:Double=0.0
    var lastNumber:Double=0.0

    var status : String?=null
    var operator: Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding=ActivityMainBinding.inflate(layoutInflater)


        val view = mainBinding.root

        setContentView(view)


        mainBinding.textView.text="0"

        mainBinding.button0.setOnClickListener{
            onNubmerClick("0")
        }
        mainBinding.button1.setOnClickListener{
            onNubmerClick("1")
        }
        mainBinding.button2.setOnClickListener{
            onNubmerClick("2")
        }
        mainBinding.button3.setOnClickListener{
            onNubmerClick("3")
        }
        mainBinding.button4.setOnClickListener{
            onNubmerClick("4")
        }
        mainBinding.button5.setOnClickListener{
            onNubmerClick("5")
        }
        mainBinding.button6.setOnClickListener{
            onNubmerClick("6")
        }
        mainBinding.button7.setOnClickListener{
            onNubmerClick("7")
        }
        mainBinding.button8.setOnClickListener{
            onNubmerClick("8")
        }
        mainBinding.button9.setOnClickListener{
            onNubmerClick("9")
        }
        mainBinding.buttonAC.setOnClickListener{
            onButtonACClicked()
        }
        mainBinding.buttonDel.setOnClickListener{
            number?.let{
                number=it.substring(0,it.length-1)
                mainBinding.textView.text=number
            }

        }
        mainBinding.buttonDevide.setOnClickListener{
            if(operator){
                when(status){
                    "multi"->multi()
                    "division" ->divide()
                    "subtraction" -> minus()
                    "addition"->plus()
                    else -> fristNumber = mainBinding.textView.text.toString().toDouble()
                }
            }
            status="division"
            operator =false
            number = null
        }
        mainBinding.buttonMultiple.setOnClickListener{
            if(operator){
                when(status){
                    "multi"->multi()
                    "division" ->divide()
                    "subtraction" -> minus()
                    "addition"->plus()
                    else -> fristNumber = mainBinding.textView.text.toString().toDouble()
                }
            }
            status="multi"
            operator =false
            number = null
        }
        mainBinding.buttonMinus.setOnClickListener{
            if(operator){
                when(status){
                    "multi"->multi()
                    "division" ->divide()
                    "subtraction" -> minus()
                    "addition"->plus()
                    else -> fristNumber = mainBinding.textView.text.toString().toDouble()
                }
            }
            status="subtraction"
            operator =false
            number = null
        }
        mainBinding.buttonPlus.setOnClickListener{
            if(operator){
                when(status){
                    "multi"->multi()
                    "division" ->divide()
                    "subtraction" -> minus()
                    "addition"->plus()
                    else -> fristNumber = mainBinding.textView.text.toString().toDouble()
                }
            }
            status="addition"
            operator =false
            number = null
        }

        mainBinding.buttonEqual.setOnClickListener{
            if(operator){
                when(status){
                    "multi"->multi()
                    "division" ->divide()
                    "subtraction" -> minus()
                    "addition"->plus()
                    else -> fristNumber = mainBinding.textView.text.toString().toDouble()
                }
            }

            operator =false

        }
        mainBinding.buttonDot.setOnClickListener{
            number = if(number ==  null){
                "0."
            }else{
                "$number."
            }
            mainBinding.textView.text=number
        }

    }
    fun onButtonACClicked(){
        number = null
        status = null
        mainBinding.textView.text="0"
        fristNumber =0.0
        lastNumber=0.0
    }

    fun onNubmerClick(clickedNumber: String){
        if(number==null){
            number =clickedNumber
        }else{
            number+=clickedNumber
        }
        mainBinding.textView.text=number
        operator=true
    }

    fun plus(){
        lastNumber=mainBinding.textView.text.toString().toDouble()
        fristNumber+=lastNumber
        mainBinding.textView.text=fristNumber.toString()

    }
    fun minus(){
        lastNumber=mainBinding.textView.text.toString().toDouble()
        fristNumber-=lastNumber
        mainBinding.textView.text=fristNumber.toString()
    }
    fun multi(){
        lastNumber=mainBinding.textView.text.toString().toDouble()
        fristNumber*=lastNumber
        mainBinding.textView.text=fristNumber.toString()

    }
    fun divide(){
        lastNumber=mainBinding.textView.text.toString().toDouble()
        if(lastNumber==0.0){
            Toast.makeText(applicationContext,"Делитель не может быть 0",Toast.LENGTH_LONG).show()
        }else{
            fristNumber/=lastNumber
            mainBinding.textView.text=fristNumber.toString()
        }


    }

}