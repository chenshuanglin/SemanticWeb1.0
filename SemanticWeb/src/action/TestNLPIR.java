package action;


import java.util.*;
import java.io.*;

import kevin.zhang.NLPIR;

public class TestNLPIR {
	
	/*说明：运行此程序，请先在D盘下创建KeyDir文件夹,其中存有欲提取关键词的文档*/
	
	
	public static NLPIR testNLPIR;
	public String argu = "./";
	public HashMap<String, ArrayList<String>> fordwardIndexMap;  //前向索引记录
	private String path;	
	
	/*
	 * 构造函数
	 * */
	public TestNLPIR() {
		path =this.getClass().getResource("/").getPath();
		testNLPIR = new NLPIR();
		fordwardIndexMap = new HashMap<String, ArrayList<String>>();	
		//环境初始化
		this.init();
		
	}
	public void init(){
		argu = path;
		try {
			if (testNLPIR.NLPIR_Init(argu.getBytes("utf-8"), 1) == false) {
																		
				System.out.println("Init Fail!");
				return;
			}
			System.out.println("NLPIR_Init OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	//分词
	public String splitWord(String text){
		String splitWord = null;
		StringBuffer bf=new StringBuffer();
		byte nativeBytes[];
		try {
			
			nativeBytes = testNLPIR.NLPIR_ParagraphProcess(text.getBytes("UTF-8"), 1);
			String splitString = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
			
			//biaodian.txt为存放标点符号的文件，目的对分词结果的字符串进行去除符号处理
			File file=new File(path+"/biaodian.txt");
			BufferedReader br=new BufferedReader(new FileReader(file));
			
			ArrayList<String>  biaodian = new ArrayList<String> (); //停用词后缀词性
		
			String temp=br.readLine();
			while(temp!=null){
				biaodian.add(temp);
	       	 temp=br.readLine();
	        }
			br.close();
			
		
		
			ArrayList<String> wordlist = new ArrayList<String>();
			
		    String[] array=splitString.split(" ");
		    System.out.println(array.length);
		    int j = 0;
			for (int i = 0; i < array.length; i++) {
				for (j = 0; j < biaodian.size(); j++) {
					// 过滤标点和空格
					if (array[i].indexOf(biaodian.get(j)) != -1 )//包含标点 
						break;
				}
				
				if (j == biaodian.size()) {
					if(array[i].indexOf("/")!=-1){//包含空格
						String s=array[i].substring(0, array[i].indexOf("/"));
			    		bf.append(s+" ");
					}
					else{
						
					}
				}
			}
			System.out.println("分词结果为： " + bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		splitWord = bf.toString();
		return splitWord;
	}

}
