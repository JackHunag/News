package domain;

import java.util.ArrayList;

public class NewMenu {

	public int retcode;
	public ArrayList<String> extend;
	public ArrayList<MenuData> data;

	public class MenuData {

		public int id;
		public String title;
		public int type;

		public ArrayList<NewTagData> children;

		@Override
		public String toString() {
			return "MenuData [title=" + title + ", children=" + children + "]";
		}

	}

	public class NewTagData {
		public int id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewTagData [title=" + title + "]";
		}
	}

	@Override
	public String toString() {
		return "NewMenu [data=" + data + "]";
	}

}
