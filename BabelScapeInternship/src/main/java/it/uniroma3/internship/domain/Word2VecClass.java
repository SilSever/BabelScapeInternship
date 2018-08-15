package it.uniroma3.internship.domain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.thrift.TException;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.medallia.word2vec.Searcher.UnknownWordException;
import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.Word2VecTrainerBuilder.TrainingProgressListener;
import com.medallia.word2vec.neuralnetwork.NeuralNetworkType;
import com.medallia.word2vec.util.Format;

/**
 * 
 * @author Silvio Severino
 *
 */
public class Word2VecClass
{
	public Word2VecModel buildModelOf(List<String> sensesAndGlossesList) throws IOException, InterruptedException, TException, UnknownWordException
	{
		List<List<String>> partitioned = Lists.transform(sensesAndGlossesList, new Function<String, List<String>>() {
			@Override
			public List<String> apply(String input) {
				return Arrays.asList(input.split(" "));
			}
		});
		
		Word2VecModel model = Word2VecModel.trainer()
				.setMinVocabFrequency(5)
				.useNumThreads(20)
				.setWindowSize(8)
				.type(NeuralNetworkType.CBOW)
				.setLayerSize(200)
				.useNegativeSamples(25)
				.setDownSamplingRate(1e-4)
				.setNumIterations(5)
				.setListener(new TrainingProgressListener() {
					@Override public void update(Stage stage, double progress) {
						System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
					}
				})
				.train(partitioned);
		
		return model;
	}
}
