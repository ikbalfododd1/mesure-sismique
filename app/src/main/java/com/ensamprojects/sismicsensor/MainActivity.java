package com.ensamprojects.sismicsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    LineGraphSeries<DataPoint> plot = new LineGraphSeries<DataPoint>();

    double accelerationChange, currentAcceleration, previousAcceleration = 0, x, y, z;
    int xPlotTrack = 0;

    TextView x_axis, y_axis, z_axis;

    Viewport viewport;

    SensorManager sensorManager;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser le graphe
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(plot);

        // Configuration du graphe
        viewport = graph.getViewport();
        viewport.scrollToEnd();
        viewport.setXAxisBoundsManual(true);
        plot.setColor(getResources().getColor(R.color.primaryColor));
        plot.setThickness(3);


        // Initialiser les zones du texte
        x_axis = findViewById(R.id.x_axis);
        y_axis = findViewById(R.id.y_axis);
        z_axis = findViewById(R.id.z_axis);

        // Création d'un gestionnaire de capteurs
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initialiser l'accéléromètre
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Enregistrer l'écouteur du capteur (Sensor listener)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Récupérer les nouvelles données
        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];

        // Mettre a jour les zones texte
        x_axis.setText(String.valueOf((int) x));
        y_axis.setText(String.valueOf((int) y));
        z_axis.setText(String.valueOf((int) z));


        // Calcul de la valeur de l'accélération  actuelle
        currentAcceleration = Math.sqrt(x*x+y*y+z*z);
        accelerationChange = currentAcceleration - previousAcceleration;
        previousAcceleration = currentAcceleration;


        // Mettre a jour le graphe
        xPlotTrack++;
        plot.appendData(new DataPoint(xPlotTrack, accelerationChange), true, xPlotTrack);
        viewport.setMaxX(xPlotTrack);
        viewport.setMinX(xPlotTrack - 200);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

