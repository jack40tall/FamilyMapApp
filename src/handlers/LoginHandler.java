package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import exceptions.DataAccessException;
import object.EncoderDecoder;
import request_result.LoginRequest;
import request_result.LoginResult;
import services.LoginService;


public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("In LoginHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

//        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                LoginRequest request = encoderDecoder.deserialize(exchange, LoginRequest.class);
//                LoginRequest request = gson.fromJson(reqData, LoginRequest.class);

//                String userName = request.getUserName();
//                System.out.printf("userName = %s\n", userName);
//                String password = request.getPassword();
//                System.out.printf("password = %s\n", password);

                // Display/log the request JSON data


                LoginService service = new LoginService();

                LoginResult result = service.login(request);

                String respData = encoderDecoder.serialize(exchange, result);

                if(result.isSuccess() == true) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream respBody = exchange.getResponseBody();

                writeString(respData, respBody);

                exchange.getResponseBody().close();

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
        }
        catch (IOException | DataAccessException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
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
