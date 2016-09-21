package pccontrol;

import java.util.ArrayList;

public class ReadCommand {
	public String title ;
	public ArrayList<String> command = new ArrayList<String>();
	public static final char ENDCOMMAND = '!';
	public static final char NODECOMMAND = ';';
	public static final char TIMECOMMAND = ':';
	public void Read(String data) throws Exception{
		int beginIndex = 0;
		int endIndex = 0;
		while(endIndex <= data.length() && data.charAt(endIndex) != ENDCOMMAND){
			if(data.charAt(endIndex) == NODECOMMAND||data.charAt(endIndex) == TIMECOMMAND){
				title = data.substring(beginIndex, endIndex);
				System.out.println("title="+title);
				command.add(title);
				beginIndex = endIndex+1;
			}
			endIndex++;
		}
		System.out.println("command="+command);
	}
}
