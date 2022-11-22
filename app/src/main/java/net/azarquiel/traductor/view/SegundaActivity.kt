package net.azarquiel.traductor.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import net.azarquiel.traductor.R

class SegundaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda)
        //sacamos los datos que le hemos pasado de la primera pantalla
        val palabra = intent.getSerializableExtra("palabra")
        Toast.makeText(this, palabra.toString(), Toast.LENGTH_LONG).show()

        val txtpalabra = findViewById<ConstraintLayout>(R.id.txtEspañolSeg) as TextView

        txtpalabra.text = palabra.toString()

    }
}