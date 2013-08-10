

import java.io.BufferedReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class URLoperate {
	private String url;
	private Document doc;
	int COUNT=10;
	public  URLoperate(String url){
		this.url = url;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 在获取征文内容的时候，认为如果某句话的长度小于多少就抛弃，不放入正文
	 * 而标题必须放入正文
	 * */
	public String getContent(){
		String content = null;
		content = doc.body().text();
		String title = doc.title();
		String[] text = content.split(" ");
		StringBuffer buf = new StringBuffer();
		buf.append(title);
		int max = 0;
		for(int i = 0 ; i < text.length; i++){
			int length = text[i].length();
			if(length > COUNT){
				buf.append(text[i]+" ");
			}
		}
		content = buf.toString();
		return content;
	}
	
	
}
