package eu.indexer;

import gnu.getopt.Getopt;

import java.io.IOException;

import javax.swing.text.html.Option;

/**
 * Created by cosmin at 30/04/15 18:23:21
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>The main class</p>
 *
 */
public class App {
    /**
     * Created by cosmin
     *
     * <p>The help function</p>
     *
     * @param   opt Getopt The program options
     *
     * @return    void 
     */
    public static void usage(){
        System.out.println("Usage: java -jar folder-indexer [OPTIONS]");
        System.out.println("\t-p\tThe port on which the server will listen for requests.");
        System.out.println("\t-e\tThe list of file extensions to index.");
        System.out.println("\t-d\tThe folder to index.");
        System.out.println("\t-m\tThe maximum number of results to display from a query (default 5000).");
        System.out.println("\t-w\t(boolean) If set, then disable the folder watcher (default enabled).");
        System.exit(0);
    }
    
    public static void main(String[] args) throws IOException {
        int c;
        String v;
        Getopt opt = new Getopt("folder-indexer", args, "p:e:d:w");
        while ((c = opt.getopt()) != -1) {
            v = opt.getOptarg();
            if (v == null && c != 'w'){
                usage();
            }
            else {
                if (c == 'p'){
                    Options.setPort(v);
                }
                else if (c == 'e'){
                    Options.setExtensions(v);
                }
                else if (c == 'd'){
                    Options.setFolder(v);
                }
                else if (c == 'm'){
                    Options.setMaxResults(v);
                }
                else if (c == 'w'){
                    Options.setEnableWatcher();
                }
                else {
                    usage();
                }
            }
        }

        if (Options.getExtensions() == null || Options.getFolder() == null){
            usage();
        }

        Lucene.open();

        if (Options.getEnableWatcher()){
            Thread t = new Thread(new Watcher(Options.getFolder(), true));
            t.start();
        }

        Server s = new Server();
        s.run();

        Lucene.close();
    }
}

