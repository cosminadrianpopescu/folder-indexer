Introduction
========================================

This is a java folder indexer. It is based on Lucene 5.1.0. It indexes a
folder and then opens a port on which you can connect and do searches. 

Building
========================================

* `git clone https://github.com/cosminadrianpopescu/folder-indexer`
* `cd folder-indexer`
* `mvn package`

After this you will have a folder `folder-indexer/target` which will contain
two `jar` files: `folder-indexer-version.jar` and
`folder-indexer-version.jar-with-dependencies.jar`. The second one, contains
also all the dependencies. You can start it with `java -jar
folder-indexer-version.jar-with-dependencies.jar [OPTIONS]`. 

Starting
========================================

Command line arguments: 

* `-p`: The port on which the server will listen for connections
* `-e`: A list of comma separated extensions. All the files with this
  extension will be indexed
* `-d`: The folder to index and watch
* `-m`: Maximum number of results that a query will return
* `-w`: This option does not take any argument. 

Usage
========================================

By default, when starting the server, for every subfolder in your indexed
folder, a `folder watcher` will be added. This means that every time a file is
modified, deleted or removed in the tree, it will be automatically indexed. 

However, this comes at a price. First of all, the memory consumption is high.
Second of all, on a `*NIX` system, by default you can have only 8192 folders
watched in the system. This limit can be increased, but more memory will be
consumed. This means that you can have only 8192 subfolders in your main
folder or you will get an error. 

If you have problems because of this, you can disable the watcher, by using
the `-w` option. If you disable the watcher, then it's up to you to index the
files, remove them from the index and so on. 

Protocol
========================================

The server implements a simple protocol for querying and indexing files. All
the command begin with a dot (`.`). In order to launch a command, connect on
the server port and type the command and enter. 

*NOTE*: 

* All command have a single line (you cannot have a command on two lines or
  two command on one line)

## Adding files to the indexer

`.index <file>`

## Deleting files from the indexer

`.delete <file>`

## Searching for a file in the tree: 

* `.file-regular <file>`: performs a regular search (this means that the file has
to have a part of it's path equals with your query)
* `.file-regex <file>`: performs a regular expression search of a file name
* `.file-wild <file>`: performs a wild search (using `*`) of a files

## Searching for terms in the index: 

* `.query-regular <term>`: perform a regular search of a term. 
* `.query-regex <term>`: performs a regular expression search of a term
* `.query-wild <term>`: performs a wild search query

TODO
========================================

At the moment there is not possible to query a phrase. You can only search
for terms, which means that also you cannot search for files which contain
spaces. 
