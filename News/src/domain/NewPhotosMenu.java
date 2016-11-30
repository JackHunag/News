package domain;

import java.util.ArrayList;

public class NewPhotosMenu {

	  public PhotosData data;
	  
	  public class PhotosData{
		  
		  public String more;
		  public ArrayList<PhotosNewData> news;
	  }

       public class PhotosNewData{
    	   public int id;
    	   public String title;
    	   public String listimage;
    	   public String url;
       }
 }
