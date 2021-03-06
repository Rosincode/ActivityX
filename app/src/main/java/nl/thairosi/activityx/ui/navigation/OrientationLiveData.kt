package nl.thairosi.activityx.ui.navigation

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData

/**
 * This class extends the orientation LiveData in the navigationViewModel to provide it with
 * live orientation data
 *
 * Used sensors:
 * TYPE_ACCELEROMETER
 * TYPE_MAGNETIC_FIELD
 */
class OrientationLiveData(application: Application) : LiveData<Float>(), SensorEventListener {

    // Properties
    private val sensorManager: SensorManager =
        application.getSystemService(SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
    private var magnetometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    // Flags
    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    // Called when the lifecycle owner(Activity) is either paused, stopped or destroyed
    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, magnetometer)
    }

    // Called when the lifecycle owner(LocationActivity) is either started or resumed
    override fun onActive() {
        super.onActive()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("orientation", "$sensor accuracy changed to: $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor === accelerometer) {
            lowPass(event.values, lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event.sensor === magnetometer) {
            lowPass(event.values, lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                value = (Math.toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360
            }
        }
    }

    private fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f
        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    }
}