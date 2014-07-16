import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
public class Socket_C extends Thread
{
	private InetSocketAddress	isa;
	private BlockingQueue<String> queue;
	private int port;
	public Socket_C(String ip,int port,BlockingQueue<String> queue)
	{
		//Creater
		isa = new InetSocketAddress(ip,port);
		this.queue = queue;
		this.port=port;
	}
	
	public void run()
    {
        //start
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        
        //Start connect
        Socket socket=null;
        try
        {
            socket = new Socket(isa.getAddress(), isa.getPort());
            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());

            while (true)
            {
                //for loop
                String temp = queue.poll();
                if (temp == null)
                {
                   // System.out.println("queue is null");
                }
                else
                {
                    temp = temp + '\n';
                    System.out.print("send to " + port + ":" + temp);
                    out.write(temp.getBytes());
                    out.flush();
                }
               
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (ConnectException e)
        {
            System.out.println("Socket connect refused, sleep 5 sec");
            try
            {
                Thread.sleep(5 * 1000);
            } 
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
        catch (SocketTimeoutException e)
        {
            System.out.println("Socket connection timeout, sleep 5 sec");
            try 
            {
                Thread.sleep(5 * 1000);
            } 
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
        catch (IOException e)
        {
            queue.clear();
            e.printStackTrace();
            System.out.println("error point : 1-12-1,port:" + port);
        }
        finally
        {
            try
            {
                if (socket != null)
                    socket.close();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
            catch (IOException e)
            {
                System.out.println("error point : 1-12-2");
            }
        }
    }

    
}
