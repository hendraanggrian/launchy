package com.example.rxactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import kota.dialogs.OkButton;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;

import static com.hendraanggrian.app.ActivityCallbacksKt.onActivityResult2;
import static com.hendraanggrian.app.StartActivityForResult2Kt.startActivityForResult2;
import static java.lang.String.format;
import static kota.FragmentManagersV4Kt.addNow;
import static kota.LogsKt.debug;
import static kota.dialogs.AlertsV7Kt.supportAlert;

public class MainActivity2 extends AppCompatActivity {

    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    private Toolbar toolbar;
    private Toolbar toolbar2;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        debug(this, format("%s onCreate", this));

        toolbar = findViewById(R.id.toolbar);
        toolbar2 = findViewById(R.id.toolbar2);
        textView = findViewById(R.id.textView);

        setSupportActionBar(toolbar);
        if (getSupportFragmentManager().findFragmentByTag(TAG_RETAINED_FRAGMENT) == null) {
            addNow(getSupportFragmentManager(), R.id.container, new Content(), TAG_RETAINED_FRAGMENT);
        }
        toolbar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult2(MainActivity2.this, new Intent(MainActivity2.this, NextActivity2.class).putExtra("from", "ACTIVITY"), new Function4<MainActivity2, Integer, Integer, Intent, Unit>() {
                    @Override
                    public Unit invoke(MainActivity2 activity, Integer requestCode, Integer resultCode, Intent data) {
                        debug(activity, format("%s onDestroy", activity));
                        supportAlert(activity, "Callback", format("requestCode = %s, resultCode = %s", requestCode, resultCode), OkButton.Companion);
                        activity.textView.setText("got result...");
                        return null;
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debug(this, format("%s onDestroy", this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResult2(this, requestCode, resultCode, data);
    }

    public static class Content extends PreferenceFragmentCompat {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.activity_main);
            findPreference("fragmentPreference").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivityForResult2(Content.this, new Intent(getContext(), NextActivity2.class).putExtra("from", "FRAGMENT"), new Function4<Content, Integer, Integer, Intent, Unit>() {
                        @Override
                        public Unit invoke(Content fragment, Integer requestCode, Integer resultCode, Intent intent) {
                            supportAlert(fragment, "Callback", format("requestCode = %s, resultCode = %s", requestCode, resultCode), OkButton.Companion);
                            return null;
                        }
                    });
                    return false;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            onActivityResult2(this, requestCode, resultCode, data);
        }
    }
}