package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import exceptions.DataAccessException;
import exceptions.IncorrectFillException;
import object.EncoderDecoder;
import request_result.FillRequest;
import request_result.FillResult;
import services.FillService;

import static java.lang.Integer.parseInt;


public class FillHandler implements HttpHandler {
    private static final int DEFAULT_NUM_GENERATIONS = 4;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("In FillHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();
        FillResult result = null;

//        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String uriInfo = exchange.getRequestURI().toString();
                System.out.printf("URIInfo = %s\n", uriInfo);
                String[] parameters = uriInfo.split("/");

                int numGenerations = DEFAULT_NUM_GENERATIONS;
                String username;

                if(parameters.length == 3) {
                    username = parameters[2];
                }
                else if (parameters.length == 4){
                    username = parameters[2];
                    String stringGenerations = parameters[3];
                    numGenerations = parseInt(stringGenerations);
                    if (numGenerations < 0) {
                        throw new IncorrectFillException("Error: Incorrect Username or Generations");
                    }
                }
                else {
                    throw new IncorrectFillException("Error: Incorrect Username or Generations");
                }



                FillRequest request = new FillRequest(numGenerations, username);


                FillService service = new FillService();

                result = service.fill(request);

//                success = true;
            }

//            if (!success) {
//                // The HTTP request was invalid somehow, so we return a "bad request"
//                // status code to the client.
//                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
//                // We are not sending a response body, so close the response body
//                // output stream, indicating that the response is complete.
//                exchange.getResponseBody().close();
//            }
        } catch(IncorrectFillException | DataAccessException e) {
            result = new FillResult("Error: Invalid Username or Generations", false);

        }
        finally {
            String respData = encoderDecoder.serialize(exchange, result);

            if(result.isSuccess()) {
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
