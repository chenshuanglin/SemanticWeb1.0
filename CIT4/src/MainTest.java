import java.io.File;
import java.io.IOException;


public class MainTest {
	/*
	 * 以下路径都是绝对路径，如果是windows系统，还要更改NLPIR.java函数里面的最后几行中的
	 * System.load(path+"/libNLPIR_JNI.so");
	 * 更改为类似为
	 * System.load(path+"/NLPIR_JNI.dll");
	 * */
	public static void main(String[] args) {
		ReaderFile readFile = new ReaderFile("/home/lin/语义web/html_to_txt"); //爬虫爬到的文件存储的目录，自己的程序就自己更改这个地方
		File file =readFile.getFileName(); 
		readFile.readUrlFromTxt(file);
		File file1 = new File("/home/lin/test_html/keyword.txt");  //前向索引存储的文件
		//如果没有该前向向索引文件就创建一个新的文件
		if(!file1.exists()){
			try {
					file1.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}    
			}
		readFile.writeToFile(file1);
	
		File file2 = new File("/home/lin/test_html/INVERTED_INDEX.txt"); //后向索引存储的文件
	 //如果没有该逆向索引文件就创建一个新的文件
		if(!file2.exists()){
			try {
				file2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}    
		}
		ReverseIndex.toInvertedIndex(file1,file2);
		System.out.println("jieshu");
	}
}
