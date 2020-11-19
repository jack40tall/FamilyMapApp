package object;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * An encoder/decoder Object that takes care of conversion between json and Java
 */
public class EncoderDecoder {
    public String serialize(HttpExchange exchange, Object object) throws IOException {

        Gson gson = new Gson();

        String respData = gson.toJson(object);

        return respData;
    }

    public <T> T deserialize(HttpExchange exchange, Class<T> returnType) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        // Read JSON string from the input stream
//                Gson gson = new Gson();
        String reqData = readString(reqBody);
        System.out.println(reqData);
        return (new Gson()).fromJson(reqData, returnType);
    }

    public <T> T deserializeFile(Reader reader, Class<T> returnType) throws IOException {
        return (new Gson()).fromJson(reader, returnType);
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }


}


