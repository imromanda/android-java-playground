package com.example.my_meteo_app;

/*Este WeatherMapper va a encargarse de:

✔️ convertir weatherCode + isDay → icono
✔️ convertir weatherCode → descripción

*/

public class WeatherMapper {

    public static int getIcon(int weatherCode, int isDay) {

        boolean isDayTime = (isDay == 1);

        switch (weatherCode) {

            case 0:
                return isDayTime ? R.drawable.w01d : R.drawable.w01n;

            case 1:
                return isDayTime ? R.drawable.w02d : R.drawable.w02n;

            case 2:
                return isDayTime ? R.drawable.w03d : R.drawable.w03n;

            case 3:
                return isDayTime ? R.drawable.w04d : R.drawable.w04n;

            case 45:
            case 48:
                return isDayTime ? R.drawable.w50d : R.drawable.w50n;

            case 51:
            case 53:
            case 55:
                return isDayTime ? R.drawable.w09d : R.drawable.w09n;

            case 61:
            case 63:
            case 65:
                return isDayTime ? R.drawable.w10d : R.drawable.w10n;

            case 80:
            case 81:
            case 82:
                return isDayTime ? R.drawable.w09d : R.drawable.w09n;

            case 95:
            case 96:
            case 99:
                return isDayTime ? R.drawable.w11d : R.drawable.w11n;

            default:
                return isDayTime ? R.drawable.w01d : R.drawable.w01n;
        }
    }

    public static String getDescription (int weatherCode) {
    switch (weatherCode) {
      case 0:
        return "Despejado";
      case 1:
        return "Mayormente despejado";
      case 2:
        return "Parcialmente nublado";
      case 3:
        return "Nublado";
      case 45:
      case 48:
        return "Niebla";
      case 51:
      case 53:
      case 55:
        return "Llovizna";
      case 61:
      case 63:
      case 65:
        return "Lluvia";
      case 80:
      case 81:
      case 82:
        return "Chubascos";
      case 95:
          return "Tormenta";
      case 96:
      case 99:
        return "Tormenta con granizo";
      default:
        return "Desconocido";
    }
  }
}
