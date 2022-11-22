package net.azarquiel.traductor.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.azarquiel.traductor.R
import net.azarquiel.traductor.databinding.ActivityMainBinding
import net.azarquiel.traductor.model.Palabras
import net.azarquiel.traductor.util.Util
import net.azarquiel.traducttor.adapter.CustomAdapter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),  SearchView.OnQueryTextListener, TextToSpeech.OnInitListener
{

    // Singleton TTS
    companion object {
        private var tts: TextToSpeech? = null
    }
    private var bandera: Boolean = false
    private lateinit var searchView: SearchView
    private lateinit var espanolSH: SharedPreferences
    private lateinit var inglesSH: SharedPreferences
    private lateinit var palabras: ArrayList<Palabras>
    private lateinit var adapter: CustomAdapter
    private lateinit var rvcarro: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //instanciamos el TextToSpeech
        tts = TextToSpeech(this, this)

        rvcarro = findViewById<RecyclerView>(R.id.rvcarro) as RecyclerView
        espanolSH = getSharedPreferences("espanol", Context.MODE_PRIVATE)
        inglesSH = getSharedPreferences("ingles", Context.MODE_PRIVATE)
        setSupportActionBar(binding.toolbar)


        initRV()
        getPalabrasIngles()
        getPalabrasEspanol()
        showPalabras()


        //inyectamos los dos archivos a la aplicacion para que una vez la iniciemos ya tenga datos por defecto
        Util.inyecta(this, "espanol.xml")
        Util.inyecta(this, "ingles.xml")

    }

    private fun getPalabrasEspanol() {
        //creamos una variable para almacenar todas las palabras que ya estan metidas dentro del sharepreferences
        palabras = ArrayList<Palabras>()
        val inglesAll = inglesSH.all
        for ((key, value) in inglesAll) {
            val espanol = espanolSH.getString(key, "")
            val palabra = Palabras(value.toString(), espanol!!)
            palabras.add(palabra)
        }
    }


    private fun showPalabras() {
        adapter.setpalabras(palabras)
    }

    private fun getPalabrasIngles() {
        //creamos una variable para almacenar todas las palabras que ya estan metidas dentro del sharepreferences
        palabras = ArrayList<Palabras>()
        val espanolAll = espanolSH.all
        for ((key, value) in espanolAll) {
            val ingles = inglesSH.getString(key, "")
            val palabra = Palabras(value.toString(), ingles!!)
            palabras.add(palabra)
        }
    }

    private fun initRV() {
        adapter = CustomAdapter(this, R.layout.row)
        rvcarro.adapter = adapter
        rvcarro.layoutManager = LinearLayoutManager(this)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_flagEspanol -> {

                cambiarbandera(item)
                true

            }
            else -> {
                super.onOptionsItemSelected(item)

            }
        }
        return false
    }

    private fun cambiarbandera(item: MenuItem) {
        if (bandera){
            item.setIcon(R.drawable.flage)
            bandera = !bandera
        }else{
            item.setIcon(R.drawable.flagi)
            bandera = !bandera
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

            return false
        }


        override fun onQueryTextChange(query: String?): Boolean {
            val original = ArrayList<Palabras>(palabras)
            if (bandera)
                adapter.setpalabras(original.filter { palabra ->
                    palabra.palabraEspanol.startsWith(
                        query!!
                    )
                })
            else {
                adapter.setpalabras(original.filter { palabra ->
                    palabra.palabraIngles.startsWith(
                        query!!
                    )
                })
            }
            return false
        }


    //Metodo que descarga los idiomas y hace que hable
    private fun speakOut(palabra: Palabras) {
        tts!!.language = Locale("es")
        tts!!.speak(palabra.palabraEspanol, TextToSpeech.QUEUE_ADD, null,"")
        tts!!.language = Locale.US
        tts!!.speak(palabra.palabraIngles, TextToSpeech.QUEUE_ADD, null,"")
    }

    //metodo onInit del TTS
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje US no suportado")
            } else {
            }
            result = tts!!.setLanguage(Locale("es"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje ESP no suportado")
            } else {
            }
        } else {
            Log.e("TTS", "Fallo en inicialización de TTS")
        }
    }

    //metodo para pulsar sobre la row y que asi el speech consiga hablar
    fun onClickPalabra(view: View) {
        val palabras = view.tag as Palabras
        speakOut(palabras)
        //instanciamos la segunda pantalla
        intent = Intent(this, SegundaActivity::class.java)
        //vamos a pasar datos a la segunda pantalla, (para hacer esto tenemos que serailizar la clase)
        intent.putExtra("palabra", palabras)
        //Cuando ejecutemos el Onclick esto ira a la segunda pantalla
        startActivity(intent)

    }


}


