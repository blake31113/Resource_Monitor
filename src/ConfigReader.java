import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

public class ConfigReader 
{
	private static final String FILEPATH_STRING = "monitor.conf"; //configuration file name
	private Conf conf;
	private void parserConigFile(String name, String value) 
	{
		
		switch (name.toLowerCase()) 
		{
			
			case "web_ip":
				conf.web_ip = value;
			break;
			case "ip_imc":
				conf.ip_IMC = value;
				break;
			case "host_ip":
				conf.host_ip = value;
				break;
			case "port_webserver":
				conf.port_webServer = Integer.parseInt(value);
				break;
			case "port_imc":
				conf.port_IMC = Integer.parseInt(value);
				break;
			case "hostname":
				conf.hostName = value;
				break;
			default:
				break;
		}
	}
	
	public ConfigReader()
	{
		conf = new Conf();
	}
	
	
	public Conf getConf()
	{
		File configFile = new File(FILEPATH_STRING);
		try 
		{
			
			BufferedReader reader= new BufferedReader(new FileReader(configFile));
			String line = null;
			while((line = reader.readLine()) !=null)
			{
				try 
				{
					String non_comment = line.replaceAll(" ", "").split("#")[0];
					if(non_comment.length()==0)
						continue;
					parserConigFile(non_comment.split("=")[0],non_comment.split("=")[1]);
				} 
				catch (ArrayIndexOutOfBoundsException e) 
				{
					
					e.printStackTrace();
				}
				catch (PatternSyntaxException e) 
				{
					
					e.printStackTrace();
				}
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		
		return conf;
	}



	
	
}