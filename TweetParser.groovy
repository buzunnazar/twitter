package ru.ispras.modis.sentanal.preprocessors


import groovy.json.JsonSlurper
import ru.ispras.*
import ru.ispras.modis.sentanal.ngramcollection.NGramExtractor;

/**
 * Created with IntelliJ IDEA.
 * User: valerij
 * Date: 4/28/12
 * Time: 4:32 PM
 */
class TweetParser {

    public static void main(args){
        def path =   "/home/valerij/git/twitter/data/testTweets/all"
        def pathOut = "/home/valerij/git/twitter/data/testTweets/noTrash"
        def file = new File(path);
        def out = new File(pathOut);
        out.delete();
        def prep = new ru.ispras.modis.sentanal.preprocessors.TrashRemovingPreprocessor()
        file.eachLine {
            def parcer = new JsonSlurper();
            def res = parcer.parseText(it);
            res.each {
                out.append(prep.process(it.text))
            }
            out.append('\n');
        }

    }
}
