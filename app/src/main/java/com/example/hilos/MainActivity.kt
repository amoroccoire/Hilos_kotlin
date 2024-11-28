package com.example.hilos

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var progressContainer: LinearLayout
    private lateinit var btnStartDownloads: Button
    private lateinit var numDownloads: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressContainer = findViewById(R.id.progressContainer)
        btnStartDownloads = findViewById(R.id.btnStartDownloads)
        numDownloads = findViewById(R.id.numDownloads)

        btnStartDownloads.setOnClickListener {
            val txt = numDownloads.text
            if (txt.isNotBlank() && txt.isNotEmpty() && txt.toString().toInt() != 0)
                startSimulatedDownloads(txt.toString().toInt())
            else
                startSimulatedDownloads(5)
        }
        /*enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    fun startSimulatedDownloads(simulations: Int) {
        progressContainer.removeAllViews()
        for (i in 1..simulations) {
            // Crear dinámicamente un texto para cada descarga
            val textView = TextView(this).apply {
                text = "Archivo $i: 0%"
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 16
                }
            }

            progressContainer.addView(textView)

            // Iniciar la descarga simulada en un hilo independiente
            simulateDownload(i, textView)
        }
    }

    fun simulateDownload(downloadNumber: Int, textView: TextView) {
        thread {
            var progress = 0
            while (progress < 100) {
                Thread.sleep((50..150).random().toLong()) // simular tiempo variable de descarga
                progress += 1

                // Actualizar el texto en la UI
                Handler(Looper.getMainLooper()).post {
                    textView.text = "Archivo $downloadNumber: $progress%"
                }
            }
        }
    }
}