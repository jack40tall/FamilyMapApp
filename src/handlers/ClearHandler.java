package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import object.EncoderDecoder;
import request_result.*;
import services.ClearService;


public class ClearHandler implements HttpHandler {
    public static final int DEFAULT_NUM_GENERATIONS = 4;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("In ClearHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                ClearService service = new ClearService();

                ClearResult result = service.clear();

                String respData = encoderDecoder.serialize(exchange, result);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();

                writeString(respData, respBody);

                exchange.getResponseBody().close();

            }
        }
        catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }


    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
