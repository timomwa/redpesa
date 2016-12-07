package co.ke.technovation.ejb;

public interface ReprocessorEJBI {

	public int processC2B(String ip_addr, int limit);
	
	public int processB2C(String ip_addr, int limit);
	
	public int process(String ip_addr, int limit);

}
