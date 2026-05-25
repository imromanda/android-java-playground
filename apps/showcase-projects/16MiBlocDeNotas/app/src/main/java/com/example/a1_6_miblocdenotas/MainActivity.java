package com.example.a1_6_miblocdenotas;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        ImageView imgIcon = findViewById(R.id.imgIcon);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        MaterialButton btnShow = findViewById(R.id.btnShow);
            // NUEVO: referencia al bloque informativo (apartado 6.6)
             LinearLayout infoBlock = findViewById(R.id.infoBlock);
             TextView txtInfo = findViewById(R.id.txtInfo);
             ImageView imgPencil = findViewById(R.id.imgPencil);
        // Capturamos el switch de cambio tema Claro/Oscuro (apartado 6.12.3)
        SwitchMaterial switchTheme = findViewById(R.id.switchTheme);


        // Acción del botón: mostrar un mensaje usando un recurso string
        btnShow.setOnClickListener(v -> {
            // getString() obtiene un texto desde strings.xml
            String message = getString(R.string.note_message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Cambiar el texto del bloque informativo usando getString()
                txtInfo.setText(getString(R.string.label_description2));

        // Cambiar el icono del bloque informativo usando getDrawable()
                imgPencil.setImageDrawable(getDrawable(R.drawable.ic_lapiz_flip));

// NUEVO: cambio dinámico tras unos segundos (apartado 6.6 + 6.7)
        new android.os.Handler().postDelayed(() -> {

            // Cambiar el tamaño usando una dimensión desde recursos
            int newSize = (int) getResources().getDimension(R.dimen.icon_small);
            imgPencil.getLayoutParams().width = newSize;
            imgPencil.getLayoutParams().height = newSize;
            imgPencil.requestLayout();

        }, 2000); // 2000 ms = 2 segundos


        // SEGUNDO CAMBIO (3 segundos): colores del bloque informativo
        new android.os.Handler().postDelayed(() -> {
            infoBlock.setBackgroundColor(getColor(R.color.info_bg_highlight));
            txtInfo.setTextColor(getColor(R.color.info_text_highlight));

        }, 3000); // 3000 ms = 3 segundos

        // TERCER CAMBIO (4 segundos): aplicar estilo destacado al texto informativo
        new android.os.Handler().postDelayed(() -> {

            txtInfo.setTextAppearance(R.style.InfoTextStyleHighlight);

        }, 4000);

// CUARTO CAMBIO (5 segundos): cambiar estilo Material del botón
        new android.os.Handler().postDelayed(() -> {

            btnShow.setTextAppearance(R.style.PrimaryMaterialButtonStyleAlt);
            btnShow.setBackgroundTintList(
                    getColorStateList(R.color.teal_200)
            );

        }, 5000);

        // QUINTO CAMBIO (6 segundos): cambiar tipografía dinámicamente
        new android.os.Handler().postDelayed(() -> {

            txtInfo.setTypeface(
                    ResourcesCompat.getFont(this, R.font.libre_caslon)
            );

        }, 6000);

// (apartado 6.14.5) Animamos el sexto cambio para que TODA la pantalla haga un “zoom suave”
// Escuchamos cambios en el switch y aplicamos el tema correspondiente según si está activado o no
        LinearLayout  rootLayout = findViewById(R.id.rootLayout);
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Animación en el layout completo
            rootLayout.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(400)
                    .withEndAction(() ->
                            rootLayout.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(400)
                                    .start()
                    )
                    .start();

            // Retraso para que la animación se vea antes de recrear la Activity
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        AppCompatDelegate.setDefaultNightMode(
                            isChecked
                                    ? AppCompatDelegate.MODE_NIGHT_YES
                                    : AppCompatDelegate.MODE_NIGHT_NO
                    );
            }, 250);
        });



        MaterialCardView cardDemo = findViewById(R.id.cardDemo);
        cardDemo.setAlpha(0f);
        cardDemo.animate()
                .alpha(1f)
                .setDuration(800)
                .start();

// SÉPTIMO CAMBIO (8 segundos): aumentar la elevación a 20(float)
        new Handler().postDelayed(() -> {
            cardDemo.setCardElevation(20f);
            // Animamos la card con un cambio de escala (apartado 6.14.2)
            cardDemo.setScaleX(0.8f);
            cardDemo.setScaleY(0.8f);
            cardDemo.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(1000)
                    .start();
            cardDemo.setTranslationY(50f);
            cardDemo.animate()
                    .translationY(0f)
                    .setDuration(1000)
                    .start();

        }, 8000);


        // OCTAVO CAMBIO. Un pequeño “tilt” o inclinación a los 9 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            cardDemo.animate()
                    .rotation(5f)
                    .setDuration(300)
                    .withEndAction(() ->
                            cardDemo.animate()
                                    .rotation(0f)
                                    .setDuration(300)
                                    .start()
                    )
                    .start();
        }, 9000);




    }



}



