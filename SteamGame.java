public class SteamGame
{
   private String name;
   private int appid;
   private int playtime;
   
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