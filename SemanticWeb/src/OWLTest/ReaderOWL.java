package OWLTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ReaderOWL {
	public ReaderOWL(){
		
	}
	//以下的操作是从已经分析好的OWL_ANAYLISIS_FILE.txt的内容读出，找到对应的关键字存放的文件的位置
	public OWLMessage getOWLMessage(String keyWord){
		OWLMessage owlMessage = new OWLMessage();
		String path =this.getClass().getResource("/").getPath();
		File file = new File(path+"/OWL_ANAYLSIS_FILE.txt");
		path = path+"/ontology";  //本体文件夹的绝对位置
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = null;
			//读取文件的每一行
			while((text = reader.readLine())!=null)
			{
				System.out.println(text);
				String[] argu = text.split(" "); //得到这一行数据，存入String 数组中
				for(int i = 1 ; i < argu.length;i++){
					String ss=new String(argu[i]);
					if(ss.equals(keyWord)){
						//把得到文件的存放位置，和关键字当作参数，构造listClass实例
						argu[0] = path+"/"+argu[0];              // 得到本体文件的绝对路径
						ListClassAndIndividual listClass = new ListClassAndIndividual(argu[0], keyWord);  
						owlMessage = listClass.listMessage();  //调用ListClassAndIndividual类中的listMessage方法得到owlMessage对象
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return owlMessage;
	}
}
