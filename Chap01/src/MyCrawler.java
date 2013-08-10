
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MyCrawler {
	/**
	 * 使用种子初始化 URL 队列
	 * 
	 * @return
	 * @param seeds
	 *            种子URL
	 */
	String startStr = null;// "http://blog.sina.com.cn/";//"http://blog.chinaunix.net/";//"http://www.baidu.com";//http://www.lietu.com";
	String startWith = null;// startStr.substring(0, startStr.lastIndexOf('/'));

	
	public static String[] initConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String[] urls = null;
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new File("src/config.xml")); // doc即根

			NodeList nl = doc.getElementsByTagName("src");
			int len = nl.getLength();
			urls = new String[len];

			for (int i = 0; i < len; i++) {
				Element eltStu = (Element) nl.item(i);

				Node eltName = eltStu.getElementsByTagName("link").item(0);

				String srcStr = eltName.getFirstChild().getNodeValue();
				urls[i] = srcStr;
				System.out.println("startLink:" + srcStr);
			}

			NodeList nodeList = doc.getElementsByTagName("page");
			Element elpc = (Element) nodeList.item(0);
			Node no = elpc.getElementsByTagName("page_count").item(0);
			String pageCount = no.getFirstChild().getNodeValue();
System.out.println("page_count = " + pageCount);
			DownLoadFile.setPage_count(Integer.parseInt(pageCount));
			
			NodeList nodeList_path = doc.getElementsByTagName("downloadFile");
			Element eleFile = (Element) nodeList_path.item(0);
			Node node = eleFile.getElementsByTagName("fileDir").item(0);
		
			String fileDir = node.getFirstChild().getNodeValue();
			DownLoadFile.setDownloadFileDir(fileDir);
System.out.println("downloadFileDir:" + fileDir);
			
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

		return urls;
	}

	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++)
			LinkQueue.addUnvisitedUrl(seeds[i]);
	}

	/**
	 * 抓取过程
	 * 
	 * @return
	 * @param seeds
	 */
	public void crawling(String[] seeds) {
		if (seeds.length <= 0)
			return;
		startStr = seeds[0];
		startWith = startStr.substring(0, startStr.lastIndexOf('/'));
		System.out.println("startWith: " + startWith);
		// 定义过滤器，提取以(starWith)开头的链接
		LinkFilter filter = new LinkFilter() {

			public boolean accept(String url) {

				if (url.startsWith(startWith))
					return true;
				else
					return false;
			}
		};
		// 初始化 URL 队列
		initCrawlerWithSeeds(seeds);
		// 准备一个文件存储 已经访问的url和对应的html文件
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		FileOutputStream fileOut = null;
		try {
			Date dt = new Date();
			SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
			String ndt = fmt.format(dt);
			String filePath = "UrlWithFileName" + ndt + ".txt";
			fileOut = new FileOutputStream(filePath, true);
			if (fileOut == null)
				return;
			try {
				osw = new OutputStreamWriter(fileOut, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw = new BufferedWriter(osw);
		} catch (FileNotFoundException e2) {

			e2.printStackTrace();
		}

		// 循环条件：待抓取的链接不空且抓取的网页不多于***
		while (!LinkQueue.unVisitedUrlsEmpty()
				&& LinkQueue.getVisitedUrlNum() <= DownLoadFile.getPage_count()) {
			// 队头URL出队列
			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();
			if (visitUrl == null || visitUrl.trim().length() == 0)
				continue;
			
			
//			DownLoadFile downLoader = new DownLoadFile();
			// 下载网页
//			String pathStr = downLoader.downloadFile(visitUrl);
//			// System.out.println("download visit:" + visitUrl);
//			// url 和 对应文件名保存起来
			//if (pathStr != null) {
				try {
					if (!visitUrl.contains("#")) {
						bw.write(visitUrl);
						bw.newLine();
						bw.flush();
					}
				} catch (UnsupportedEncodingException e) {
					System.out.println("没有指定的字符集");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.out.println("没有指定的文件");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					// throw new IOException("IOException");
				}
		//	}
			// 该 url 放入到已访问的 URL 中
			LinkQueue.addVisitedUrl(visitUrl);
			// 提取出下载网页中的 URL

			Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
			// 新的未访问的 URL 入队
			for (String link : links) {
				//if (!link.contains("#")) {
					LinkQueue.addUnvisitedUrl(link);
				//}
			}
		}

		if (bw != null) {
			try {
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (osw != null) {
			try {
				osw.close();
			fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}

	// main 方法入口
	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();

		// String[] urls = new String[]{startStr};//"http://www.lietu.com",};
		String[] urls = MyCrawler.initConfig();
		if (urls != null)
			crawler.crawling(urls);

		System.out.println("...end....");
	}

}