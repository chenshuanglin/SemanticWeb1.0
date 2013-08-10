

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ReverseIndex {
	
	public static void toInvertedIndex(File file,File indexFile){
		try{
			Set set = new HashSet<String>();
			//取得所有的关键字，并利用set的去重功能
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buf = null;
			while((buf = reader.readLine())!=null){
				String[] argu = buf.split(" ");
				for(int i=1; i < argu.length;i++){
					System.out.println(argu[i]);
					set.add(argu[i]);   //把所有的关键字存入set集合中
				}
			}
			reader.close();
			
			//每迭代出一个数值，就查找整个文件，构造逆向索引文件
		
			//用BufferedWrite方法写文件，其中new FileWriter(indexFile,true)中的true参数是运行添加读写而不覆盖与来的内容
			BufferedWriter write = new BufferedWriter(new FileWriter(indexFile,true));
			String text = null;
			Iterator<String> it = set.iterator(); //迭代出每一个关键字，之后去寻找每条记录，如果有一条记录含有该关键字.就把信息添加到strbuf中
			while(it.hasNext()){
				BufferedReader readerTwo = new BufferedReader(new FileReader(file)); //读file文件
				String keyword = it.next();
				StringBuffer strbuf = new StringBuffer(keyword+" ");//每条记录的格式{keyword word1 word2 word3}
				while((text=readerTwo.readLine())!=null){
					String[] arg = text.split(" ");   //获取的每条记录，分开成一个字符串数组
					for(int i=1; i < arg.length;i++){
						if(arg[i].equals(keyword)){  //判断是否含有该关键字，有则把地址加入strbuf中
							strbuf.append(arg[0]+" ");  
						}
					}
				}
				System.out.println(strbuf.toString());
				write.write(strbuf.toString()); //写一条记录到文件中
				write.newLine(); //文件中的数据另起一行
			}
			write.close();
		}catch(Exception e){}
	
	}
	
}
