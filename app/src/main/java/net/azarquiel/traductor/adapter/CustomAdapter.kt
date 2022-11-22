package net.azarquiel.traducttor.adapter

    import android.content.Context
    import android.media.SoundPool
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import net.azarquiel.traductor.R
    import net.azarquiel.traductor.model.Palabras


/**
     * Created by pacopulido on 9/10/18.
     */
    class CustomAdapter(val context: Context,
                        val layout: Int,
                        //val listenerEspanol : OnClickListenerEspanol
                        //val listener: OnClickListenerSonido
    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        private var dataList: List<Palabras> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val viewlayout = layoutInflater.inflate(layout, parent, false)
            return ViewHolder(viewlayout, context)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.bind(item//, listenerEspanol
         )
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        internal fun setpalabras(palabras: List<Palabras>) {
            this.dataList = palabras
            notifyDataSetChanged()
        }


        class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
            fun bind(dataItem: Palabras//, listenerEspanol: OnClickListenerEspanol
            )
                 {
                // itemview es el item de diseño
                // al que hay que poner los datos del objeto dataItem
                val tvEspanol = itemView.findViewById(R.id.tvEspanol) as TextView
                val tvIngles = itemView.findViewById(R.id.tvIngles) as TextView
                val ivSonido = itemView.findViewById(R.id.ivsonido) as ImageView


                tvEspanol.text = dataItem.palabraEspanol
                tvIngles.text = dataItem.palabraIngles
               // itemView.setOnClickListener{listener.OnClick(itemView)}


                itemView.tag = dataItem

            }


        }

//    interface OnClickListenerEspanol {
//        fun OnClick(itemView: View)
//    }

    //   interface OnClickListenerSonido {
//        fun OnClick(itemView: View)
 //   }


}

