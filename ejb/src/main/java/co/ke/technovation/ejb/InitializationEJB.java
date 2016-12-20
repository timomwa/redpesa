package co.ke.technovation.ejb;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;
import org.jasypt.digest.PooledStringDigester;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;


@Singleton
@Startup
public class InitializationEJB {
	
	public static PooledPBEStringEncryptor db_encryptor;
	public static PooledStringDigester password_digestor;
	private static final String password_ = "_TheRestlessGeek1985!";
	private static final String ADMIN_USERNAME = "lottoadmin";

	private Logger logger = Logger.getLogger(getClass());
	
	
	@PostConstruct
	public void init(){
		initEncryptors();
	}
	
	
	public void initEncryptors(){
		
		int cores = Runtime.getRuntime().availableProcessors();
		logger.info("\n\t *** cores - "+cores+" ***");
		
		if(db_encryptor==null){
			logger.info("\n\t  *** initializing pooled encryptor  ***\n");
			db_encryptor = new PooledPBEStringEncryptor();
			db_encryptor.setPassword(password_);
			db_encryptor.setPoolSize(cores);
			HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
			registry.registerPBEStringEncryptor("myHibernateStringEncryptor", db_encryptor);
			logger.info("\n\t  *** successfully initialized pooled encryptor  ***\n");
		}
		
		if(password_digestor==null){
			logger.info("\n\t  *** initializing poled password digestor  ***\n");
			password_digestor = new PooledStringDigester();
			password_digestor.setPoolSize(cores);          
			password_digestor.setAlgorithm("SHA-1");
			password_digestor.setIterations(50000);//TODO figure out whether this affects performance
			logger.info("\n\t  *** successfully initialized pooled password digestor  ***\n");
		}
	}

}
