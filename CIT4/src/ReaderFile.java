

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReaderFile {
	public String path;   //爬虫所爬到的url存储的位置,绝对路径
	public File[] files;
	public List<String> forwordIndex = new ArrayList<String>();
	//构造函数
	public ReaderFile(String path){
		this.path = path;
		File file = new File(path);
		if(!file.isDirectory()){
			return;
		}
		else{
			this.files = file.listFiles();
		}
	}
	
	public File getFileName(){
		File filename=null;
		long maxTime = 0 ;             //距离现在最短的时间
		for(int i = 0 ; i < files.length ; i++){
			if(files[i].isFile()){
				String name = files[i].getName();
				//提取文件名中时间的那部分
				String dateStr = name.substring(name.indexOf("me")+2,name.lastIndexOf("."));
				long time = DateStrToSec(dateStr);
				if( time > maxTime){
					maxTime = time;
					filename = files[i];
				}
			}
		}
		return filename;
	}
	
	public long DateStrToSec(String dateStr)
	{
		String sDt = null;
		if(null == dateStr) 
			return 0;
		sDt = dateStr;
		
		SimpleDateFormat sdf= new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
		Date dt2 = null;
		try {
		    dt2 = sdf.parse(sDt);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		//继续转换得到秒数的long型
		long lTime = dt2.getTime() / 1000;
		return lTime;
	}
	
	//读到前向索引内容，并放入前向索引list中
	public void readUrlFromTxt(File file){
		TestNLPIR testNlpir = new TestNLPIR();
		try {
			String url;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((url = reader.readLine()) != null){
				if(!url.contains(".chm")){
					System.out.println(url);
					URLoperate urlOperate = new URLoperate(url);
					String content = urlOperate.getContent();
					String keyword=null;
					keyword = testNlpir.getInvertedIndex(url, content);
					forwordIndex.add(keyword);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//前向索引写入文件
	public void writeToFile(File fileResult){
		boolean isadd = true;
		Iterator<String> it = forwordIndex.iterator();
		List<String> finalyIndex = new ArrayList<String>();
		
		//把要存入的前向索引与原本文件的的内容做比较，重复就去除，实际上就是一个去重功能
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileResult));
			String text;
			while(it.hasNext()){
				String keyword = it.next();
				while((text = reader.readLine()) !=null){
					String url = text.split(" ")[0].trim();
					String urlFromKeyword = keyword.split(" ")[0].trim();
					if(url.equals(urlFromKeyword)){
						isadd = false;
					}
				}	
				if(isadd){
					finalyIndex.add(keyword);
				}
			}
			reader.close();
			
			//把去重之后的前向索引存入文件
			BufferedWriter write = new BufferedWriter(new FileWriter(fileResult,true));
			Iterator<String> keyIt = finalyIndex.iterator();
			while(keyIt.hasNext()){
				write.write(keyIt.next());
				write.newLine();
			}
			write.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
