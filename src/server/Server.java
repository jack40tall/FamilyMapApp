package server;

import com.sun.net.httpserver.HttpServer;
import file_handler.FileHandler;
import handlers.*;
import load_data.FemaleNames;
import load_data.LocationData;
import load_data.MaleNames;
import load_data.SurNames;
import object.EncoderDecoder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.InetSocketAddress;

public class Server {
    private LocationData locData;
    private MaleNames mNames;
    private FemaleNames fNames;

    public LocationData getLocData() {
        return locData;
    }

    public MaleNames getmNames() {
        return mNames;
    }

    public FemaleNames getfNames() {
        return fNames;
    }

    public SurNames getsNames() {
        return sNames;
    }

    private SurNames sNames;

    // The maximum number of waiting incoming connections to queue.
    // While this value is necessary, for our purposes it is unimportant.
    // Take CS 460 for a deeper understanding of what it means.
    private static final int MAX_WAITING_CONNECTIONS = 12;

    // Java provides an HttpServer class that can be used to embed
    // an HTTP server in any Java program.
    // Using the HttpServer class, you can easily make a Java
    // program that can receive incoming HTTP requests, and respond
    // with appropriate HTTP responses.
    // HttpServer is the class that actually implements the HTTP network
    // protocol (be glad you don't have to).
    // The "server" field contains the HttpServer instance for this program,
    // which is initialized in the "run" method below.
    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber) throws IOException {

        // Since the server has no "user interface", it should display "log"
        // messages containing information about its internal activities.
        // This allows a system administrator (or you) to know what is happening
        // inside the server, which can be useful for diagnosing problems
        // that may occur.
        System.out.println("Initializing HTTP Server");

        try {
            // Create a new HttpServer object.
            // Rather than calling "new" directly, we instead create
            // the object by calling the HttpServer.create static factory method.
            // Just like "new", this method returns a reference to the new object.
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }



//        LoadJsonFiles();

//        Load Test
//        Location temp = locData.getData()[0];
//        System.out.printf("Latitude = %s\n", temp.getLatitude());
//        System.out.printf("Longitude = %s\n", temp.getLongitude());
//        System.out.printf("City = %s\n", temp.getCity());
//        System.out.printf("Country = %s\n", temp.getCountry());
//        String maleName = mNames.getData()[1];
//        System.out.printf("malename = %s\n", maleName);
//        String femaleName = fNames.getData()[1];
//        System.out.printf("fname = %s\n", femaleName);
//        String surName = sNames.getData()[1];
//        System.out.printf("surname = %s\n", surName);


        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        // Log message indicating that the server is creating and installing
        // its HTTP handlers.
        // The HttpServer class listens for incoming HTTP requests.  When one
        // is received, it looks at the URL path inside the HTTP request, and
        // forwards the request to the handler for that URL path.
        System.out.println("Creating contexts");

        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/person", new PersonTreeHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/event", new AllEventHandler());
        server.createContext("/event/", new EventHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/", new FileHandler());


        // Create and install the HTTP handler for the "/games/list" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/games/list" URL path, it will forward the request to ListGamesHandler
        // for processing.
//        server.createContext("/games/list", new ListGamesHandler());

        // Create and install the HTTP handler for the "/routes/claim" URL path.
        // When the HttpServer receives an HTTP request containing the
        // "/routes/claim" URL path, it will forward the request to ClaimRouteHandler
        // for processing.
//        server.createContext("/routes/claim", new ClaimRouteHandler());

        // Log message indicating that the HttpServer is about to start accepting
        // incoming client connections.
        System.out.println("Starting server");

        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method
        // for the program will also complete.
        // Even though the "main" method has completed, the program will continue
        // running because the HttpServer object we created is still running
        // in the background.
        server.start();

        // Log message indicating that the server has successfully started.
        System.out.println("Server started");
    }

    public void LoadJsonFiles() throws IOException {
        EncoderDecoder encoderDecoder = new EncoderDecoder();
        //        Load Data from Json
        System.out.println("Loading Json Data");
        Reader reader = new FileReader("json/locations.json");
        locData = encoderDecoder.deserializeFile(reader, LocationData.class);

        reader = new FileReader("json/mnames.json");
        mNames = encoderDecoder.deserializeFile(reader, MaleNames.class);

        reader = new FileReader("json/fnames.json");
        fNames = encoderDecoder.deserializeFile(reader, FemaleNames.class);

        reader = new FileReader("json/snames.json");
        sNames = encoderDecoder.deserializeFile(reader, SurNames.class);
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    public static void main(String[] args) throws IOException {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
