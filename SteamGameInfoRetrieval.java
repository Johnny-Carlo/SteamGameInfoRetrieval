import java.io.*;
import java.util.*;
import java.net.*;

import org.json.*;

/*

 *Expected arguments: 
 *args[0]: the name of the output file 
 *args[1]: The ID of the person in question
 *args[2]: Steam's API access key

 */

public class SteamGameInfoRetrieval
{

   public static int gameCount;
   public static List<SteamGame> gameList = new ArrayList<SteamGame>();
   
   public static void main(String [] args) throws IOException, JSONException
   {
      String complete_url = prepareURL(args[1], args[2]);
      
      URLConnection connection = new URL(complete_url).openConnection();
      InputStream response = connection.getInputStream();
      String out = convertStreamToString(response);
      response.close();
      //System.out.print(out);
      writeOut(out, "json.txt");
      
      parseJSONToList(out);
      // takeNames();
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

      String query = "key=" + param1 
         + "&steamid=" + param2 
         + "&format=" + param3 
         + "&include_appinfo=1&include_played_free_games=1";
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
            arr.getJSONObject(i).getInt( "playtime_forever" ),
            arr.getJSONObject(i).getString( "name" ) 
            );
            // construct a new steam game object...
            
         gameList.add(thing);
         // ...and add it to the list
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