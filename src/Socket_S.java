import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.io.*;

public class Socket_S extends java.lang.Thread
{
	private boolean outServer 		= false;
	private ServerSocket server 	= null;
	private final int serverPort 	= 9001;
	private BlockingQueue<String> queue;
	private BlockingQueue<String> queueR;
	private BlockingQueue<String> queueI;
	private String hostName;
	
	public Socket_S(BlockingQueue<String> queue,BlockingQueue<String> queueR,BlockingQueue<String> queueI,String hostName)
	{
		this.queue=queue;
		this.queueR=queueR;
		this.queueI=queueI;
		this.hostName=hostName;
		try
		{
			server = new ServerSocket(serverPort);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Server socket error!");
			System.out.println("error point : 1-1-1");
		}
	}

	public void run()
	{
		System.out.println("Server start listen : "+serverPort);
		while(!outServer)
		{
			Socket socket = null;
			try
			{
				synchronized (server)
				{
					socket = server.accept();
				}
				socket.setSoTimeout(15000);
				System.out.println("accept a Socket");
				new ProcessSocket(socket,queue,queueR,queueI,hostName).start();
			}
			catch(IOException e)
			{
				System.out.println("get socket error");
				System.out.println("error point : 1-1-2");
				e.printStackTrace();
			}
		}
	}
}