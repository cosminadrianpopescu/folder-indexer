package eu.indexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 * Created by cosmin at 01/05/15 20:47:07
 *
 * @author <a href="mailto:cosminadrianpopescu@gmail.com">Cosmin Popescu</a>.
 *
 * <p>The class abstracting the lucene part of the application</p>
 *
 */
public class Lucene {
    /**
     * The file list filter
     */
    private static FilesFilter filter;

    /**
     * The lucene analyzer
     */
    private static StandardAnalyzer analyzer = new StandardAnalyzer();
    
    /**
     * The lucene index writer
     */
    private static IndexWriter writer;

    /**
     * The logger
     */
    private static Logger logger = Logger.getLogger(Watcher.class);

    /**
     * Created by cosmin
     *
     * <p>Deletes a document from the index</p>
     *
     * @param    f File The file to be deleted
     *
     * @return    void 
     */
    public static void delete(File f){
        try {
            writer.deleteDocuments(new Term("path", f.getPath()));
            logger.info("Deleted " + f.getPath());
        }
        catch (Exception e){
            logger.error("There has been a problem deleting a document", e);
        }
    }
    
    
    
    /**
     * Created by cosmin
     *
     * <p>Index a folder or a file</p>
     *
     * @param    f File The folder or file to index
     *
     * @return   void
     */
    public static void index(File f){
        logger.info("Processing " + f);
        if (f.isDirectory()){
            for (File _f: f.listFiles()){
                Lucene.index(_f);
            }
        }
        else if (Lucene.filter.goodExt(f)){
            Document doc = new Document();

            try {
                FileReader fr = new FileReader(f);
                doc.add(new TextField("contents", fr));
                doc.add(new SortedDocValuesField("path", new BytesRef(f.getPath())));
                doc.add(new StringField("path", f.getPath(), Field.Store.YES));
                doc.add(new StringField("filename", f.getName(), Field.Store.YES));

                writer.addDocument(doc);
                fr.close();
                logger.info("Added " + f);
            }
            catch (Exception e){
                logger.error("There has been a problem adding " + f.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Created by cosmin
     *
     * <p>Indexes a document and commits</p>
     *
     * @param    f File The file to index
     *
     * @return    void 
     */
    public static void indexAndCommit(File f){
        try {
            Lucene.delete(f);
            Lucene.index(f);
            Lucene.writer.commit();
        }
        catch (IOException e){
            logger.error("There has been an error commiting the transaction", e);
        }
    }
    
    /**
     * Created by cosmin
     *
     * <p>Deletes a file and commits the transaction</p>
     *
     * @param    f File The file to delete
     *
     * @return    void 
     */
    public static void deleteAndCommit(File f){
        try {
            Lucene.delete(f);
            Lucene.writer.commit();
        }
        catch (IOException e) {
            logger.error("There has been a problem deleting and commiting the file", e);
        }
    }

    /**
     * Created by cosmin
     *
     * <p>Search for a file</p>
     *
     * @param    file String The file name
     * @param    type LuceneQueryType The query type
     *
     * @return    List<String> The list of files matching the criteria
     */
    public static List<String> searchFile(String file, LuceneQueryType type){
        return Lucene.doQuery("filename", file, type, null);
    }

    /**
     * Created by cosmin
     *
     * <p>Performs a Lucene query</p>
     *
     * @param    field String The field name to be searched
     * @param    query String The query
     * @param    type LuceneQueryType The type of query
     * @param    folder String If not null, then limit the search to this folder
     *
     * @return    List<String> The list of maching files
     */
    private static List<String> doQuery(String field, String query, LuceneQueryType type, String folder){
        try {
            List<String> list = new ArrayList<String>();
            String indexDir = Options.getFolder().toFile().getAbsolutePath() + "/.idx/";
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
            IndexSearcher searcher = new IndexSearcher(reader);

            Query q = null;
            if (type == LuceneQueryType.regex) {
                q = new RegexpQuery(new Term(field, query));
            }
            else if (type == LuceneQueryType.regular){
                // QueryParser p = new QueryParser("contents", analyzer);
                // q = p.createPhraseQuery(field, query);
                // q = new WildcardQuery(new Term(field, query));
                q = new TermQuery(new Term(field, query));
            }
            else if (type == LuceneQueryType.wild){
                q = new WildcardQuery(new Term(field, query));
            }
            else if (type == LuceneQueryType.phrase){
                List<String> l = new ArrayList<String>();
                l.add("Not yet implemented");
                return l;
                // q = new PhraseQuery();
                // ((PhraseQuery) q).add(new Term(field, "reading"), 0);
                // ((PhraseQuery) q).add(new Term(field, "this"), 1);
                // ((PhraseQuery) q).setSlop(0);
                // QueryParser p = new QueryParser(field, analyzer);
                // p.setDefaultOperator(QueryParser.Operator.AND);
                // p.setPhraseSlop(0);
                // q = p.createPhraseQuery(field, query, 0);
                // q = p.parse(field + ":\"" + query + "\"");
                // QueryParser p = new QueryParser(field, analyzer);
                // q = p.parse("\"" + query + "\"");
            }
            if (q == null){
                return new ArrayList<String>();
            }
            TopDocs result = searcher.search(q, Options.getMaxResults(), new Sort(new SortField("path", SortField.Type.STRING)));
            // TopDocs result = searcher.search(q, Options.getMaxResults());
            ScoreDoc[] resultSet = result.scoreDocs;
            logger.info("Query type: " + type.toString() + ". Query: " + query + ". Hits: " + result.totalHits);
            for (int i = 0; i < resultSet.length; i++){
                Document d = searcher.doc(resultSet[i].doc);
                if (folder != null) {
                    if (!d.get("path").startsWith(folder)){
                        continue;
                    }
                }
                list.add(d.get("path"));
            }

            return list;
        }
        catch (Exception e){
            logger.error("There has been a problem searching", e);
            return new ArrayList<String>();
        }
    }
    
    

    /**
     * Created by cosmin
     *
     * <p>Executes a regular expression query</p>
     *
     * @param    query String The regexp pattern
     * @param    type LuceneQueryType The type of query
     * @param    folder String If set, the folder in which to search
     *
     * @return    return_type The result
     */
    public static List<String> query(String query, LuceneQueryType type, String folder){
        return Lucene.doQuery("contents", query, type, folder);
    }
    
    

    /**
     * Created by cosmin
     *
     * <p>Initializes the lucene indexer</p>
     *
     * @return    void 
     */
    public static void open(){
        try {
            String indexDir = Options.getFolder().toFile().getAbsolutePath() + "/.idx/";
            Lucene.filter = new FilesFilter();
            Lucene.filter.setExtensions(Options.getExtensions());
            FSDirectory dir = FSDirectory.open(Paths.get(indexDir));

            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            Lucene.writer = new IndexWriter(dir, config);
        }
        catch (IOException e){
            logger.error("There has been a problem initializing the lucene engine", e);
        }
    }

    /**
     * Created by cosmin
     *
     * <p>Closes the indexer</p>
     *
     * @return    void 
     */
    public static void close(){
        try {
            Lucene.writer.close();
        }
        catch (IOException e){
            logger.error("There has been a problem closing the lucene indexer", e);
        }
    }
    
    
    
    /**
     * Created by cosmin
     *
     * <p>Creates a new index from scratch</p>
     *
     * @param    folder Path The folder to index
     * @param    extensions List<String> The list of extensions
     *
     * @return    void 
     */
    public static void indexAll(){
        try {
            Lucene.writer.deleteAll();
            Lucene.index(Options.getFolder().toFile());
            Lucene.writer.commit();
        }
        catch (IOException e){
            logger.error("There has been a problem commiting the transaction", e);
        }
        logger.info("Finished indexing");
    }
    
    
}

