package OWLTest;

import java.util.Iterator;
import java.util.Scanner;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.impl.OntologyImpl;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ListClassAndIndividual {
	private String owlFilePath;                 //owl文件的绝对路径名
	private String keyWord;                     //要查找的概念
	public ListClassAndIndividual(String owlFilePath,String keyWord){
		this.owlFilePath = owlFilePath;
		this.keyWord = keyWord;
	}
	public OWLMessage listMessage(){
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontModel.read("file:"+owlFilePath);  //读取当前路径下的文件，加载模型
		OWLMessage owlMessage = new OWLMessage();
		//以下迭代出的结果都存入owlMessage的文件中
		for(Iterator it = ontModel.listClasses();it.hasNext();){
			OntClass ontClass = (OntClass)it.next();
			if(ontClass.getLocalName() != null && ontClass.getLocalName().equals(keyWord)){   //判断是否是同一个类
				
				//以下是把该概念的父类存入owlMessage中的superClass中
				StringBuffer superBuf = new StringBuffer();
				for(Iterator i=ontClass.listSuperClasses();i.hasNext();){
					OntClass superClass = (OntClass)i.next();
					String superClassUri = superClass.getURI();
					String superName = superClassUri.substring(superClassUri.indexOf("#")+1);
					System.out.println(superName);
					superBuf.append(superName+" ");
				}
				owlMessage.setSuperClass(superBuf.toString());
				//以下是把概念的子类存入
				StringBuffer subBuf = new StringBuffer();
				for(Iterator i=ontClass.listSubClasses();i.hasNext();){
					OntClass subClass = (OntClass)i.next();
					String subClassUri = subClass.getURI();
					String subName = subClassUri.substring(subClassUri.indexOf("#")+1);
					System.out.println(subName);
					subBuf.append(subName+" ");
				}
				owlMessage.setSubClass(subBuf.toString());
				//以下是把同概念的东西存入，这个同概念的东西也是当作关键字进行查找的
				StringBuffer equailBuf = new StringBuffer();
				for(Iterator i=ontClass.listEquivalentClasses();i.hasNext();){
					OntClass equilClass=(OntClass)i.next();
					String equilClassUri = equilClass.getURI();
					String equilName = equilClassUri.substring(equilClassUri.indexOf("#")+1);
					equailBuf.append(equilName+" ");
				}
				owlMessage.setEquivalentClass(equailBuf.toString());
				//以下是把实例存入
				System.out.println("The individual is: ");
				StringBuffer instanceBuf = new StringBuffer();
				for(Iterator i=ontClass.listInstances();i.hasNext();){
					Individual individual = (Individual)i.next();				
					instanceBuf.append(individual.getLocalName()+" ");
				}
				owlMessage.setIndividual(instanceBuf.toString());
				
				//迭代显示当前类相关的所有属性
				System.out.println("The all property is: ");
				StringBuffer propertyBuf = new StringBuffer();
				for(Iterator ipp = ontClass.listDeclaredProperties(); ipp.hasNext();){
					OntProperty p = (OntProperty)ipp.next();
					propertyBuf.append(p.getLocalName()+" ");
				}
				owlMessage.setProperties(propertyBuf.toString());
			}
		}
		return owlMessage;
	}
	
}
