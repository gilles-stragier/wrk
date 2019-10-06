package net.ocheyedan.wrk;

import com.fasterxml.jackson.core.type.TypeReference;
import net.ocheyedan.wrk.output.Output;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * User: blangel
 * Date: 6/29/12
 * Time: 9:26 PM
 */
public class RestTemplate {

    public <T> T get(String restfulEndpoint, TypeReference<T> forResultType) {
        return invoke(restfulEndpoint, "GET", forResultType);
    }

    public <T> T post(String restfulEndpoint, TypeReference<T> forResultType) {
        return invoke(restfulEndpoint, "POST", forResultType);
    }

    public <T> T put(String restfulEndpoint, TypeReference<T> forResultType) {
        return invoke(restfulEndpoint, "PUT", forResultType);
    }

    public <T> T delete(String restfulEndpoint, TypeReference<T> forResultType) {
        return invoke(restfulEndpoint, "DELETE", forResultType);
    }

    private <T> T invoke(String restfulEndpoint, String method, TypeReference<T> forResultType) {
        T result = null;
        InputStream stream = null;
        try {
            URL url = new URL(restfulEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(method);

            stream = connection.getInputStream();
           // displayResult(stream);
            result = Json.mapper().readValue(stream, forResultType);
            connection.disconnect();
        } catch (FileNotFoundException fnfe) {
            return null;
        } catch (IOException ioe) {
            if (ioe.getMessage().startsWith("Server returned HTTP response code: 400")) {
                return result;
            } else {
                Output.print(ioe);
            }
        } catch (Exception e) {
            Output.print(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                    Output.print(ioe);
                }
            }
        }
        return result;
    }

    private void displayResult(InputStream stream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        System.out.println(textBuilder);
    }

    public RestTemplate() { }

}
