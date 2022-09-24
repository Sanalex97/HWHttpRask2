import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=hdfOW39Q2IUunkgJFbKeRbqaCMaupH7mwkIdLzcY");
        CloseableHttpResponse response = httpClient.execute(request);

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseServer responseServer = objectMapper.readValue(response.getEntity().getContent().readAllBytes(), new TypeReference<>() {
        });

        String url = responseServer.getUrl();

        HttpGet request2 = new HttpGet(url);
        CloseableHttpResponse response2 = httpClient.execute(request2);


        String[] arrays = url.split("/");
        String nameFile = arrays[arrays.length - 1];

        File file = new File("D:\\WorkPlace\\Курс по java\\НЕТОЛОГИЯ\\Модуль 4\\" + nameFile);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] bytes = response2.getEntity().getContent().readAllBytes();
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
