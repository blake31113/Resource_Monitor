import java.net.Socket;
import java.util.*;
import java.text.*;
import java.util.concurrent.BlockingQueue;
import java.io.*;
import java.lang.Exception;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ProcessSocket extends java.lang.Thread
{
	private Socket socket;
	private String hostName;
	private BufferedInputStream in = null;
	private BufferedOutputStream out = null;
	private BlockingQueue<String> queue;
	private BlockingQueue<String> queueR;
	private BlockingQueue<String> queueI;
	Date date = new Date(); 
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmm");
	FileWriter fw = new FileWriter("log/"+dateFormat.format(date)+".log");
	public ProcessSocket(Socket s,BlockingQueue<String> queue,BlockingQueue<String> queueR,BlockingQueue<String> queueI,String hostName) throws IOException
	{	
		this.hostName=hostName;
		this.queue=queue;
		this.queueR=queueR;
		this.queueI=queueI;
		socket = s;
		

	}

	public void run()
	{
	 	try
		{
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
			//echo server
			byte[] buff = new byte[30000];
			int length = 0;
			//read message from sniffer
			while(true)
			{
				if((length = in.read(buff)) <= 0)
				{
					continue;
				}
				//read string
				String msg = new String(buff,0,length);
				System.out.println("Thread"+Thread.currentThread().getId()+" : "+msg);
				String[] cmds = msg.split(""+'\n');
				for(String string:cmds)
				{
					
					analysiscmd(string);
				}
					
				
				if(msg.indexOf(0)=='z')
				{
					System.out.println("get z");
					break;
				}
					
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("error point : 1-2-1");
		}
		catch(Exception e)
		{
			System.out.println("error point : 1-2-2");
		}
		finally
		{
			System.out.println("Thread"+Thread.currentThread().getId()+" : socket will close");
			try
			{
				in.close();
				out.close();
				socket.close();
				fw.close();
			}
			catch(IOException e)
			{
				System.out.println("error point : 1-2-3");
			}
		}
	}

	private void analysiscmd(String msg) throws IOException {
		if(msg.isEmpty())
			return;
		try{
			String[] cmdarg=msg.split("\\|");
			
			if(cmdarg[0].equals("F"))
			{
				//controller problem
				sshCommand(cmdarg[1],cmdarg[2],cmdarg[3]);
				
			}
			else 
			{
				queue.put(msg);
				queueR.put(msg);
				queueI.put(msg);
				fw.write(msg+'\n');
			}
			//echo 
			out.write(msg.getBytes());
			out.flush();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.out.println("error point : 1-2-1");
			}
			catch(Exception e)
			{
				System.out.println("error point : 1-2-2");
			}
	}

	private void sshCommand(String port, String host, String handover) throws Exception {
		//ssh handover
		JSch jsch = new JSch();
	    //System.out.println("Getting session...");
	    int Port=Integer.parseInt(port);
	    String userName = "root";
	    String password = "5796857968";
	    String cmd = "ovs-vsctl set-controller br-int "+handover;
	    Session session = jsch.getSession(userName, host, Port);
	    //System.out.println("session " + session.getUserName() + "@" + session.getHost() + " established");
	    session.setPassword(password);
	    Properties config = new java.util.Properties();
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
	    session.connect(40000);
	    Channel channel = session.openChannel("exec");
	    ((ChannelExec) channel).setCommand(cmd);
	    channel.connect();
	    channel.run();
	   
	    channel.disconnect();
	    session.disconnect();
	}
	
	
}