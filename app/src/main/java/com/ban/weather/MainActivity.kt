package com.ban.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.ban.weather.adapters.ScreenSlidePagerAdapter
import com.ban.weather.databinding.ActivityMainBinding
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivityMainBinding

    private var numberOfCities = -1

    // View Model
    private val mainViewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

    // View Pager
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ScreenSlidePagerAdapter
    private var listFragment : ArrayList<WeatherFragment> = ArrayList()

    // Current Location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
//    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request Current Location
        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (requestGpsPermission(this)) {
            startLocationUpdates()
        }

        initView()
        addObservers()
//        recyclerView()

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
            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra("test", "TEST MESSAGE using Intent")
            }
            startActivity(intent)
        }
    }

    private fun startLocationUpdates() {
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

        mainViewModel.getWeatherByLattLong(lattLongQueryString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObservers() {
        mainViewModel.weather.observe(this, {
            Log.d(TAG, "[observe] >> weather, num of cities to be sent to view pager ${it.size}")
                it.map {
                    val fragment = WeatherFragment.newInstance(it)
                    listFragment.add(fragment)
                }
                viewPagerAdapter.updateData(listFragment)
//            updateTodayView(it)
//            updateRecyclerView(it.consolidatedWeather)
        })

        mainViewModel.favoriteList.observe(this, {
            Log.d(TAG, "[observe] >> favoriteList, num of saved cities => ${it.size}")
            numberOfCities = it.size
        })
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestGpsPermission(context: Context) : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d(TAG, "onRequestPermissionsResult")
            }
        }
    }



}


