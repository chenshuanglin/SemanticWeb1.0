package OWLTest;

public class OWLMessage {
	private String superClass;                              //父类，即父概念
	private String subClass;                                //子类，即子概念
	private String equivalentClass;                         //同等概念的，有完全相同的实例
	private String individual;                              //个体，即类的实例
	private String properties;                              //类的属性
	public OWLMessage(){
	}
	public void setSuperClass(String superClass){
		this.superClass = superClass;
	}
	public String getSuperClass(){
		return this.superClass;
	}
	public void setSubClass(String subClass){
		this.subClass = subClass;
	}
	public String getSubClass(){
		return this.subClass;
	}
	public void setEquivalentClass(String equivalentClass){
		this.equivalentClass= equivalentClass;
	}
	public String getEquivalentClass(){
		return this.equivalentClass;
	}
	public void setIndividual(String individual){
		this.individual= individual;
	}
	public String getIndividual(){
		return this.individual;
	}
	public void setProperties(String properties){
		this.properties = properties;
	}
	public String getProperties(){
		return this.properties;
	}
}
