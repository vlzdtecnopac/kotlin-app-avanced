import android.util.Log
import com.momocoffee.app.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(BuildConfig.API_BASE_URL)
            mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
                Log.d("Socket.IO", "Conectado")
                // Aquí puedes enviar mensajes al servidor o realizar otras acciones al conectarte
            }).on(Socket.EVENT_DISCONNECT, Emitter.Listener {
                Log.d("Socket.IO", "Desconectado")
            }).on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener {
                Log.e("Socket.IO", "Error de conexión: ${it[0]}")
            })

        } catch (e: URISyntaxException) {
            e.printStackTrace();
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}