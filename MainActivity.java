package com.example.calculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    public static final int MODE_STANDARD   = 0;
    public static final int MODE_SCIENTIFIC = 1;
    public static final int MODE_CONVERTER  = 2;

    private int currentMode = MODE_STANDARD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Calculatrice");
        }

        if (savedInstanceState == null) {
            loadFragment(new StandardFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_standard) {
            if (currentMode != MODE_STANDARD) {
                currentMode = MODE_STANDARD;
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Calculatrice");
                loadFragment(new StandardFragment());
            }
            return true;
        } else if (id == R.id.menu_scientific) {
            if (currentMode != MODE_SCIENTIFIC) {
                currentMode = MODE_SCIENTIFIC;
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Mode Scientifique");
                loadFragment(new ScientificFragment());
            }
            return true;
        } else if (id == R.id.menu_converter) {
            if (currentMode != MODE_CONVERTER) {
                currentMode = MODE_CONVERTER;
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Convertisseur de Bases");
                loadFragment(new ConverterFragment());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
