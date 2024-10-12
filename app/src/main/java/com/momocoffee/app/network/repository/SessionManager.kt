package com.momocoffee.app.network.repository

import android.content.Context
import com.momocoffee.app.App
import java.util.Timer
import java.util.TimerTask

class SessionManager(){
    private val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    private var timer: Timer? = null

    fun startSession() {
        timer?.cancel() // Cancelar el temporizador actual si existe
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val editor = sharedPreferences.edit()
                editor.remove("clientId")
                editor.remove("nameClient")
                editor.apply()
            }
        }, 640000)
    }

    fun stopSession() {
        timer?.cancel()
    }

}