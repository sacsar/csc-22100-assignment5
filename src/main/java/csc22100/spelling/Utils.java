package csc22100.spelling;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static List<String> getLinesFromFile(Path path) throws IOException {
        try(InputStream inputStream = new FileInputStream(path.toFile())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().collect(Collectors.toList());
        }
    }

    public static void download(URI url, Path destination) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(url).build();
        HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofFile(destination));
    }

    public static void writeJson(Path path, Object obj) throws IOException {
        OBJECT_MAPPER.writeValue(path.toFile(), obj);
    }

    private Utils() {}
}
