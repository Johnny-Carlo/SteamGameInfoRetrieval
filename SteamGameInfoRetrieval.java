import java.io.*;
import java.util.*;
import java.net.*;

// import com.google.gson.*;
import org.json.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
//import org.jsoup.parser.*;
//import org.jsoup.select.*;


public class SteamGameInfoRetrieval
{

   public static int gameCount;
   public static List<SteamGame> gameList = new ArrayList<SteamGame>();
   
   public static void main(String [] args) throws IOException, JSONException
   {
      // System.out.print("Working: \n");
      String complete_url = prepareURL(args[1], args[2]);
      
      URLConnection connection = new URL(complete_url).openConnection();
      InputStream response = connection.getInputStream();
      String out = convertStreamToString(response);
      response.close();
      
      writeOut(out, "json.txt");
      
      parseJSONToList(out);
      takeNames();
      // System.out.print(out);
      String last = getEverything();
      writeOut(last, args[0]);
   }
   /**/
   
   public static String prepareURL(String id, String key)
   {
      String url = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/";
      // URL of the desired service

      String param1 = key;
      // Our (current) Key for Steam

      String param2 = id;
      // The ID of the subject in question.

      String param3 = "json";
      // The desired format of the response.

      // URL format for the information needed:
      // http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=XXXXXXXXXXXXXXXXX&steamid=76561197960434622&format=json

      String query = "key=" + param1 + "&steamid=" + param2 + "&format=" + param3;
      // The customized end of the URL
      
      System.out.print(url + "?" + query + "\n");

      String complete_url = url + "?" + query;
      // The completed URL
      return complete_url;
   }
   
   public static String convertStreamToString(InputStream is)
   {//just as the name implies, convert the InputStream to a String
      Scanner read = new Scanner(is, "UTF-8").useDelimiter("\\A");
      // Set up the scanner so that the next token is 
      // the entirety of the input stream
      if(read.hasNext())
      {
         return read.next();
      }// and either return the resultant string to the calling method...
      else
      {
         return "";
      }// ...or return the empty string if the input stream is empty.
   }

   /*
   static String convertStreamToString(java.io.InputStream is) 
   {
      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }
   I learned this trick from "Stupid Scanner tricks" article. 
   The reason it works is because Scanner iterates over tokens in the stream, 
   and in this case we separate tokens using "beginning of the input boundary" 
   (\A) thus giving us only one token for the entire contents of the stream.

   Note, if you need to be specific about the input stream's encoding, 
   you can provide the second argument to Scanner constructor that indicates what 
   charset to use (e.g. "UTF-8").
   
   [Text copied from Stack Overflow]
   */
   
   public static void writeOut(String in, String file)
   {// write the String to a text file in case a review of the info is in order
      File outFile = new File(file);
      try
      {
         PrintStream out = new PrintStream(outFile);
         out.print(in);
         out.close();
      }
      catch(Exception FileNotFoundException)
      {
         System.out.print("File not found.");
      }
   }
   
   public static void parseJSONToList(String in) throws JSONException
   {
      JSONObject obj = new JSONObject(in);
      
      JSONArray arr = obj.getJSONObject("response").getJSONArray("games");

      for(int i = 0; i < arr.length(); i++)
      {
         SteamGame thing = new SteamGame( 
            arr.getJSONObject(i).getInt( "appid" ), 
            arr.getJSONObject(i).getInt( "playtime_forever" ) );
            // construct a new steam game object...
            
         gameList.add(thing);
         // ...and add it to the list
      }
      //for(int i = 0; i < gameList.size(); i++)
      //{
         // System.out.print( "appid = " + gameList.get(i).getAppid() + "\n" );
         // System.out.print( "playtime = " + gameList.get(i).getPlaytime() + " minutes\n\n" );
      //}// print out loop to make sure all the games were added properly
   }
   
   public static void takeNames() throws IOException
   {
      String url;
      System.out.print("\nAnd now for the games:\n");
      
      for(int i = 0; i < gameList.size(); i++)
      {
         try
         {
            Thread.sleep(500);
         }
         catch(InterruptedException e)
         {
         
         }
         System.out.print(i + "...\n");
         url = "http://store.steampowered.com/app/" + gameList.get(i).getAppid();
         
         Document doc = Jsoup.connect(url).get();
         
         // Element thing = doc.select("div.apphub_AppName").first();
         // the title as it appears on the corresponding steam page
         
         String title = doc.title();
         if( title.contains("Welcome to Steam") )
         {// it is no longer available
          //System.out.print("Game Unavailable\n");
            gameList.get(i).setName("Game No Longer Available");
         }
         else
         {// or we can make due with the webpage title itself
            if(title.contains("Save") && title.contains("%"))
            {// sometimes the webpage title will include the fact that it's on sale:
             // "Save XX% on [game] on Steam"
             // eliminating both parts of unwanted information is simple enough
             // Note: I don't believe Steam allows a sale unless it's at least 10% off,
             // so there's no need to worry about eliminating an extra character accidentally.
             //System.out.print( title.substring( 12, title.length() - 9) + "\n" );
               gameList.get(i).setName(title.substring(12, title.length() - 9));
            }
            else
            {// else, webpage is invariably titled "[game] on steam" 
             // so we shorten it to just "[game]"
             //System.out.print(title.substring(0, title.length() - 9) + "\n");
               gameList.get(i).setName(title.substring(0, title.length() - 9));
            }
         } 
      }
   }
   
   public static String getEverything()
   {
      String give = "";
      for(int i = 0; i < gameList.size(); i++)
      {
         give += gameList.get(i).getName()     + " : " 
               + gameList.get(i).getPlaytime() + " minutes\n";
      }
      return give;
   }
}

/**/