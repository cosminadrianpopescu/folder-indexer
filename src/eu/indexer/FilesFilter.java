package eu.indexer;

import java.io.File;
import java.util.List;

/**
 * Created by cosmin at 01/05/15 21:04:40
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>Class used for filtering the files for lucene</p>
 *
 */
public class FilesFilter{
    /**
     * The list of extensions to be filtered
     */
    private String pattern;
    
    /**
     * Created by cosmin
     *
     * <p>Implements the accept method</p>
     *
     * @param    dir File The dir
     * @param    fileName String The file name
     *
     * @return    boolean True if the file should be included
     */
    public boolean goodExt(File f){
        return f.getName().matches(this.pattern);
    }

    /**
     * Created by cosmin
     *
     * <p>Sets the list of extensions</p>
     *
     * @param    extensions List<String> The list of extensions
     *
     * @return    void 
     */
    public void setExtensions(List<String> extensions){
        String list = "";
        for (String s: extensions){
            list += (list.equals("") ? "" : "|") + s;
        }

        this.pattern = "^.*\\.(" + list + ")$";
    }
    
    
}

