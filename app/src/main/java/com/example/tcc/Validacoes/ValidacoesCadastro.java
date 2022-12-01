package com.example.tcc.Validacoes;

import android.net.MailTo;

import java.util.InputMismatchException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidacoesCadastro {

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }

    public static boolean validarEmail(String email){
        boolean ehEmail = true;
        try {
            InternetAddress emailAddres = new InternetAddress(email);
        } catch (AddressException e) {
            ehEmail = false;
        }
        return ehEmail;
    }

    public static String validarDataNasi(String data){
        try
        {
            Integer dia = Integer.parseInt(data.substring(0,2)) ;
            Integer mes = Integer.parseInt(data.substring(2,4)) ;
            Integer ano = Integer.parseInt(data.substring(4,8)) ;

            if(dia<1 || dia>31){
                return ("dia invalido");
            }
            else if(mes<1 || mes>12){
                return ("mes invalido");
            }
            else if(ano<1920 || ano>2020){
                return ("ano invalido");
            }
            else{
                return (null);
            }
        } catch (NumberFormatException e) {
            return "data inválida";
        }
    }

    public static String validarSenha(String senha){
        if(senha.length()<6){
            return ("minimo de 6 caracteres");
        }
        else if(!senha.matches(".*[A-Z].*")){
            return ("deve conter ao menos 1 letra maiuscula");
        }
        else if(!senha.matches(".*[a-z].*")){
            return ("deve conter ao menos 1 letra minuscula");
        }
        else if(senha.length()>16)
        {
            return ("maximo de 16 caracteres");
        }
        else if(!senha.matches(".*[@#/@?$%&+=~^.,*!();:´`{}_-].*"))
        {
            return ("deve conter ao menos 1 caracter especial");
        }
        else if(!senha.toString().matches(".*[0-9].*"))
        {
            return ("deve conter ao menos 1 número");
        }
        else{
            return (null);
        }
    }

}
