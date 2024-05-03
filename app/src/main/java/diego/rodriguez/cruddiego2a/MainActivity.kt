package diego.rodriguez.cruddiego2a

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataclassProductos

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mandar a llamar todos los elementos de la pantalla
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnButton)

        //2- Programar el boton
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){

                //Guardar datos
                //1- Creo un objeto de la clase conexion
                val claseConexion = ClaseConexion().cadenaConexion()

                //2- Creo una variable que contenga un PreparedStatement
                val addProducto = claseConexion?.prepareStatement("insert into tbProductos(nombreProducto, precio, cantidad) values(?, ?, ?)")!!
                addProducto.setString(1, txtNombre.text.toString())
                addProducto.setInt(2, txtPrecio.text.toString().toInt())
                addProducto.setInt(3, txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()

            }
        }

        ///////////////////////////////////////////Mostar///////////////////////////////////////////

        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)


        //Asignar un layyout al RecyclerView

        rcvProductos.layoutManager = LinearLayoutManager( this)

        //Funcion para obtner datos
        fun obtenerDatos(): List<DataclassProductos>{
           val objConexion = ClaseConexion().cadenaConexion()

           val statement = objConexion?.createStatement()
           val resultado = statement?.executeQuery("select * from brproductos")!!
           val productos = mutableListOf<DataclassProductos>()
           while (resultado.next()) {
               val nombre = resultado.getString("nombreProducto")
               val producto = DataclassProductos(nombre)
               productos.add(producto)
           }
            return productos

            //Asignacion un adaptador
            CoroutineScope(Dispatchers.IO).launch {
                val productos = obtenerDatos()
                withContext(Dispatchers.Main){
                    val myAdapter = Adaptador(productos)
                    rcvProductos.adapter = myAdapter
                }
            }


           }
        }

    }
}