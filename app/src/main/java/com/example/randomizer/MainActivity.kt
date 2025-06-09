package com.example.randomizer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val numberRandomizer = NumberRandomizer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        val btnGenerate = findViewById<MaterialButton>(R.id.btn_generate)
        val btnCopy = findViewById<MaterialButton>(R.id.btn_copy)

        btnGenerate?.setOnClickListener {
            generateNumbers()
        }

        btnCopy?.setOnClickListener {
            copyToClipboard()
        }
    }

    private fun generateNumbers() {
        val etMinValue = findViewById<TextInputEditText>(R.id.et_min_value)
        val etMaxValue = findViewById<TextInputEditText>(R.id.et_max_value)
        val etCount = findViewById<TextInputEditText>(R.id.et_count)
        val switchUnique = findViewById<SwitchMaterial>(R.id.switch_unique)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        val minText = etMinValue?.text?.toString() ?: ""
        val maxText = etMaxValue?.text?.toString() ?: ""
        val countText = etCount?.text?.toString() ?: ""
        val isUnique = switchUnique?.isChecked ?: false

        if (minText.isEmpty() || maxText.isEmpty() || countText.isEmpty()) {
            showToast("Заполните все поля")
            return
        }

        try {
            val min = minText.toInt()
            val max = maxText.toInt()
            val count = countText.toInt()

            if (min >= max) {
                showToast("Минимальное значение должно быть меньше максимального")
                return
            }

            if (count <= 0) {
                showToast("Количество чисел должно быть больше 0")
                return
            }

            if (isUnique && count > (max - min + 1)) {
                showToast("Невозможно сгенерировать столько уникальных чисел в заданном диапазоне")
                return
            }

            val numbers = if (isUnique) {
                numberRandomizer.generateUniqueNumbers(min, max, count)
            } else {
                numberRandomizer.generateNumbers(min, max, count)
            }

            tvResult?.text = numbers.joinToString(", ")

        } catch (e: NumberFormatException) {
            showToast("Введите корректные числа")
        }
    }

    private fun copyToClipboard() {
        val tvResult = findViewById<TextView>(R.id.tv_result)
        val result = tvResult?.text?.toString() ?: ""

        if (result.isNotEmpty() && result != "Результат появится здесь") {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Random Numbers", result)
            clipboard.setPrimaryClip(clip)
            showToast("Скопировано в буфер обмена")
        } else {
            showToast("Нет данных для копирования")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

