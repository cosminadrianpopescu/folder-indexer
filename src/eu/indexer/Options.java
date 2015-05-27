package eu.indexer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cosmin at 30/04/15 18:41:13
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>The application options class</p>
 *
 */
public class Options {

    /**
     * The port on which the server will listen
     */
    private static Integer port;

    /**
     * The folder to watch
     */
    private static Path folder;

    /**
     * The list of extensions to index
     */
    private static List<String> extensions;

    /**
     * The maximum number of results
     */
    private static Integer maxResults = 5000;

    /**
     * If true, enable the watcher
     */
    private static Boolean enableWatcher = true;

    /**
     * An exclusion pattern (folders or matching this patter will not be indexed)
     */
    private static String excludePattern = null;

    /**
     * Created by popesad
     *
     * <p>Gets the excludePattern variable</p>
     *
     * @return	String
     */
    public static String getExcludePattern(){
        return Options.excludePattern;
    }

    /**
     * Created by popesad
     *
     * <p>Sets the excludePattern variable</p>
     *
     * @param	newValue List<String> The new value
     *
     * @return	void
     */
    public static void setExcludePattern(String newValue){
        Options.excludePattern = newValue;
    }

    /**
     * Created by popesad
     *
     * <p>Gets the enableWatcher variable</p>
     *
     * @return	Boolean
     */
    public static Boolean getEnableWatcher(){
        return Options.enableWatcher;
    }

    /**
     * Created by popesad
     *
     * <p>Sets the enableWatcher variable</p>
     *
     * @param	newValue Boolean The new value
     *
     * @return	void
     */
    public static void setEnableWatcher(){
        Options.enableWatcher = false;
    }

    /**
     * Created by popesad
     *
     * <p>Gets the maxResults variable</p>
     *
     * @return	Integer
     */
    public static Integer getMaxResults(){
        return Options.maxResults;
    }

    /**
     * Created by popesad
     *
     * <p>Sets the maxResults variable</p>
     *
     * @param	newValue Integer The new value
     *
     * @return	void
     */
    public static void setMaxResults(String newValue){
        Options.maxResults = Integer.parseInt(newValue);
    }

    /**
     * Created by popesad
     *
     * <p>Gets the extensions variable</p>
     *
     * @return  List<String>
     */
    public static List<String> getExtensions(){
        return Options.extensions;
    }

    /**
     * Created by popesad
     *
     * <p>The extensions to index</p>
     *
     * @param   newValue String The new value
     *
     * @return  void
     */
    public static void setExtensions(String newValue){
        Options.extensions = Arrays.asList(newValue.split(","));
    }

    /**
     * Created by popesad
     *
     * <p>Gets the folder variable</p>
     *
     * @return  Path
     */
    public static Path getFolder(){
        return Options.folder;
    }

    /**
     * Created by popesad
     *
     * <p>Sets the folder variable</p>
     *
     * @param   newValue String The new value
     *
     * @return  void
     */
    public static void setFolder(String newValue){
        Options.folder = Paths.get(newValue);
    }

    /**
     * Created by popesad
     *
     * <p>Gets the port variable</p>
     *
     * @return  Integer
     */
    public static Integer getPort(){
        return Options.port;
    }

    /**
     * Created by popesad
     *
     * <p>Sets the port variable</p>
     *
     * @param   newValue String The new value
     *
     * @return  void
     */
    public static void setPort(String newValue){
        Options.port = Integer.parseInt(newValue);
    }
    
}

