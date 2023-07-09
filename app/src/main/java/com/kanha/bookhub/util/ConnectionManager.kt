package com.kanha.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.getSystemService

class ConnectionManager {

    fun checkConnection(context: Context):Boolean{
        val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork : NetworkInfo ?= connectionManager.activeNetworkInfo

        return if(activeNetwork?.isConnected != null){
            activeNetwork.isConnected
        }else{
            false
        }
    }
}