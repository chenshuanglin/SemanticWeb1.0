package OWLTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetURL {
	String path =this.getClass().getResource("/").getPath();
	File file = new File(path+"/INVERTED_INDEX.txt");
	public String getResultURL(String keyword){
		StringBuffer allURL = new StringBuffer();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text;
			while((text = reader.readLine())!=null){
				String[] argu = text.split(" ");
				if(argu[0].equals(keyword)){
					for(int i = 1 ; i < argu.length ;i++){
						allURL.append(argu[i]+" ");
					}
				}
			}
			reader.close();
		}catch(Exception e){}
		return allURL.toString();
	}
}
