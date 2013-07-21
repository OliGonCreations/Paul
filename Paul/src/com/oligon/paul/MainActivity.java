package com.oligon.paul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends Activity {
    Shaker mShaker;
    Button bt1;
    TextView trinkspruch;
    TextView land;
    SoundPool countSound;
    int sound;
    int min = 1;
    int max;
    public static DatabaseHandler db;
    static Resources res;
    public static String[] autoCompArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setIcon(R.drawable.innericon);   //Icon in der Actionbar
        res = getResources();
        Log.d("Database", "creating Database");
        db = new DatabaseHandler(this);
        Log.d("Inserting", "insterting");
        Log.d("Var", "" + max);


        countSound = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        sound = countSound.load(this, R.raw.paulsound, 1);

        bt1 = (Button) findViewById(R.id.button1);
        trinkspruch = (TextView) findViewById(R.id.trinkspruch1);
        land = (TextView) findViewById(R.id.land1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound != 0) {
                    countSound.play(sound, 1, 1, 0, 0, 1);
                }
                max = db.getSpruchCount();
                if (max != 0) {

                    int res;
                    Random r = new Random();
                    res = min + r.nextInt(max);

                    Log.d("random", "" + res);

                    // trinkspruch.setText("" + array[res][0]);
                    trinkspruch.setText("" + db.getSpruch(res)._spruch);
                    land.setText("" + db.getSpruch(res)._land);
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mShaker = new Shaker(this, 1.5d, 0, new Shaker.Callback() {

            @Override
            public void shakingStopped() {
                if (sound != 0) {
                    countSound.play(sound, 1, 1, 0, 0, 1);
                }
                max = db.getSpruchCount();
                if (max != 0) {
                    int res;
                    Random r = new Random();
                    res = min + r.nextInt(max);

                    Log.d("random", "" + res);

                    // trinkspruch.setText("" + array[res][0]);
                    trinkspruch.setText("" + db.getSpruch(res)._spruch);
                    land.setText("" + db.getSpruch(res)._land);
                }


            }

            @Override
            public void shakingStarted() {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShaker.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                DialogAdd add = new DialogAdd();
                add.show(getFragmentManager(), "add");
                break;
            //case R.id.action_filter:
        }
        return super.onOptionsItemSelected(item);
    }

    public static String[][] loadArray() {
        InputStream streamland = res.openRawResource(R.raw.laender);

        ByteArrayOutputStream lnd = new ByteArrayOutputStream();

        int i;
        try {
            i = streamland.read();
            while (i != -1) {
                lnd.write(i);
                i = streamland.read();
            }
            streamland.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res1 = lnd.toString();
        String[] splittedlnd = res1.split("\n");

        InputStream spruch = res.openRawResource(R.raw.sprueche);
        ByteArrayOutputStream spr = new ByteArrayOutputStream();

        int j;
        try {
            j = spruch.read();
            while (j != -1) {
                spr.write(j);
                j = spruch.read();
            }
            spruch.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String spr1 = spr.toString();
        String[] splittedspr = spr1.split("\n");

        String[][] data = new String[splittedspr.length][2];

        for (int k = 0; k < data.length; k++) {
            data[k][0] = splittedspr[k];
            data[k][1] = splittedlnd[k];
        }

        return data;
    }

    public static String[] loadAutoCompArray() { //befüllt ein array mit den Ländern, die für das Autocomplete verwendet werden
        InputStream autoCompStream = res.openRawResource(R.raw.autocomp);

        ByteArrayOutputStream lnd = new ByteArrayOutputStream();

        int i;
        try {
            i = autoCompStream.read();
            while (i != -1) {
                lnd.write(i);
                i = autoCompStream.read();
            }
            autoCompStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res1 = lnd.toString();
        String[] splittedAutoComp = res1.split("\n");

        return splittedAutoComp;
    }

    public static class DialogAdd extends DialogFragment {

        String item[] = {
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };
        AutoCompleteTextView actvLand;
        EditText etSpruch;
        //Button bt_ok, bt_Abbrechen;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Custom Dialog-Layout
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_add, null);

            autoCompArray = loadAutoCompArray();
            etSpruch = (EditText) view.findViewById(R.id.etSpruch);
            actvLand = (AutoCompleteTextView) view.findViewById(R.id.actwLand);
            actvLand.setThreshold(1);
            //Bei Fragments immer 'this' in Constructoren zu 'getActivity()' ändern!!
            actvLand.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autoCompArray));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d("Database", "creating Database");
                            if (etSpruch.getText().toString().equals("") || actvLand.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Leerer Spruch!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                db.addSpruch(new Spruch(etSpruch.getText().toString(),
                                        actvLand.getText().toString()));
                                Toast.makeText(getActivity(), "Spruch gespeichert",
                                        Toast.LENGTH_SHORT).show();
                            }
                            getDialog().dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getDialog().dismiss();
                        }
                    });

            return builder.create();
            /*
            getDialog().setTitle(getResources().getString(R.string.dialog_add));
            getDialog().setCanceledOnTouchOutside(false);

            return inflater.inflate(R.layout.dialog_add, container, false);*/
        }


        // Wird nicht mehr benötigt!
            /*
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            //erstellen eines Adapters für das Autocomplete

            autoCompArray = loadAutoCompArray();


            etSpruch = (EditText) getView().findViewById(R.id.etSpruch);
            actvLand = (AutoCompleteTextView) getView().findViewById(R.id.actwLand);
            actvLand.setThreshold(1);
            //Bei Fragments immer 'this' in Constructoren zu 'getActivity()' ändern!!
            actvLand.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, item));


            bt_ok = (Button) getView().findViewById(R.id.bt_ok);
            bt_Abbrechen = (Button) getView().findViewById(R.id.bt_cancel);
            bt_ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("Database", "creating Database");
                    if (etSpruch.getText().toString().equals("") || actvLand.getText().toString().equals("")) {
                        getDialog().dismiss();
                        Toast.makeText(getActivity(), "Leerer Spruch!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        db.addSpruch(new Spruch(etSpruch.getText().toString(),
                                actvLand.getText().toString()));
                        Toast.makeText(getActivity(), "Spruch gespeichert",
                                Toast.LENGTH_SHORT).show();

                        getDialog().dismiss();
                    }
                }
            });
            bt_Abbrechen.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
        }

            */
    }

}
