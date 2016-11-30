package domain;

import java.util.ArrayList;

public class NewTagMenu {

	public NewTagData data;

	public class NewTagData {

		public String more;
		public ArrayList<NewData> news;
		public ArrayList<NewTopData> topnews;

		public class NewData {
			public int id;
			public String listimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}

		public class NewTopData {
			public int id;
			public String topimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}

	}

}
