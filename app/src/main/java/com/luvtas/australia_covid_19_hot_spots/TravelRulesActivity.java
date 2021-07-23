package com.luvtas.australia_covid_19_hot_spots;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TravelRulesActivity extends AppCompatActivity {
    private TextView vic_rule,tas_rule,nsw_rule,qld_rule,act_rule,sa_rule,wa_rule,nt_rule;
    private ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_rules);
        getDate();
        preview = findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDate() {

        String vic = getIntent().getStringExtra("vic");
        String tas = getIntent().getStringExtra("tas");
        String nsw = getIntent().getStringExtra("nsw");
        String qld = getIntent().getStringExtra("qld");
        String act = getIntent().getStringExtra("act");
        String sa = getIntent().getStringExtra("sa");
        String wa = getIntent().getStringExtra("wa");
        String nt = getIntent().getStringExtra("nt");
        setDate(vic,tas,nsw,qld,act,sa,wa,nt);
    }

    private void setDate(String vic, String tas, String nsw, String qld, String act, String sa, String wa, String nt) {
        vic_rule = findViewById(R.id.vic_rule);
        tas_rule = findViewById(R.id.tas_rule);
        nsw_rule = findViewById(R.id.nsw_rule);
        qld_rule = findViewById(R.id.qld_rule);
        act_rule = findViewById(R.id.act_rule);
        sa_rule = findViewById(R.id.sa_rule);
        wa_rule = findViewById(R.id.wa_rule);
        nt_rule = findViewById(R.id.nt_rule);

        vic_rule.setText(vic);
        tas_rule.setText(tas);
        nsw_rule.setText(nsw);
        qld_rule.setText(qld);
        act_rule.setText(act);
        sa_rule.setText(sa);
        wa_rule.setText(wa);
        nt_rule.setText(nt);
    }
}
