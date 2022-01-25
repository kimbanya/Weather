package com.ban.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.ban.weather.adapters.ScreenSlidePagerAdapter
import com.ban.weather.databinding.ActivityMainBinding
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory
import com.google.android.gms.location.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private var currentLattLong = ""

    private lateinit var binding : ActivityMainBinding

    // The number of cities saved in DB
    private var numberOfCities = -1

    // View Model
    private val mainViewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

    // View Pager
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ScreenSlidePagerAdapter

    // List Fragments for View Pager Adapter
    private var listFragment : ArrayList<WeatherFragment> = ArrayList()

    // Current Location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLocationRequest: LocationRequest

    companion object {
        const val REQUEST_PERMISSION_LOCATION = 10
    }

    // Network Status
//    private lateinit var networkConnectionStateMonitor : NetworkConnectionStateMonitor

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register network status
//        networkConnectionStateMonitor = NetworkConnectionStateMonitor(this)
//        networkConnectionStateMonitor.register()

        if(!isNetworkAvailable(this)){
            val toast = Toast.makeText(this, "Check Network Connection \r\nApp is closing", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL)
            toast.show()

            ActivityCompat.finishAffinity(this)
            exitProcess(0)
        }

        // Process response of permit
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "requestPermissionLauncher >> GRANTED")
                startLocationUpdates() // only fine location comes in
            } else {
                Log.d(TAG, "requestPermissionLauncher >> ELSE")
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "[permssion coarse granted] > do somethign here")
                    showCoarseLocationExample()
                }
                else {
                    showSampleCityWeather()
                    Log.d(TAG, "[permssion coarse NOT] > do somethign here")
                }

            }
        }

        when {
            // Already Access
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "checkSelfPermission >> FINE_GRANTED")
                startLocationUpdates()
            }
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "checkSelfPermission >> COARSE_GRANTED")
                startLocationUpdates()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Log.d(TAG, "shouldShowRequestPermissionRationale")
                Toast.makeText(this, "should make UI, showing requesst permission rationale", Toast.LENGTH_LONG).show()
//            showInContextUI()
            }
            else -> {
                // First Access
                Log.d(TAG, "requestPermissionLauncher")
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) // ask for upgrading access,,,, coarse location ?


            }
        }

//        requestCurrentLocation()
        initView()
        addObservers()
    }

    /*
    // Once user responsed to permit or not, onRequestPermissionsResult is called
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d(TAG, "onRequestPermissionsResult >> GRANTED")
                    startLocationUpdates()
                } else {
                    Log.d(TAG, "onRequestPermissionsResult >> Permit Denied 1")
                    showSampleCityWeather()
                }
                return
            }
            else -> {
                // Ignore all other requests.
                Log.d(TAG, "onRequestPermissionsResult >> Permit Denied 2")
                showSampleCityWeather()
            }
        }
    }
     */

    private fun isNetworkAvailable(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork      = connectivityManager.activeNetwork ?: return false
            val networkCapability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
//        requestCurrentLocation()
        mainViewModel.getWeatherByLattLong(currentLattLong)
    }

    private fun initView() {
        // Add View Pager Adapter
        viewPager = binding.pager
        viewPagerAdapter = ScreenSlidePagerAdapter(this)
        if (::viewPager.isInitialized) {
            viewPager.adapter = viewPagerAdapter
        }

        // Move to a Acticity of Search City
        binding.fbaAddCityButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Add Progress Bar
        showProgress(true)
    }

    private fun showProgress(isShow: Boolean) {
        val progressBar = binding.progressbar
        val progressText = binding.progressText
        val floatingActionButton = binding.fbaAddCityButton

        if (isShow) {
            progressBar.visibility = View.VISIBLE
            progressText.visibility = View.VISIBLE
            floatingActionButton.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            progressText.visibility = View.GONE
            floatingActionButton.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObservers() {
        // Observer for Total City List for View Pager (Current Location + Saved Cities in DB)
        mainViewModel.weather.observe(this, {
//            Log.d(TAG, "[observe] >> weather, num of cities to be sent to view pager ${it.size}")
            it.map {
                val fragment = WeatherFragment.newInstance(it)
                listFragment.add(fragment)
            }

            viewPagerAdapter.updateData(listFragment)
            showProgress(false)
        })

        // Observer for Saved Cities in DB
        mainViewModel.favoriteList.observe(this, {
//            Log.d(TAG, "[observe] >> favoriteList, num of saved cities => ${it.size}")
            numberOfCities = it.size
        })
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem -= 1
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        val latitude = Math.round(location.latitude.toFloat() * 1000) / 1000f
        val longitude = Math.round(location.longitude.toFloat() * 1000) / 1000f
        val lattLongQueryString = "$latitude,$longitude"
        currentLattLong = lattLongQueryString
        // Request the Weather of Current Location with Lattitude and Longitude
        mainViewModel.getWeatherByLattLong(lattLongQueryString)
    }

    private fun showSampleCityWeather() {
        // show this when location permit denied
        val sampleCityLattLong = "37.777119, -122.41964"
        mainViewModel.getWeatherByLattLong(sampleCityLattLong)
    }
    private fun showCoarseLocationExample() {
    // show this when location permit
        val sampleCityLattLong = "35.6804, 139.7690"
        mainViewModel.getWeatherByLattLong(sampleCityLattLong)
    }

    //    override fun onDestroy() {
//        super.onDestroy()
//        networkConnectionStateMonitor.unregister()
//    }

}


