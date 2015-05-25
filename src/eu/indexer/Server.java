package eu.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * Created by cosmin at 30/04/15 21:08:43
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>Class responsible for opening a port and listening to connections and taking commands</p>
 *
 */
public class Server{
    /**
     * The logger
     */
    private static Logger logger = Logger.getLogger(Watcher.class);

    /**
     * Created by cosmin
     *
     * <p>The main entry point for this class</p>
     *
     * @return    void
     */
    public void run(){
        try {
            Thread t;
            Socket client;
            ServerSocket srv = new ServerSocket(Options.getPort());
            srv.setSoTimeout(1000);
            while (true){
                try {
                    client = srv.accept();
                }
                catch (SocketTimeoutException te){
                    client = null;
                }
                if (client != null){
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    t = new Thread(new Protocol(in, out));
                    t.start();
                }
            }
        }
        catch (IOException e){
            logger.error("Exception communicating with the client. ", e);
        }
    }
    
    
}

