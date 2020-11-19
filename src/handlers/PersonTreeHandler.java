package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import object.EncoderDecoder;
import request_result.PersonTreeRequest;
import request_result.PersonTreeResult;
import services.PersonTreeService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class PersonTreeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        PersonTreeResult result = null;

        System.out.println("In PersonTreeHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

//        boolean success = false;

        if (exchange.getRequestMethod().toLowerCase().equals("get")) {

//                Get AuthToken
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");

//                    Handle Request
                try {
                    PersonTreeRequest request = new PersonTreeRequest(authToken);
                    PersonTreeService service = new PersonTreeService();
                    result = service.getPersonTree(request);
//                    success = true;
                }
                catch (DataAccessException ex) {
                    result = new PersonTreeResult("Error: Internal Server Error", false);
                }
            }
        }

        String respData = encoderDecoder.serialize(exchange, result);

        if (result.isSuccess()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }

        OutputStream respBody = exchange.getResponseBody();

        writeString(respData, respBody);

        exchange.getResponseBody().close();
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}