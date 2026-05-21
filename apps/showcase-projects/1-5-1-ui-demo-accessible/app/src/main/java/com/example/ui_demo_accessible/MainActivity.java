package com.example.ui_demo_accessible;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ui_demo_accessible.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding; // ViewBinding para activity_main.xml

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflamos el layout usando ViewBinding
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Listener moderno con lambda
    binding.button1.setOnClickListener(
        v -> {
          // Acción al presionar el botón: Cambiar el texto
          binding.textTitle.setText("¡Has pulstado el boton!");
        });

    // Listener segundo botón con lambda
    binding.button2.setOnClickListener(
        v -> {
          // Acción al presionar el botón: Mostrar el FrameLayout
          binding.frameContent.setVisibility(View.VISIBLE);
        });

    // Listener tercer botón con lambda
    binding.button3.setOnClickListener(
        v -> {
          // Acción al presionar el botón: Vuelve al estado inicial
          binding.textTitle.setText("Demo de Interfaz");
          binding.frameContent.setVisibility(View.GONE);
        });

    // Funcionalidad para escuchar el input newMessage
    binding.newMessage.addTextChangedListener(
        // nuevo objeto TextWatcher que revisa cambios en el texto del input
        /*Tiene 3 métodos obligatorios,
        que hay que declarar aunque no se usen.
        Aquí vamos a usar solo el afterTextChanged
        * */
        new TextWatcher() {

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            // actualiza el textOverlay mientras onTextChanged
            binding.textOverlay.setText(s.toString());
          }

          @Override
          public void afterTextChanged(Editable s) {
            /*Habilita el botón Refresh si s (que es el texto que se ha cambiado)
             es convertido a string
            le quitas lo que sobra antes y después
            y está vacío
            */
            binding.btnRefresh.setEnabled(!s.toString().trim().isEmpty());
          }
        });

    // El botón Refresh debe estar deshabilitado de primeras
    binding.btnRefresh.setEnabled(false);

    // Listener botón 4 con lambda
    binding.btnRefresh.setOnClickListener(
        v -> {
          /*onClick = Cambia el texto del overlay por lo introducido en el textOverlay */
          binding.textOverlay.setText(binding.newMessage.getText().toString());
        });

    // Switch que oculta y muestra el texto superpuesto
    binding.switchMessage.setOnCheckedChangeListener(
        // Detecta cuando el switch cambia de estado y revisa si está activo(true) o no(false)
        (buttonView, isChecked) -> {
          // Si está activo oculta la visibilidad del elemento newMessage y del botón de refrescar
          // mensaje
          if (isChecked) {
            binding.newMessage.setVisibility(View.GONE);
            binding.btnRefresh.setVisibility(View.GONE);
            binding.textOverlay.setVisibility(View.GONE);

            // Si está desactivado muestra elemento newMessage  y del botón de refrescar mensaje
          } else {
            binding.newMessage.setVisibility(View.VISIBLE);
            binding.btnRefresh.setVisibility(View.VISIBLE);
            binding.textOverlay.setVisibility(View.VISIBLE);
          }
        });
  }
}
