package EnumConstants;

import java.awt.Color;

public enum Colors {
	BLACK(Color.BLACK),
	WHITE(Color.WHITE),
	GRAYLIGHT(Color.lightGray),
	GRAYDARK(Color.darkGray),
	RED(Color.RED),
	BLUE(Color.BLUE),
	CYAN(Color.CYAN),
	ORANGE(new Color(255,144,0)),
	GREEN(new Color(0,255,0)),
	PURPLE(new Color(128,100,162)),
	YELLOW(Color.YELLOW);
	
	
	//properties
	private Color color;
	
	//Constructor
	Colors(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public static Color getMyDefaultColor(int ID){
		if(ID==1){
			return GRAYLIGHT.getColor();
		}
		else if(ID==2){
			return GRAYDARK.getColor();
		}
		
		return null;
	}

	public static Color getMyAdditionalColor(){
		return WHITE.getColor();
	}
	
	public static Color getFocusedColor(int ID){
		if(ID==1){
			return BLUE.getColor();
		}
		else if(ID==2){
			return CYAN.getColor();
		}		
		return null;
	}
}
