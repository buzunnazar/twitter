package ru.ispras.modis.sentanal

import ru.ispras.modis.sentanal.ngramcollection.NGramExtractor
import ru.ispras.modis.sentanal.ordered.RandomOrder
import opennlp.tools.tokenize.SimpleTokenizer
import cc.mallet.util.Maths;

/**
 * Created with IntelliJ IDEA.
 * User: valerij
 * Date: 4/28/12
 * Time: 5:42 PM
 */
class lda {
    public static main(args){
        def path = "/home/valerij/git/twitter/data/testTweets/noTrash"
        def file = new File(path);
        def tok = SimpleTokenizer.INSTANCE;
        def setNgrams =[];
        int length = 0;
        file.each {
            setNgrams.addAll(tok.tokenize(it))
            length++;
        }

        def order = new RandomOrder<String>(setNgrams);

        file = new File(path);

        int [][] ldaInput = new int[length][];

        def i = 0;
        file.each {
            def tokens = tok.tokenize(it);
            double [] digits = new int[tokens.length];
            def j = 0;
            tokens.each {
                digits[j] = (order.getOrderStatistic(it));
                j++;
            }
            ldaInput[i] = (digits);
            i++;
        }

        def lda = new LdaGibbsSampler(ldaInput, setNgrams.size());
        // # topics
        int K = 10;
        // good values alpha = 2, beta = .5
        double alpha = 2;
        double beta = 0.5;
        lda.configure(1000, 200, 100, 10);
        lda.gibbs(K, alpha, beta);
        double[][] theta = lda.getTheta();

        double [][] weights = new double[length][length];
        for (i = 0 ; i < length; i ++){
            for (int j =0 ; j < i; j++){
                if (i == j )
                    continue;
                weights[i][j] = 1 / Math.sqrt( Maths.jensenShannonDivergence(theta[i],theta[j]));
            }
        }
        println weights
    }
}
