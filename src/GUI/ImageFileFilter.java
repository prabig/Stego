package GUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter{
    public ImageFileFilter(){
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()){
            return true;
        }
        
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
            
            if (ext == null){
                return false;
            }
            
            if (ext.equals("bmp") || ext.equals("png") || ext.equals("wav") || ext.equals("jpg")){
                return true;
            }
        }        
        
        return false;
    }  

    @Override
    public String getDescription() {
        return "Supported Files: Image(.bmp, .png), Audio(.wav)";
    }
}