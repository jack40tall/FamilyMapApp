package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.DataAccessException;
import object.EncoderDecoder;
import request_result.EventRequest;
import request_result.EventResult;
import request_result.PersonRequest;
import request_result.PersonResult;
import services.EventService;
import services.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    //        Returns the single Event Object with specified ID
        EventResult result = null;

        System.out.println("In EventHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String uriInfo = exchange.getRequestURI().toString();
                String eventID = uriInfo.substring(7);
                System.out.printf("eventID = %s\n", eventID);

    //                Get AuthToken
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

    //                    Handle Request
                    EventRequest request = new EventRequest(eventID, authToken);
                    EventService service = new EventService();
                    result = service.getEvent(request);
                    success = true;
                }
            }
            if(!success) {
    //                throw exception
            }

        } catch (DataAccessException e) {
            result = new EventResult("Error: Indicated person not found in Database", false);

        } finally {
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