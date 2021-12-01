package com.jhostinlh.tiempokotlin


import Dia
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.jhostinlh.tiempokotlin.Retrofit.MyApiAdapter
import com.jhostinlh.tiempokotlin.databinding.ActivityMainBinding
import retrofit2.Call
import java.io.IOException
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import com.microsoft.appcenter.crashes.Crashes

import com.microsoft.appcenter.analytics.Analytics

import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.distribute.Distribute


const val CIUDAD = "com.jhostinlh.tiempokotlin.CIUDAD"
class MainActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var binding: ActivityMainBinding
    companion object{
        val LOCATION_REQUEST_CODE: Int = 10001
        const val REQUEST_CODE_UBICATION = 0x600

    }
    var location: Location? = null
    lateinit var miIntent: Intent
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var fineLocation: Int = 0
    var coarseLocation: Int = 0
    lateinit var locationManager: LocationManager
    lateinit var taskLocationSetResp: Task<LocationSettingsResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this@MainActivity)
        miIntent = Intent()
        miIntent = Intent(this,DetalleTiempo::class.java)
        binding.btnEnviarMa.setOnClickListener(this)
        binding.imgUbicacionMa.setOnClickListener(this)
        binding.imgMapsMa.setOnClickListener(this)
        fineLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        coarseLocation = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
        locationManager = getSystemService(Context.LOCATION_SERVICE)  as LocationManager
        val locationRequest: LocationRequest = LocationRequest.create()

        // iniciando configuracion de clliente SettingClient
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsRequest: LocationSettingsRequest?
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        taskLocationSetResp = settingsClient.checkLocationSettings(locationSettingsRequest)
        AppCenter.start(
            application, "32de52e5-b616-45f0-bde7-09fad6287c0e",
            Analytics::class.java, Crashes::class.java
        )
        AppCenter.start(application, "32de52e5-b616-45f0-bde7-09fad6287c0e", Distribute::class.java)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_REQUEST_CODE ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission Granted
                    Toast.makeText(this, "¡Permiso Concedido!",Toast.LENGTH_LONG).show()

                }else{
                    //Permission no Granted
                    Toast.makeText(this, "¡Necesitas Permisos para utilizar esta función!",Toast.LENGTH_LONG).show()
                }
        }


    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("prueba", "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        if (requestCode == REQUEST_CODE_UBICATION) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //GPS activado por el usuario

                    var milListener: android.location.LocationListener = object : android.location.LocationListener {





                        override fun onProviderEnabled(provider: String) {
                            Toast.makeText(this@MainActivity,"Tu Ubicación esta Desactivada",Toast.LENGTH_LONG).show()

                        }

                        override fun onProviderDisabled(provider: String) {
                            Toast.makeText(this@MainActivity,"GPS ha sido desHabilitado",Toast.LENGTH_LONG).show()

                        }
                        /*Se ejecuta cada vez que hay un cambio en la configuracion del gps*/
                        override fun onLocationChanged(location: Location) {
                            peticionApiService(location)
                        }


                    }


                    val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if (asklocationPermission()){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,600000,7000f,milListener)

                    }

                }
                Activity.RESULT_CANCELED -> {
                    //GPS desactivado, cancelado por el usuario
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    // Si tiene permiso y esta habilitado la ubicacion llama a peticionApiService con location actual
    fun getUbicacion(){
        //Pido permisos para acceder a la ubicación
        if (asklocationPermission()){
            val task = fusedLocationClient.lastLocation
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                habilitaUbicacion()
            }else{
                task.addOnSuccessListener { location ->
                    // Pido los datos a webservice y la envio a la Activity DetalleTiempo
                    peticionApiService(location)
                }
            }
        }
    }
    //recoge location y pide a la web service los datos
    private fun peticionApiService(location: Location) {
        val myApiService = MyApiAdapter.getApiService()
        val call = myApiService?.getPrediccionDias(location.latitude.toString(),
            location.longitude.toString(),"current,minutely,alerts","metric","es", API_KEY)
        if (call != null) {
            call.enqueue(object : Callback<Dia>{
                override fun onResponse(call: Call<Dia>, response: Response<Dia>) {
                    if (!response.isSuccessful){
                        Log.i("responseretrofit","Respuesta retrofit sin exito")
                        return
                    }
                    val respuesta: Dia = response.body()!!
                    Log.i("apiService",respuesta.toString())
                    miIntent.putExtra(CIUDAD,respuesta)
                    startActivity(miIntent)


                }

                override fun onFailure(call: Call<Dia>, t: Throwable) {
                    Log.i("responseretrofit","Respuesta Fállida")
                }

            })
        }
    }
    //Habilita Ubicación si no lo esta sacando el dialogo para que acepte el usuario
    @SuppressLint("MissingPermission")
    fun habilitaUbicacion(){
    taskLocationSetResp.addOnSuccessListener {
        Toast.makeText(this@MainActivity,"Ubicación Habilitada!!",Toast.LENGTH_LONG).show()

    }.addOnFailureListener(this@MainActivity) { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@MainActivity,
                                REQUEST_CODE_UBICATION
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i("prueba", "PendingIntent unable to execute request.")
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.e("prueba", "Enable location services from settings.")
                    }
                }
            }

    }
    /*Pide permiso de ubicacion si no los tiene,
    si los tiene ya devuelve true y si no saca el dialogo para pedirlo
     */
    private fun asklocationPermission(): Boolean {

        var ok = false

        if (fineLocation != PackageManager.PERMISSION_GRANTED && coarseLocation != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            Log.i("permission", "se va a requestPermision")
            /*
            // Entra en el if aunque ya haya sido denegado antes y dentro saca el dialogo del permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
                Log.i("permission", "pide permisos")
            }

            else{

                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
                Log.i("permission", "se va a requestPermision")

            }
             */
        }else{
            ok = true
        }
        return ok
    }




    //carga datos de apiservice de la ciudad escrita en el Editext escrito por el usuario
    fun inputCiudad(){
        val txtciudad: String = binding.edittxtCiudad.text.toString()
        var city: MutableList<Address> = ArrayList<Address>()
        try{
            city = geocoder.getFromLocationName(txtciudad, 2)

        }catch (e: IOException){
            Log.i("geocoder","¡if the network is unavailable or any other I/O problem occurs!")
            return
        }catch (e: IllegalArgumentException){
            Log.i("geocoder","if locationName is null")
            return
        }
        if (city.isNotEmpty() && txtciudad.isNotEmpty() ){
            val loc = Location(LocationManager.GPS_PROVIDER)
            loc.latitude = city[0].latitude
            loc.longitude = city[0].longitude
            peticionApiService(loc)

        }else{
            Toast.makeText(this, "¡introduce una ciudad válida!", Toast.LENGTH_SHORT).show()

        }




    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"HAS AÑADIDO DISTRIBUTION",Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == binding.btnEnviarMa.id){

                inputCiudad()
            }
            else if (v.id == binding.imgUbicacionMa.id){
                getUbicacion()
            }else if (v.id == binding.imgMapsMa.id){
                habilitaUbicacion()
                val intent = Intent(this,MapsActivity::class.java)
                startActivity(intent)
            }
        }else{
            Toast.makeText(this, "¡View es null!", Toast.LENGTH_SHORT).show()

        }
    }

}