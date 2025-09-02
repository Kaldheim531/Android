package com.example.android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.accessibility.AccessibilityManagerCompat.TouchExplorationStateChangeListener
import com.example.myapplication.databinding.ActivityCalculatorBinding
import com.example.myapplication.databinding.ActivityMainBinding


class Calculator : AppCompatActivity() {
    lateinit var mainBinding: ActivityCalculatorBinding
    var number :String?=null

    var fristNumber:Double=0.0
    var lastNumber:Double=0.0

    var status : String?=null
    var operator: Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding=ActivityCalculatorBinding.inflate(layoutInflater)


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
        mainBinding.buttonDot.setOnClickListener{
            number = if(number ==  null){
                "0."
            }else{
                "$number."
            }
            mainBinding.textView.text=number
        }

        mainBinding.buttonDevide.setOnClickListener { onOperatorClicked("division") }
        mainBinding.buttonMultiple.setOnClickListener { onOperatorClicked("multi") }
        mainBinding.buttonMinus.setOnClickListener { onOperatorClicked("subtraction") }
        mainBinding.buttonPlus.setOnClickListener { onOperatorClicked("addition") }
        mainBinding.buttonEqual.setOnClickListener { onEqualClicked() }

    }
    override fun onResume() {
        super.onResume()

    }
    fun onNubmerClick(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else {
            number += clickedNumber
        }
        mainBinding.textView.text = number
        operator = true
    }
    fun onButtonACClicked(){
        number = null
        status = null
        mainBinding.textView.text="0"
        fristNumber =0.0
        lastNumber=0.0
    }

    fun onOperatorClicked(operatorType: String) {
        if (operator) {
            performOperation()
        }
        status = operatorType
        operator = false
        number = null
    }
    fun performOperation() {
        lastNumber = mainBinding.textView.text.toString().toDouble()
        when (status) {
            "addition" -> fristNumber += lastNumber
            "subtraction" -> fristNumber -= lastNumber
            "multi" -> fristNumber *= lastNumber
            "division" -> {
                if (lastNumber == 0.0) {
                    Toast.makeText(
                        applicationContext,
                        "Делитель не может быть 0",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else {
                    fristNumber /= lastNumber
                }
            }

            else -> fristNumber = lastNumber
        }
        mainBinding.textView.text = fristNumber.toString()
    }

    fun onEqualClicked() {
        if (operator) {
            performOperation()
        }
        operator = false
    }


}