package pccontrol;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class PhotoOpen {
	public void open(String path){
		try {
			Desktop.getDesktop().open(new File(path));
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
}
