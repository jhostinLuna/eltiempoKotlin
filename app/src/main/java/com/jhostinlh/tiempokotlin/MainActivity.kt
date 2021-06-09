package com.jhostinlh.tiempokotlin


import android.Manifest
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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.jhostinlh.tiempokotlin.databinding.ActivityMainBinding
import com.jhostinlh.tiempokotlin.location.levantarGPS.LocationUtils
import java.io.IOException


const val CIUDAD = "com.jhostinlh.tiempokotlin.CIUDAD"
class MainActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var binding: ActivityMainBinding
    companion object{
        val LOCATION_REQUEST_CODE: Int = 10001
    }
    lateinit var miIntent: Intent
    private lateinit var coord:String
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this@MainActivity)
        coord = ""
        miIntent = Intent()
        miIntent = Intent(this,DetalleTiempo::class.java)
        binding.btnEnviarMa.setOnClickListener(this)
        binding.imgUbicacionMa.setOnClickListener(this)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 ){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.size > 0){
                // Permission Granted
                asklocationPermission()
            }else{
                //Permission no Granted
                Toast.makeText(this,"Permiso Denegado!!",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("prueba", "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [$data]")
        if (requestCode == LocationUtils.PRIORITY_HIGH_ACCURACY_REQ) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //GPS activado por el usuario

                    var milListener: android.location.LocationListener = object : android.location.LocationListener {


                        /*Se ejecuta cada vez que hay un cambio en la configuracion del gps*/


                        override fun onProviderEnabled(provider: String) {

                        }

                        override fun onProviderDisabled(provider: String) {
                            Toast.makeText(this@MainActivity,"GPS ha sido desHabilitado",Toast.LENGTH_LONG).show()

                        }

                        override fun onLocationChanged(location: Location) {
                            miIntent.putExtra(CIUDAD,"${location.latitude};${location.longitude}")
                            startActivity(miIntent)
                        }


                    }

                    val estado: LocationSettingsStates = LocationSettingsStates.fromIntent(data)

                    estado.isGpsUsable
                    val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                            Toast.makeText(this,"Ya tienes permisos de localizacion fineLocation",Toast.LENGTH_LONG).show()



                        }else{
                            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                LOCATION_REQUEST_CODE)
                            Toast.makeText(this,"se va a else",Toast.LENGTH_LONG).show()

                        }
                    }else{
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,600000,7000f,milListener)

                    }






                }
                Activity.RESULT_CANCELED -> {
                    //GPS desactivado, cancelado por el usuario
                }
            }
        }
    }
    fun getUbicacion(){
        habilitaUbicacion()
        if (coord.isNotEmpty()){

            startActivity(miIntent);
        }else{

            asklocationPermission()
        }

    }
    fun habilitaUbicacion(){
        val locationRequest: LocationRequest = LocationRequest.create()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)




        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsRequest: LocationSettingsRequest?


        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)



        settingsClient
            .checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(this as Activity) {
                // GPS enabled already
                val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationRequest: LocationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this,"locationManager no funciona",Toast.LENGTH_LONG).show()

                }else{
                    val loc:Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (loc != null) {
                        miIntent.putExtra(CIUDAD,"${loc.latitude};${loc.longitude}")

                    }

                }


            }
            .addOnFailureListener(this@MainActivity) { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@MainActivity,
                                LocationUtils.PRIORITY_HIGH_ACCURACY_REQ
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
    private fun asklocationPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"Ya tienes permisos de localizacion fineLocation",Toast.LENGTH_LONG).show()



            }else{
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
                Toast.makeText(this,"se va a else",Toast.LENGTH_LONG).show()

            }
        }else{
            val locationTask : Task<Location> = fusedLocationClient.lastLocation

            locationTask.addOnSuccessListener(object :OnSuccessListener<Location>{
                override fun onSuccess(p0: Location?) {
                    if (p0 != null) {
                        coord = "${p0.latitude};${p0.longitude}"
                        miIntent.putExtra(CIUDAD,coord)
                        startActivity(miIntent)
                    }else{
                        Toast.makeText(this@MainActivity,"getLastLocation es null!!",Toast.LENGTH_LONG).show()
                    }
                }

            })
            locationTask.addOnFailureListener(object :OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Log.i("fallo",p0.localizedMessage)
                }

            })

        }

    }






    fun intentToDetalle(){
        var txtciudad: String = binding.edittxtCiudad.text.toString()
        var city: MutableList<Address> = ArrayList<Address>()
        try{
            city = geocoder.getFromLocationName(txtciudad, 1)

        }catch (e: IOException){
            Log.i("geocoder","¡if the network is unavailable or any other I/O problem occurs!")
            return
        }catch (e: IllegalArgumentException){
            Log.i("geocoder","if locationName is null")
            return
        }
        if (city.isNotEmpty()){
            coord = ""+city[0].latitude+";"+city[0].longitude
            if (txtciudad.isNotEmpty()){

                miIntent.apply { putExtra(CIUDAD,coord) }
                startActivity(miIntent);
                coord=""
            }else{
                Toast.makeText(this, "¡Lo Has dejado en blanco!", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "¡introduce una ciudad válida!", Toast.LENGTH_SHORT).show()

        }




    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.btn_enviar_ma){

                intentToDetalle()
            }
            else if (v.id == R.id.img_ubicacion_ma){
                getUbicacion()
            }
        }else{
            Toast.makeText(this, "¡View es null!", Toast.LENGTH_SHORT).show()

        }
    }

}