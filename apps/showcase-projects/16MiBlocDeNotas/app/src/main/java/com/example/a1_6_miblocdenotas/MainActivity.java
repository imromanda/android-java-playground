package com.example.a1_6_miblocdenotas;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos del layout
        ImageView imgIcon = findViewById(R.id.imgIcon);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        Button btnShow = findViewById(R.id.btnShow);
            // NUEVO: referencia al bloque informativo (apartado 6.6)
             LinearLayout infoBlock = findViewById(R.id.infoBlock);
             TextView txtInfo = findViewById(R.id.txtInfo);
             ImageView imgPencil = findViewById(R.id.imgPencil);

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



    }



}



