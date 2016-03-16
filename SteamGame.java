public class SteamGame
{
   private String name;
   //The title of the game
   
   private int appid;
   //Number representing the game in [Steam's database?]
   
   private int playtime;
   //The number of minutes the person in question has spent
   //playing the game
   
   public SteamGame(int id, int play, String title)
   {
      appid = id;
      playtime = play;
      name = title;
   }
   
   public String getName()
   {
      return name;
   }
   
   public int getAppid()
   {
      return appid;
   }
   
   public int getPlaytime()
   {
      return playtime;
   }
   
   public void setName(String in)
   {
      name = in;
   }
   
   public void setAppid(int in)
   {
      appid = in;
   }
   
   public void setPlaytime(int in)
   {
      playtime = in;
   }
}