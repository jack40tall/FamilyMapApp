package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;
import exceptions.DataAccessException;
import object.EncoderDecoder;
import request_result.*;
import services.FillService;
import services.RegisterService;


public class RegisterHandler implements HttpHandler {
public static final int DEFAULT_NUM_GENERATIONS = 4;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("In RegisterHandler...");
        EncoderDecoder encoderDecoder = new EncoderDecoder();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                RegisterRequest request = encoderDecoder.deserialize(exchange, RegisterRequest.class);

                RegisterService service = new RegisterService();

                RegisterResult result = service.register(request);

                if(result.isSuccess()) {
//                Generate 4 generations
                    FillService fillService = new FillService();
                    FillRequest fillRequest = new FillRequest(DEFAULT_NUM_GENERATIONS, request.getUserName());
                    fillService.fill(fillRequest);
                }
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
        catch (IOException | DataAccessException e) {

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
