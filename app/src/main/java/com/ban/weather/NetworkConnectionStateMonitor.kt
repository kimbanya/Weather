package com.ban.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast

class NetworkConnectionStateMonitor(context: Context) : ConnectivityManager.NetworkCallback() {

    private var context: Context? = null
    private var networkRequest: NetworkRequest? = null
    private var connectivityManager: ConnectivityManager? = null

    init {
        this.context = context
        this.networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    fun register() {
        connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager!!.registerNetworkCallback(networkRequest!!, this)
    }

    fun unregister() {
        connectivityManager!!.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        // Process when network is connected
        Toast.makeText(this.context, "Network available", Toast.LENGTH_SHORT).show()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        // Proces when network is disconnected
        Toast.makeText(this.context, "Network lost", Toast.LENGTH_SHORT).show()
    }
}