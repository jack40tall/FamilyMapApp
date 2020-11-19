package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import object.EncoderDecoder;
import request_result.AllEventRequest;
import request_result.AllEventResult;
import services.AllEventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

//TODO: Includes all communication details.
public class AllEventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        AllEventResult result = null;

        System.out.println("In AllEventHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

//        boolean success = false;

        if (exchange.getRequestMethod().toLowerCase().equals("get")) {

//                Get AuthToken
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");

//                    Handle Request
                try {
                    AllEventRequest request = new AllEventRequest(authToken);
                    AllEventService service = new AllEventService();
                    result = service.getAllEvents(request);
//                    success = true;
                }
                catch (DataAccessException ex) {
                    result = new AllEventResult("Error: Internal Server Error", false);
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