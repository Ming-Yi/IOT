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

	public static final String URL = "jdbc:mysql://localhost:3306/資料庫";
    public final static String USER = "使用者帳號";
    public final static String PASSWORD = "使用者密碼";
    public final static String TABLENAME = "資料表";

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("data/Test.arff"));
		Instances training = new Instances(reader);
		reader.close();
		// 設定要分類的屬性
		training.setClassIndex(training.numAttributes() - 1);

		// 使用貝氏分類
		NaiveBayes nb = new NaiveBayes();
		
		// 開始訓練
		nb.buildClassifier(training);

		//評估
		/*Evaluation eval = new Evaluation(training);
		eval.crossValidateModel(nb, training, 10, new Random(1));
		System.out.println(eval.toSummaryString());*/
		
		//資料庫取值
		InstanceQuery query = new InstanceQuery();
		query.setUsername(USER);
		query.setPassword(PASSWORD);
		query.setDatabaseURL(URL);
		query.setQuery("select * from "+TABLENAME);
		Instances data = query.retrieveInstances();
		
		String[] options= new String[2];		
		
		//刪除屬性
		Remove remove=new Remove();
		options[0]="-R";
        options[1]="1,4";
        
        remove.setOptions(options);
		remove.setInputFormat(data);
		Instances removedata = Filter.useFilter(data, remove);
			
		//數值轉名詞
		NumericToNominal convert= new NumericToNominal();
        options[0]="-R";
        options[1]="2";

        convert.setOptions(options);
        convert.setInputFormat(removedata);
        Instances Dataset=Filter.useFilter(removedata, convert);
        
        //設定測試集要預測的分類屬性
        Dataset.setClassIndex(Dataset.numAttributes() - 1);       

		//開始分類
		double result;
		String sql;
		for(int i=0;i<Dataset.numInstances();i++){
			//測試集分類
			result = nb.classifyInstance(Dataset.instance(i));
			if(data.instance(i).value(3) != result )
			{
				//對資料庫進行修改
				sql = "UPDATE "+TABLENAME+" SET state ="+String.valueOf(result)+"WHERE id="+data.instance(i).value(0);
				query.execute(sql);
			}
		}
	}
}
