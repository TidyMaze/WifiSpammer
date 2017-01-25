package fr.yaro.wifispammer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static fr.yaro.wifispammer.R.id.messagesInput;

public class ConsoleActivity extends AppCompatActivity {

    static TextView console = null;
    EditText messagesInput = null;
    String lines = "";
    private Timer timer;

    void addConsole(final String text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                console.append(text + "\n");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);

        console = (TextView) findViewById(R.id.console);
        messagesInput = (EditText) findViewById(R.id.messagesInput);
        messagesInput.setText(lines);

        messagesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lines = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(ConsoleActivity.this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + ConsoleActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        final int[] i = {0};


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String[] eachLine = lines.split("\n");
                if(i[0] >= eachLine.length) i[0] = 0;
                String line = eachLine[i[0]];
                addConsole("Setting name : " + line);


                ApManager.setApState(ConsoleActivity.this, false);
                sleepABit();
                ApManager.setApState(ConsoleActivity.this, true);
                sleepABit();
                ApManager.setApState(ConsoleActivity.this, true);
                ApManager.setHotspotName(line, ConsoleActivity.this);
                i[0]++;
            }
        }, 0, 15000);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        timer.purge();
        super.onDestroy();
    }

    private static void sleepABit(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
