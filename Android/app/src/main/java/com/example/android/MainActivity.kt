package com.example.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // View Binding
    private var currentInput = "" // Текущий ввод
    private var currentOperator = "" // Текущий оператор
    private var firstNumber = 0.0 // Первое число
    private var secondNumber = 0.0 // Второе число

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обработка нажатий на кнопки цифр
        binding.button0.setOnClickListener { appendNumber("0") }
        binding.button1.setOnClickListener { appendNumber("1") }
        binding.button2.setOnClickListener { appendNumber("2") }
        binding.button3.setOnClickListener { appendNumber("3") }
        binding.button4.setOnClickListener { appendNumber("4") }
        binding.button5.setOnClickListener { appendNumber("5") }
        binding.button6.setOnClickListener { appendNumber("6") }
        binding.button7.setOnClickListener { appendNumber("7") }
        binding.button8.setOnClickListener { appendNumber("8") }
        binding.button9.setOnClickListener { appendNumber("9") }

        // Обработка нажатий на кнопки операций
        binding.buttonAdd.setOnClickListener { setOperator("+") }
        binding.buttonSubtract.setOnClickListener { setOperator("-") }
        binding.buttonMultiply.setOnClickListener { setOperator("*") }
        binding.buttonDivide.setOnClickListener { setOperator("/") }

        // Обработка нажатия на кнопку "."
        binding.buttonDot.setOnClickListener { appendNumber(".") }

        // Обработка нажатия на кнопку "="
        binding.buttonEquals.setOnClickListener { calculateResult() }

        // Обработка нажатия на кнопку "C"
        binding.buttonClear.setOnClickListener { clearInput() }
    }

    // Добавление цифры или точки в текущий ввод
    private fun appendNumber(number: String) {
        if (number == "." && currentInput.contains(".")) {
            return // Не добавляем больше одной точки
        }
        currentInput += number
        updateDisplay()
    }

    // Установка оператора
    private fun setOperator(operator: String) {
        if (currentInput.isNotEmpty()) {
            firstNumber = currentInput.toDouble()
            currentOperator = operator
            currentInput = ""
            updateDisplay()
        }
    }

    // Вычисление результата
    private fun calculateResult() {
        if (currentInput.isNotEmpty() && currentOperator.isNotEmpty()) {
            secondNumber = currentInput.toDouble()
            val result = when (currentOperator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> {
                    if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN // Обработка деления на ноль
                }
                else -> Double.NaN
            }
            currentInput = if (result.isNaN()) "Error" else result.toString()
            currentOperator = ""
            updateDisplay()
        }
    }

    // Очистка ввода
    private fun clearInput() {
        currentInput = ""
        currentOperator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        updateDisplay()
    }

    // Обновление отображения
    private fun updateDisplay() {
        // Стало (корректно):
        binding.display.setText(currentInput)
    }
}