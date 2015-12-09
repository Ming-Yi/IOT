import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.Instances;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;

import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

public class bayes {

	public static final String URL = "jdbc:mysql://localhost:3306/��Ʈw";
    public final static String USER = "�ϥΪ̱b��";
    public final static String PASSWORD = "�ϥΪ̱K�X";
    public final static String TABLENAME = "��ƪ�";

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("data/Test.arff"));
		Instances training = new Instances(reader);
		reader.close();
		// �]�w�n�������ݩ�
		training.setClassIndex(training.numAttributes() - 1);

		// �ϥΨ������
		NaiveBayes nb = new NaiveBayes();
		
		// �}�l�V�m
		nb.buildClassifier(training);

		//����
		/*Evaluation eval = new Evaluation(training);
		eval.crossValidateModel(nb, training, 10, new Random(1));
		System.out.println(eval.toSummaryString());*/
		
		//��Ʈw����
		InstanceQuery query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASSWORD);
		query.setDatabaseURL(URL);
		query.setQuery("select * from "+TABLENAME);
		Instances data = query.retrieveInstances();
		
		String[] options= new String[2];		
		
		//�R���ݩ�
		Remove remove=new Remove();
		options[0]="-R";
        options[1]="1,4";
        
        remove.setOptions(options);
		remove.setInputFormat(data);
		Instances removedata = Filter.useFilter(data, remove);
			
		//�ƭ���W��
		NumericToNominal convert= new NumericToNominal();
        options[0]="-R";
        options[1]="2";

        convert.setOptions(options);
        convert.setInputFormat(removedata);
        Instances Dataset=Filter.useFilter(removedata, convert);
        
        //�]�w���ն��n�w���������ݩ�
        Dataset.setClassIndex(Dataset.numAttributes() - 1);       

		//�}�l����
		double result;
		String sql;
		for(int i=0;i<Dataset.numInstances();i++){
			//���ն�����
			result = nb.classifyInstance(Dataset.instance(i));
			if(data.instance(i).value(3) != result )
			{
				//���Ʈw�i��ק�
				sql = "UPDATE "+TABLENAME+" SET state ="+String.valueOf(result)+"WHERE id="+data.instance(i).value(0);
				query.execute(sql);
			}
		}
	}
}
