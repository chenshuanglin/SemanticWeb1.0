package action;


import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;

import java.util.Set;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import OWLTest.GetURL;
import OWLTest.OWLMessage;
import OWLTest.ReaderOWL;


import com.opensymphony.xwork2.ActionSupport;

public class SearchAction extends ActionSupport{
	private String webAppPath;
	private String searchMessage;      //接受搜索框里面输入的内容
	private List<PageContext> list = new ArrayList<PageContext>();           //把得到结果的URL放入list中
	int count=10;
	public void setList(List<PageContext> list){
		this.list = list;
	}
	public List<PageContext> getList(){
		return this.list;
	}
	public void setSearchMessage(String searchMessage){
		this.searchMessage = searchMessage;
	}
	public String getSearchMessage(){
		return this.searchMessage;
	}
	public String execute(){
		Set<PageContext> set=new HashSet<PageContext>();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String path =this.getClass().getResource("/").getPath();
        webAppPath = path;
        TestNLPIR nlpir = new TestNLPIR();
		String splitWord = nlpir.splitWord(searchMessage);  //调用分词函数得到的分词之后的结果
		String[] argus = splitWord.split(" ");               //把分词得到的结果用空格分开，存入字符串数组中
		OWLMessage owlMessage = new OWLMessage();              //声明一个存储OWL信息的类
		ReaderOWL readerOWl = new ReaderOWL();                 //声明一个读OWL操作的类
		GetURL getURL = new GetURL();                             //声明一个从逆向索引得到关键字对应的URL
		//以下操作是针对每一个分词得到的关键字，先判断是否在本体中有一个同概念的词，如果有同概念的词
		//则当作关键字查找逆向文件，把得到的结果URL放入list集合中
		//之后把分词得到的关键字也当作查找字符串，把这次得到的结果URL放入list集合中
		for(int i=0 ;i < argus.length;i++){
			owlMessage = readerOWl.getOWLMessage(argus[i]);
			String equilName= null;
			if(owlMessage.getEquivalentClass()!=null && !((equilName = owlMessage.getEquivalentClass()).equals(""))){
				String equilUrl = getURL.getResultURL(equilName.trim());
				String[] url=equilUrl.split(" ");
				for(int j = 0 ; j < url.length;j++){
					if(count<=0){
						break;
					}
					Urloperation urlOperate = new Urloperation(url[j], equilName);
					PageContext page=urlOperate.getAllMessage();
					if(page!=null)
						set.add(page);
					count--;
				}
				
			}
			String keywordUrl = getURL.getResultURL(argus[i]);
			if(!keywordUrl.equals("") && keywordUrl!=null){
				String[] url=keywordUrl.split(" ");
				for(int j = 0 ; j < url.length;j++){
					if(count<=0){
						break;
					}
					Urloperation urlOperate = new Urloperation(url[j], argus[i]);
					PageContext page=urlOperate.getAllMessage();
					if(page!=null)
						set.add(page);
					count--;
				}
			}
			
		}
		this.list=new ArrayList<PageContext>(set);
	//	request.setAttribute("result", searchMessage);
		return SUCCESS;
	}
	
	
}
