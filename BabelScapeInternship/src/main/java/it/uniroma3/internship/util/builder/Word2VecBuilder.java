package it.uniroma3.internship.util.builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.Word2VecTrainerBuilder.TrainingProgressListener;
import com.medallia.word2vec.neuralnetwork.NeuralNetworkType;
import com.medallia.word2vec.util.Common;
import com.medallia.word2vec.util.Format;

import it.uniroma3.internship.io.handler.Word2VecHandler;

/**
 * 
 * @author Silvio Severino
 *
 */
public class Word2VecBuilder
{
	public Word2VecModel buildWord2VecModel()	{
		Word2VecHandler handler = new Word2VecHandler();
		if(handler.checkIfEmpty())
		{
//			GlossAndSensesBuilder builder = new GlossAndSensesBuilder();
//			List<String> sensesAndGlossesList = builder.getSensesAndGlosses();
			
			File f = new File("text8");
			List<String> read = null;
			try
			{
				read = Common.readToList(f);
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			List<List<String>> partitioned = Lists.transform(read, new Function<String, List<String>>() {
				@Override
				public List<String> apply(String input) {
					return Arrays.asList(input.split(" "));
				}
			});

			Word2VecModel model;
			try
			{
				model = Word2VecModel.trainer()
						.setMinVocabFrequency(50)
						.useNumThreads(25)
						.setWindowSize(1)
						.type(NeuralNetworkType.CBOW)
						.setLayerSize(14)//200
						.useNegativeSamples(1)//25
						.setDownSamplingRate(1e-2)//1e-4
						.setNumIterations(1)//5
						.setListener(new TrainingProgressListener() {
							@Override public void update(Stage stage, double progress) {
								System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100));
							}
						})
						.train(partitioned);
			} catch (InterruptedException e)
			{
				model = null;
				e.printStackTrace();
			}
			
			handler.write(model);
			return model;
		}
		else
			return handler.read();
	}
}
