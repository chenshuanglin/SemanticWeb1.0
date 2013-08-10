package action;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Urloperation {
	private String url;
	private Document doc;
	private String keyword;
	int COUNT=70;   //控制截取正文的个数
	int SPLITCOUNT = 10;
	public Urloperation(String url,String keyword){
		this.url = url;
		this.keyword = keyword;
		try {
			doc = Jsoup.connect(url).timeout(10000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getTille()
	{
		String title = null;
		return doc.title();
	}
	
/*
 * 对一段正文，根据关键字进行截取
 * 
 * */
	public String getContentByKeyword(String keyword){
		String content = null;
		content = getContent();
		int length = content.length();
		int keywordIndex = content.indexOf(keyword);
		int begin,end;
		if(keywordIndex < COUNT )
		{
			begin = 0 ;
		}else{
			begin = keywordIndex-COUNT;
		}
		if(keywordIndex+COUNT>length)
		{
			end = length;
		}
		else{
			end = keywordIndex+COUNT;
		}
		content = content.substring(begin, end);
		int length1;
		String[] text = content.split(" ");
		
		content+="...";
		return content;
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
		int max = 0;
		for(int i = 0 ; i < text.length; i++){
			int length = text[i].length();
			if(length > SPLITCOUNT){
				buf.append(text[i]+" ");
			}
		}
		content = buf.toString();
		return content;
	}
	
	/*
	 * 把正文的标题，url，还有关键字包含的内容一并存入PageContext对象中
	 * */
	public PageContext getAllMessage(){
		PageContext pageContent = new PageContext();
		pageContent.setTitle(getTille());
		pageContent.setUrl(url);
		pageContent.setContent(getContentByKeyword(keyword));
		return pageContent;
	}
}
