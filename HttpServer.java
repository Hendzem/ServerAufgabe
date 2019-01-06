import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class HttpServer // TODO : Multithreading
{
    private int port;

    public HttpServer(int port)
    {
        this.port = port;
    }
    
    public void startServer()
    {
      try
      {
        //Zum Testen
        boolean runner = true;
        int timer = 0;
        //Server starten
        ServerSocket serverConnection = new ServerSocket(port);
        System.out.println("Server gestartet");
        //Server hört die ganze Zeit hin
        while(runner)
        {
            try
            {
                //Auf TCP Verbindung warten und als Thread hinzufügen
                Socket client = serverConnection.accept();
                ConnectionHandler c = new ConnectionHandler(client);
                c.go();
                client.close();
                if(timer == 5)
                {
                    serverConnection.close();
                    runner = false;
                }
                timer ++;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
      }
      catch(IOException e)
      {
          e.printStackTrace();
      }
    }
    
    
    
}
