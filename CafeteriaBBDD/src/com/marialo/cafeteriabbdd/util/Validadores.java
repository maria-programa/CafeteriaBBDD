package com.marialo.cafeteriabbdd.util;

import java.util.regex.Pattern;

public class Validadores {
    public static boolean validarDNI(String dni) {
        // Primero si el DNI no tiene nueve caracteres dará error
        String dniClean = dni.toUpperCase();
        if (dniClean.length() != 9) {
            return false;
        }

        String numero = dniClean.substring(0, 8);
        String letra = dniClean.substring(8);

        try {
            // El dni ha de ser correcto, es decir, que el número coincida con la letra
            int num = Integer.parseInt(numero);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            char letraCalculada = letras.charAt(num % 23);

            return letra.charAt(0) == letraCalculada;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validarEmail(String email) {
        // Para que un correo sea válido tiene que tener caracteres entre A-Z, a-z, 0-9, _ o .
        // una arroba y luego mas caracteres entre a-z, A-Z, un . y un dominio
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
