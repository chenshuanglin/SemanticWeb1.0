

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParserTool {
	// 获取一个网站上的链接,filter 用来过滤链接
	public static Set<String> extracLinks(String url, LinkFilter filter) {

		Set<String> links = new HashSet<String>();
		Parser parser = null;
		NodeList list = null;
		 

		NodeFilter frameFilter = new NodeFilter() {
			public boolean accept(Node node) {
				if (node.getText().startsWith("frame src="))// || node.getText().startsWith("a href=")) 
				{
					return true;
				} else {
					return false;
				}
			}
		};
		// OrFilter 来设置过滤 <a> 标签，和 <frame> 标签
		OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
		int loopCharSet = 0; // 简单解决乱码问题控制
		try {
		    parser = new Parser(url);
			//parser.setEncoding("gb2312");
		    parser.setEncoding("UTF-8");
		} catch (ParserException e2) {
		    // TODO Auto-generated catch block
		    e2.printStackTrace();
		}
		
		int i = 0;
		//reset:
		for(;;) {
		    try {
			// 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接

			// 得到所有经过过滤的标签
			list = parser.extractAllNodesThatMatch(linkFilter);

                    	for (; i < list.size(); i++) { //
                    
                    	    Node tag = list.elementAt(i);
                    	    if (tag instanceof LinkTag)// <a> 标签
                    	    {
                    		
                    		LinkTag link = (LinkTag) tag;
                    		String linkUrl = link.getLink();// url
                    		
                    		if (filter.accept(linkUrl)) {
                    		    links.add(linkUrl);
       //System.out.println("标签添加");
                    		}
                    		    
                    	    } else// <frame> 标签
                    	    {
                    		// 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
                    		String frame = tag.getText();
                    		int start = frame.indexOf("src=");
                    		frame = frame.substring(start);
                    		int end = frame.indexOf(" ");
                    		if (end == -1)
                    		    end = frame.indexOf(">");
                    		String frameUrl = frame.substring(5, end - 1);
                    		if (filter.accept(frameUrl))
                    		    links.add(frameUrl);
                    	    }
                    
                    	}
		    } catch (ParserException e) {
		    	//e.printStackTrace();
			//System.out.println("link_i=" + i + " charSet:" + loopCharSet);
		    	parser.reset();
		    	loopCharSet = (loopCharSet + 1) % 4;
		    	System.out.println("link_i=" + i + " charSet:" + loopCharSet);
//		    	try {
//		    	    switch(loopCharSet) 
//		    	    {
//		    	    case 0:parser.setEncoding("UTF-8");
//		    	    	break;
//		    	    case 1:parser.setEncoding("gb2312");
//		    	    	break;
//		    	    case 2:parser.setEncoding("ISO-8859-1");
//		    	    	break;
//		    	    case 3:parser.setEncoding("GBK");
//		    	    	break;
//		    	    }
//		    	
//			} catch (ParserException e1) {
//			    
//			    e1.printStackTrace();
//			}
		    	continue;
		    }	
		    System.out.println("Break of get all links: in the page foundLinkSize=" + links.size());
		    break;
		}
		return links;
	}
}