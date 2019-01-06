public class HServer
{
    public static void main(String[] args)
    {
        //Port zum Testen im Browser einfach : "localhost:5050" eingeben
        // wwwroot sollte im gleichen Ordner wie die Java Datei liegen
        int user = 5050;
        
        HttpServer h = new HttpServer(user);
        h.startServer();
        System.out.println("done");
        System.exit(1);
        
    }
}