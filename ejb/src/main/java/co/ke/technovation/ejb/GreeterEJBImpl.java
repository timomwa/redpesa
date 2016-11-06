package co.ke.technovation.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(GreeterEJBI.class)
public class GreeterEJBImpl implements GreeterEJBI {

	@Override
	public String sayHello(String name){
		return "Hello "+name;
	}
}
