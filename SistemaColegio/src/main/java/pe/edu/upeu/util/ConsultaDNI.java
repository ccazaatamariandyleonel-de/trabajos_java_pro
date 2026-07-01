package pe.edu.upeu.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class ConsultaDNI {

    public PersonaDto consultarDNI(String dni) {
        PersonaDto personaDto = new PersonaDto();
        String url = "https://eldni.com/pe/buscar-datos-por-dni";

        try {
            // 1. Conexión GET inicial para obtener el token
            // Usamos userAgent para simular un navegador real y evitar bloqueos
            Connection.Response getResponse = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .method(Connection.Method.GET)
                    .execute();

            Document doc = getResponse.parse();
            String token = doc.select("input[name=_token]").attr("value");

            // 2. Conexión POST para enviar el DNI
            Connection.Response postResponse = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .cookies(getResponse.cookies())
                    .data("_token", token)
                    .data("dni", dni)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();

            // 3. Procesar respuesta
            Document resultDoc = Jsoup.parse(postResponse.body());
            Element fila = resultDoc.select("table tbody tr").first();

            if (fila != null) {
                Elements celdas = fila.select("td");
                if (celdas.size() >= 4) {
                    personaDto.setDni(celdas.get(0).text());
                    personaDto.setNombre(celdas.get(1).text());
                    personaDto.setApellidoPaterno(celdas.get(2).text());
                    personaDto.setApellidoMaterno(celdas.get(3).text());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al conectar con la API: " + e.getMessage());
        }

        return personaDto;
    }
}