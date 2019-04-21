package edu.metroState.ics372GroupProject3.dataCollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        ProgramState pState = (ProgramState)getApplication();
        Readings myReadings = pState.getCurrentProgramReadings();
        Button uploadButton = findViewById(R.id.startButton);


        uploadButton.setOnClickListener(new View.OnClickListener() {
            /**
             * choose a file from the sdcard file system
             */
            @Override
            public void onClick(View v) {

            }
        });
    }
}
