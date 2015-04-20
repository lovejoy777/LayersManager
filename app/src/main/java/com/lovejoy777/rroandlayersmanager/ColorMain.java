package com.lovejoy777.rroandlayersmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * Created by lovejoy777 on 07/04/15.
 */
public class ColorMain extends Activity implements com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener {

    private ColorPicker picker;
    private SVBar svBar;
    private OpacityBar opacityBar;
    private Button button;
    private TextView text;
    private SharedPreference sharedPreference;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);

        sharedPreference = new SharedPreference();
        picker = (ColorPicker) findViewById(R.id.picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        button = (Button) findViewById(R.id.button1);
        text = (TextView) findViewById(R.id.textView1);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                text.setTextColor(picker.getColor());
                picker.setOldCenterColor(picker.getColor());

                // Save the int color to hvscol SharedPreference
                int hvscol = picker.getColor();

                // Save the String color to hexcol SharedPreference
                String hexcol = ("#"+ Integer.toHexString(hvscol));

                // Save the String & int color to hexcol & hvscol Extras and start Settings
                putExtrasToSettings();

                finish();
            }
        });
    }

    @Override
    public void onColorChanged(int color) {
        //gives the color when it's changed.
    }

    public void putExtrasToSettings() {
        int hvscol = picker.getColor();
        String hexcol = ("#"+ Integer.toHexString(hvscol));
        overridePendingTransition(R.anim.back2, R.anim.back1);
        Intent iIntent = new Intent(this, Settings.class);
        iIntent.putExtra("hexcol", hexcol);
        iIntent.putExtra("hvscol", hvscol);
        startActivity(iIntent);

    }

    public void savePrefs(String key, int value) {

        SharedPreferences newcol = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = newcol.edit();
        edit.putInt(key, value);
        edit.commit();
    }
}
