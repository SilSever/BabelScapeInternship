package it.uniroma3.internship.io.handler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.thrift.TException;

import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.thrift.Word2VecModelThrift;
import com.medallia.word2vec.util.AutoLog;
import com.medallia.word2vec.util.Common;
import com.medallia.word2vec.util.ProfilingTimer;
import com.medallia.word2vec.util.ThriftUtils;

/**
 * 
 * @author Silvio Severino
 *
 */
public class Word2VecHandler implements Handler
{
	private static final String WORD_2_VEC_MODEL = "word2Vec.model";
	private static final Log LOG = AutoLog.getLog();

	/**
	 * 
	 * Write on a model file the sensesAndGlosses model made by Word2VecModel
	 * @param Word2VecModel
	 * 
	 */
	@Override
	public void write(Object obj)
	{
		Word2VecModel model = (Word2VecModel) obj;
		
		try (ProfilingTimer timer = ProfilingTimer.create(LOG, "Writing output to file")) {
			FileUtils.writeStringToFile(new File(WORD_2_VEC_MODEL), ThriftUtils.serializeJson(model.toThrift()));
		}catch (IOException | TException e) {
			//NOPE
		}
	}
	
	/**
	 * 
	 * Read from file the word2Vec model
	 * @return the word2Vec model
	 * 
	 */
	@Override
	public Word2VecModel read()
	{
		Word2VecModel model;
		try (ProfilingTimer timer = ProfilingTimer.create(LOG, "Loading model")) {
			String json = Common.readFileToString(new File(WORD_2_VEC_MODEL));
			model = Word2VecModel.fromThrift(ThriftUtils.deserializeJson(new Word2VecModelThrift(), json));
		}catch (IOException | TException e) {
			model = null;
		}
		return model;
	}
	
	/**
	 * 
	 * @return true if word2Vec.model is empty.
	 * 		   false otherwise
	 */
	@Override
	public boolean checkIfEmpty()
	{
		File file = new File(WORD_2_VEC_MODEL);		
		return file.length() == 0;
	}

}
