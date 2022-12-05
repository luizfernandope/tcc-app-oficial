package com.example.tcc.Interfaces;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EnvioEmailFastthoot {

    public EnvioEmailFastthoot(String assunto, String emailTo, String textoMensagem){

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String value = "{\r\"personalizations\": [\r {\r\"to\": [\r {\r\"email\": \""+ emailTo +"\"\r}\r],\r\"subject\": \""+ assunto +"\"\r}\r],\r\"from\": {\r\"email\": \"tccmewtwo@example.com\"\r},\r\"content\": [\r{\r\"type\": \"text/plain\",\r\"value\": \""+ textoMensagem +"\"\r}\r]\r}";
        RequestBody body = RequestBody.create(mediaType, value);
        Request request = new Request.Builder()
                .url("https://rapidprod-sendgrid-v1.p.rapidapi.com/mail/send")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", "cdcc65fc17msh0cb7eb7078a09e7p1e10d6jsnb5ca90c18ae4")
                .addHeader("X-RapidAPI-Host", "rapidprod-sendgrid-v1.p.rapidapi.com")
                .build();

        okhttp3.Call response = client.newCall(request);
        response.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("Erro -->\n");
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                if (response.isSuccessful()){
                    System.out.println("\nFuncionou!");
                    System.out.println("\nTexto de mensagem --> \n\n" + textoMensagem);
                }
            }
        });
    }
}
