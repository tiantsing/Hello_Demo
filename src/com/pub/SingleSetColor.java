package com.pub;

public class SingleSetColor {
	public static String Name[] = new String[50];
	private static SingleSetColor instance = null;
   
	public SingleSetColor() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < 50; i++) {
			Name[i] = "0";
		}
	}
    public static SingleSetColor getInstance()
    {
    	 if(instance==null){
             synchronized(SingleGlobalVariables.class){
                 if(instance==null){
                     instance=new SingleSetColor();
                 }
             }
         }
		return instance;
    }
	public int cheackName(String Name) {
		int length = 0;
		for (int i = 0; i < 50; i++) {
			if (!this.Name[i].equals("0")) {
				length++;
			}
		}
		int i;
		for (i = 0; i < length; i++) {
			if (this.Name[i].equals(Name)) {
				break;
			}
		}
		if (i == length)
			this.Name[i] = Name;
		return SingleGlobalVariables.color[i];
	}
}
