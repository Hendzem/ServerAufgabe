import java.net.*;
import java.io.*;
import java.util.*;
public class ConnectionHandler
{
    private Socket client;
    private final File WEBROOT = new File("wwwroot");
    //Übernimmt TCP Verbindung
    public ConnectionHandler(Socket client)
    {
        this.client = client;
    }
    
    public void go()
    {
        //Sachen zum Lesen und Schreiben
        BufferedReader input = null;
        String[] request = new String[4];
        try
        {
            //HTTP Request in Array Zwischenspeichern
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for(int i = 0; i < request.length; i++)
            {
                request[i] = input.readLine();
                System.out.println(request[i]); //zum Testen
            }
            //Angefragte Methode und File ermitteln
            StringTokenizer parse = new StringTokenizer(request[0]);
            String requestMethod = parse.nextToken().toUpperCase();
            String requestFile = parse.nextToken().toLowerCase();
            //System.out.println("Das ist der Pfad" + requestFile);
            //Auf angefragte Methode reagieren
            //Reaktion wird jeweils in eigener Methode implementiert
            switch(requestMethod) //TODO : HTTP 2.0 Anfragen , Alle Fälle beachtet??? :conditional Get
            {
                case "GET":     get(request, requestFile);
                                client.close();
                                break;
                
                case "HEAD":    head(request, requestFile);
                                break;
                
                case "POST":    post(request, requestFile);
                                break;
                
                default:        defalt(request);
                                break;
            }
        }
        catch(Exception e)
        {
            //TODO : Exception Handling
        }
    }
    
    private void get(String[] arr, String r)
    {
        PrintWriter output = null;
        String path = "";
        
        try
        {
            path = suche(WEBROOT,new URL(r), false);
            if(!path.equals("NOTFOUND"))
            {
                //TODO : if Angefragte ist Ordner
                BufferedOutputStream dataOut = new BufferedOutputStream(client.getOutputStream());
                File file = new File(path);
                int fileLength = (int) file.length();
                String content = getMIMEType(path);
                byte[] send = readFileData(file,fileLength);
                //Header erstellen und senden
                output = new PrintWriter(client.getOutputStream());
                output.println("HTTP/1.1 200 OK");
                output.println("Server: JAVA HTTP SERVER from UNIAUFGABE : 1.0");
                output.println("Date: " + new Date());
                output.println("Content-type: " +content);
                output.println("Content-length: " +fileLength);
                output.println();
                output.flush();
            
                dataOut.write(send,0,fileLength);
                dataOut.flush();
            
                dataOut.close();
                output.close();
            }
            else
            {
                //Header erstellen und senden
                try
                {
                    output = new PrintWriter(client.getOutputStream());
                    output.println("HTTP/1.1 404 File Not Found");
                    output.println("Server: JAVA HTTP SERVER from UNIAUFGABE : 1.0");
                    output.println("Date: " + new Date());
                    //output.println("Content-type: " +content);
                    //output.println("Content-length: " +fileLength);
                    output.println();
                    output.flush();
                    output.close();
                }
                catch(Exception e)
                {
                    //TODO
                }
            }
        }
            catch(IOException e)
        {
            //TODO
        }   
    }
    
    private void head(String[] arr, String r)
    {
        //HTTP-Header ohne Nachrichtenrumpf wird gesendet.
        try
        {
            path = suche(WEBROOT, new URL(r), false);
            if(!path.equals("NOTFOUND"))//Gesuchter Pfad wurde gefunden.
            {
                File file = new File(path);
                int fileLength = (int) file.length();
                String content = getMIMEType(path);
                byte[] send = readFileData(file, fileLength);
                //Header erstellen und senden.
                PrintWriter output = new PrintWriter(client.OutputStream());
                output.println("HTTP/1.1 200 OK");
                output.println("Server: JAVA HTTP SERVER from UNIAUFGABE : 1.0");
                output.println("Date: " + new Date());
                output.println("Content-Type: " + content);
                output.println("Content-Length: " + fileLength);
                output.println();
                output.flusch();
                output.close();
            }
            else//Pfad wurde nicht gefunden und der Header mit dem entsprechenden Status code wird gesendet.
            {
                try
                {
                    PrintWriter output = new PrintWriter(client.getOutputstream());
                    output.println("HTTP/1.1 404 File Not Found");
                    output.println("Date: " + new Date());
                    //output.println("Content-type: " +content);
                    //output.println("Content-length: " +fileLength);
                    output.println();
                    output.flush();
                    output.close();
                }
                catch(Exception e)
                {
                    //TODO
                }
            }
        }
        catch(IOException e)
        {
            //TODO
        }
    }
    
    private void cget()
    {
        //TODO
    }
    
    private void post(String[] arr, String r)
    {
        get(arr, r);
    }
    
    private void defalt(String[] arr)
    {
        //Header erstellen und senden
        try
        {
            PrintWriter output = new PrintWriter(client.getOutputStream());
            output.println("HTTP/1.1 501 Not Implemented");
            output.println("Server: JAVA HTTP SERVER from UNIAUFGABE : 1.0");
            output.println("Date: " + new Date());
            //output.println("Content-type: " +content);
            //output.println("Content-length: " +fileLength);
            output.println();
            output.flush();
            output.close();
        }
        catch(Exception e)
        {
            //TODO
        }
    }
    
    private String getMIMEType(String s)
    {
        if(s.endsWith(".html") || s.endsWith(".htm"))
        {
            return "text/html; charset=utf-8";
        }
        else
        {
            if(s.endsWith(".txt"))
            {
                return "text/plain; charset=utf-8";
            }
            else
            {
                if(s.endsWith(".css"))
                {
                    return "text/css; charset=utf-8";
                }
                else
                {
                    if(s.endsWith(".ico"))
                    {
                        return "image/x-icon";
                    }
                    else
                    {
                        if(s.endsWith(".pdf"))
                        {
                            return "application/pdf";
                        }
                        else
                        {
                            return "application/octet-stream";
                        }
                    }
                }
            }
        }
    }
    //File zum Senden vorbereiten
    private byte[] readFileData(File file, int FileLength)
    {
        FileInputStream in = null;
        byte[] back = new byte[FileLength];
        
        try
        {
            in = new FileInputStream(file);
            in.read(back);
            in.close();
        }
        
        catch(Exception e)
        {
            //TODO
        }
        
        return back;
    }
    
    private String suche(File f, URL request, boolean end)
    {
        if(end == true)
        {
            return f.getPath();
        }
        else
        {
            if(request.hasNext(request.getPosition()))
            {
                File[] arr = f.listFiles();
                StringTokenizer check = null;
            
                for(File fi: arr)
                {
                    check = new StringTokenizer(fi.getName(),".");
                    if(fi.getName().equals(request.getWord(request.getPosition())) || check.nextToken().equals(request.getWord(request.getPosition())))
                    {
                        if(fi.isDirectory())
                        {   
                            request.setPosition(request.getPosition() + 1);
                            return suche(fi, request, end);
                            
                        }
                        else
                        {
                            return suche(fi , request, true);
                            
                        }
                    }
                }
                return "NOTFOUND";
            }
            else
            {
                return "NOTFOUND";
            }
        }
        
    }
}
