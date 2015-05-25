package eu.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by cosmin at 30/04/15 21:49:10
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>Class responsible for the simple protocol to communicate with the server</p>
 *
 */
public class Protocol implements Runnable{
    /**
     * The in channel
     */
    private BufferedReader in;
    
    /**
     * The out channel
     */
    private PrintWriter out;
    
    /**
     * The logger
     */
    private static Logger logger = Logger.getLogger(Watcher.class);

    /**
     * If true, then finish this connection
     */
    private Boolean end = false;
    

    /**
     * Created by cosmin
     *
     * <p>The constructor</p>
     *
     * @param    in BufferedReader The in channel
     * @param    out PrintWriter The out channel
     *
     * @return    void 
     */
    public Protocol(BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
    }

    /**
     * Created by cosmin
     *
     * <p>Sends the results back to the user</p>
     *
     * @param    results List<String> The results
     *
     * @return    void 
     */
    private void sendResults(List<String> results){
        for (String f: results){
            out.println(f);
        }
    }

    /**
     * Created by cosmin
     *
     * <p>Send a string to the user if the user is still active</p>
     *
     * @param    s String The string
     *
     * @return    void 
     */
    private void sendToUser(String s){
        try {
            out.println(s);
        }
        catch (Exception e){
        }
    }
    
    

    /**
     * Created by cosmin
     *
     * <p>Sends a query to lucene and fetches the result</p>
     *
     * @param    q String The query
     *
     * @return    void 
     */
    private void processCommand(String q){
        String queryTypes = "";
        for (LuceneQueryType v: LuceneQueryType.values()){
            queryTypes += (queryTypes.equals("") ? "" : "|") + v.toString();
        }
        String pattern1 = "^\\.query-(" + queryTypes + ") (.*)$";
        String pattern2 = "^\\.(index|delete) (.*)$";
        String pattern3 = "^\\.query-(" + queryTypes + ") -folder=(.*) -query=(.*)$";
        String pattern4 = "^\\.file-(" + queryTypes + ") (.*)";
        if (q.matches(pattern3)){
            LuceneQueryType type = LuceneQueryType.valueOf(q.replaceAll(pattern3, "$1"));
            String query = q.replaceAll(pattern3, "$3");
            String folder = q.replaceAll(pattern3, "$2");
            this.sendResults(Lucene.query(query, type, folder));
        }
        else if (q.equals(".index-all")){
            this.sendToUser("Indexing... Please wait. ");
            Lucene.indexAll();
            this.sendToUser("Index finished.");
        }
        else if (q.matches(pattern1)) {
            String query = q.replaceAll(pattern1, "$2");
            LuceneQueryType type = LuceneQueryType.valueOf(q.replaceAll(pattern1, "$1"));
            this.sendResults(Lucene.query(query, type, null));
        }
        else if (q.matches(pattern2)){
            String operation = q.replaceAll(pattern2, "$1");
            String file = q.replaceAll(pattern2, "$2");

            if (operation.equals("index")){
                Lucene.indexAndCommit(new File(file));
            }
            else if (operation.equals("delete")){
                Lucene.deleteAndCommit(new File(file));
            }
        }
        else if (q.matches(pattern4)){
            String file = q.replaceAll(pattern4, "$2");
            LuceneQueryType type = LuceneQueryType.valueOf(q.replaceAll(pattern4, "$1"));
            this.sendResults(Lucene.searchFile(file, type));
        }
        else if (q.equals(".")){
            this.end = true;
        }
        else {
            this.sendToUser("You typed " + q);
        }
    }
    
    

    /**
     * Created by cosmin
     *
     * <p>The main thread function</p>
     *
     * @return    void 
     */
    public void run(){
        String line;

        try {
            while ((line = in.readLine()) != null){
                this.processCommand(line);
                if (this.end){
                    break;
                }
                // this.sendToUser(".");
            }
            in.close();
            out.close();
        }
        catch (IOException e){
            logger.error("There has been a problem communicating with the client", e);
        }
    }
}

