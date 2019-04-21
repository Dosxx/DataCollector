package edu.metroState.ics372GroupProject3.dataCollector;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final String TAG = "MainActivity Log";

    /*contain the list of Study ID from stored program state*/
    String[] studyIDList = {"12345", "09876", "32145", "45555"};
    Context myContext;
    EditText studyId;
    EditText studyName;
    Spinner iDSpinner ;
    Spinner nameSpinner;
    ArrayAdapter<String> iDAdapter;
    RadioGroup radioGroup;
    RadioButton importing;
    RadioButton creating;
    EditText fileName;
    Button proceedButton;
    Button clearButton;
    ProgramState pState ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Initialize all the widgets on the main activity*/
        myContext = getApplicationContext();
        studyId = findViewById(R.id.studyIDField);
        studyName = findViewById(R.id.studyName);
        iDSpinner = findViewById(R.id.studyIDSpinner);
        nameSpinner = findViewById(R.id.studyNameSpinner);
        radioGroup = findViewById(R.id.radioGroup);
        importing = findViewById(R.id.ImportRadioButton);
        creating = findViewById(R.id.createRadioButton);
        fileName = findViewById(R.id.fileName);
        proceedButton = findViewById(R.id.proceedButton);
        clearButton = findViewById(R.id.ClearButton);

        /*Definition of the adapter for the study ID list*/
        iDAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                studyIDList);
        iDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        iDSpinner.setAdapter(iDAdapter);
        iDSpinner.setOnItemSelectedListener(this);

        /*event handler for button in the main Activity*/
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String inputFileName = fileName.getText().toString();
                if (importing.isChecked()) {
                    if(!inputFileName.equals("")) {
                        /*get the file from the file directory by the input file name*/
                        String sdCardDirectory = Environment.getExternalStorageDirectory().getParent();
                        Log.i(TAG, sdCardDirectory);
//                        File myFile = new File("/sdcard/", inputFileName);
                        File myFile = new File(sdCardDirectory, inputFileName);
                        Log.i(TAG,myFile.getName());
                        if(myFile.exists()){
                            /*read the file if it exist else tell the user the file
                            does not exist
                             */
                            try {
                                pState = new ProgramState();
                                pState.readFile(myFile);
                                Log.i(TAG, pState.getCurrentProgramReadings().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*Read the content of the file and pass the readings to importActivity */
                            intent = new Intent(myContext, ImportActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(myContext, R.string.FILE_NOT_FOUND,
                                    Toast.LENGTH_LONG).show();
                            fileName.requestFocus();
                        }

                    }else{
                        Toast.makeText(myContext, R.string.FILE_NAME_REQUEST,
                                Toast.LENGTH_LONG).show();
                        fileName.requestFocus();
                    }
                } else if(creating.isChecked()){
                    if(!studyId.getText().toString().equals("") && !studyName.getText().toString().equals("")){
                        /*redirect to create activity with input study data*/
                        pState = new ProgramState();
                        pState.createNewStudy(studyId.getText().toString(), studyName.getText().toString());
                        intent = new Intent(myContext, CreateActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(myContext, R.string.STUDY_INPUT_REQUEST,
                                Toast.LENGTH_LONG).show();
                        studyId.requestFocus();
                    }
                }else{
                    Toast.makeText(myContext, R.string.TASK_SELECTION_REQUEST,
                            Toast.LENGTH_LONG).show();
                    studyId.requestFocus();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /* clear all inputs fields */
            public void onClick(View v) {
                studyId.setText("");
                studyName.setText("");
                radioGroup.clearCheck();
                importing.setTextColor(getColor(R.color.whiteText));
                creating.setTextColor(getColor(R.color.whiteText));
                fileName.setText("");
            }
        });

        /*implementation of radio button onclick listeners*/
        importing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importing.setTextColor(getColor(R.color.buttonColor));
                creating.setTextColor(getColor(R.color.whiteText));
            }
        });
        creating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creating.setTextColor(getColor(R.color.buttonColor));
                importing.setTextColor(getColor(R.color.whiteText));
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id){
        studyId.setText(studyIDList[position]);
        Toast.makeText(myContext, "Selected Study ID: "+ studyIDList[position],
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> argo){
        Toast.makeText(myContext, "No Study ID Selected!",
                Toast.LENGTH_LONG).show();
        studyId.requestFocus();
    }

}
