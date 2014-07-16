import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class main {

	public static void main(String[] args) 
	{	
		// TODO Auto-generated method stub
		ConfigReader configreader=new ConfigReader();
		Conf conf=configreader.getConf();
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(2048);
		BlockingQueue<String> queueR = new ArrayBlockingQueue<String>(2048);//queue for Rspec Server
		BlockingQueue<String> queueI = new ArrayBlockingQueue<String>(2048);//queue for IMC
		
		Thread serverSocketThread = new Socket_S(queue,queueR,queueI,conf.hostName);
		Thread WSclientSocketThread = new Socket_C(conf.web_ip, conf.port_webServer,queueR);
		Thread IMCclientSocketThread = new Socket_C(conf.ip_IMC, conf.port_IMC,queueI);
		
		serverSocketThread.start();
		WSclientSocketThread.start();
		IMCclientSocketThread.start();
	}

}
