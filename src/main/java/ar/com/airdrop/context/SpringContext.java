package ar.com.airdrop.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

	private static ApplicationContext context;
	public static ApplicationContext getContext(){
		if (context == null){
		 context = new ClassPathXmlApplicationContext(
				"spring-beans.xml");
		}
		return context;
	}
	
}
