import kevin.zhang.NLPIR;

import java.util.*;
import java.io.*;

public class TestNLPIR {
	
	/*说明：运行此程序，请先在D盘下创建KeyDir文件夹,其中存有欲提取关键词的文档*/
	
	
	public static NLPIR testNLPIR;
	public String argu = "./";
	static File fileResult; //存储所有文档关键词的文件
	public HashMap<String, ArrayList<String>> fordwardIndexMap;  //前向索引记录
	/*
	 *只初始化一次关键字的存放位置 
	 * */
	static{
		try{
			fileResult = new File("/home/lin/test_html/keyword.txt");
			if (!fileResult.exists())
				fileResult.createNewFile();
		}catch(IOException e){e.printStackTrace();}
	}
	
	/*
	 * 构造函数
	 * */
	public TestNLPIR() {

		testNLPIR = new NLPIR();
		fordwardIndexMap = new HashMap<String, ArrayList<String>>();	
		//环境初始化
		this.init();
		
	}
	public void init(){
		try {
			if (testNLPIR.NLPIR_Init(argu.getBytes("UTF-8"), 1) == false) {
																		
				System.out.println("Init Fail!");
				return;
			}
			System.out.println("NLPIR_Init OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//此函数为对提供url和文档非文件直接操作提取关键词
	public String getKeyWord(String url,String text){
		StringBuffer buf = new StringBuffer();
		buf.append(url+" ");  //先把url放入
		String keyString=null;
		try {
			byte [] nativeBytes = testNLPIR.NLPIR_GetKeyWords(text.getBytes(), 100, true);
			keyString = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");   //调用函数得到关键子字符串
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strArray = keyString.split("  ");	
		for (int i = 0; i < strArray.length - 1; i++) {
			strArray[i] = strArray[i].substring(0, strArray[i].indexOf("/"));
			buf.append(strArray[i]+" "); //把每个关键字放入
		}
		return buf.toString();
	}
	
	public String getInvertedIndex(String url,String content){
		String keyword = getKeyWord(url, content);   //得到正向索引
		return keyword;
//		writeToFile(keyword);
	}
}
