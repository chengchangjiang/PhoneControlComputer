package utils;


import java.util.ArrayList;
/**
 * 用于解析命令
 * @author lqq
 *
 */
public class CommandExplain{

	/**
	 * 命令分隔
	 */
	private static final char TITLE_END  = ';';
	/**
	 * 附加命令分割
	 */
	private static final char EXTEND_END = ':';
	/**
	 * 命令结束
	 */
	private static final char COMMAND_END = '!';
	private String mCommand;
	/**
	 * 命令
	 */
	private String title;
	/**
	 * 附加命令
	 */
	private ArrayList<String>extendCommand;
	public CommandExplain(String command){
		mCommand = command;
		extendCommand = new ArrayList<String>();
		decoding();
	}
	/**
	 * 解码方法，将命令解码
	 * @return
	 */
	private boolean decoding(){

		int beginIndex = 0;
		int endIndex = 0;
		
		while(endIndex <= mCommand.length() && mCommand.charAt(endIndex) != COMMAND_END){
			if(mCommand.charAt(endIndex) == TITLE_END){
				title = mCommand.substring(beginIndex, endIndex);
				beginIndex = endIndex;
			}else if(mCommand.charAt(endIndex) == EXTEND_END){
				extendCommand.add(mCommand.substring(beginIndex, endIndex));
				beginIndex = endIndex;
			}
			endIndex++;
		}
		return true;
	}
	
	/**
	 * 获取头部
	 * @return
	 */
	public String getTitle(){
		return title;
	}
	/**
	 * @param index
	 * @return 返回命令，index越界会返回null
	 */
	public String getExtendCommand(int index){
		ArrayList<String> mList = getExtendCommandList();
		if(index < 0 && index > mList.size())
			return null;
		return extendCommand.get(index);
	}
	
	private ArrayList<String> getExtendCommandList(){
		return extendCommand;
	}
}