package net.azarquiel.traductor.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.*

/**
 * Autor: Paco Pulido
 */

@SuppressLint("StaticFieldLeak")
object Util {
    private lateinit var XMLFile:String
    private lateinit var context: Context

    fun inyecta(context: Context, XMLFile:String) {
        Util.context = context
        Util.XMLFile = XMLFile

        //si no existe el archivo, llamamos a un metodo para copiar el archivo y sacamos una tostada para mostrarlo la primera vez
        if (!File("/data/data/${context.packageName}/shared_prefs/$XMLFile").exists()) {
            Toast.makeText(context,"Copiando Archivos....", Toast.LENGTH_LONG).show()
            //hacemos un metodo para copiar el xml
            copiarXML()
        }
    }
    private fun copiarXML() {
        //hacemos un metodo para crear el directorio
        creaDirectorio()
        copiar(XMLFile)
    }

    private fun creaDirectorio() {
        val file = File("/data/data/${context.packageName}/shared_prefs")
        file.mkdir()
    }

    //metodo para copiar un archivo
    private fun copiar(XMLFile: String) {
        val ruta = ("/data/data/${context.packageName}/shared_prefs/$XMLFile")
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = context.assets.open(XMLFile)
            output = FileOutputStream(ruta)
            copyFile(input, output)
            input!!.close()
            output.close()
        } catch (e: IOException) {
            Log.e("Traductor", "Fallo en la copia del archivo desde el asset", e)
        }
    }

    private fun copyFile(input: InputStream?, output: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        read = input!!.read(buffer)
        while (read != -1) {
            output.write(buffer, 0, read)
            read = input!!.read(buffer)
        }
    }

}