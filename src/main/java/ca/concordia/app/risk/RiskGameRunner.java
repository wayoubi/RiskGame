package ca.concordia.app.risk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 * @author i857625
 *
 */
@SpringBootApplication
public class RiskGameRunner {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		SpringApplicationBuilder builder = new SpringApplicationBuilder(RiskGameRunner.class);
		builder.headless(false);
		builder.run(args);
	}
}