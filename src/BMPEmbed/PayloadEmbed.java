
package BMPEmbed;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

/**
 *
 * @author Evan
 */
public class PayloadEmbed {

    public PayloadEmbed(){
        
    }
    
    public boolean add(File path, String message, String ext){
        BufferedImage pic_orig = getPic(path);
        BufferedImage pic = user_space(pic_orig);
        pic = encode(pic, message);
        return(setImage(pic, ext));
    }
    
    
    private BufferedImage encode(BufferedImage pic, String text){
        byte[] img = image_data(pic);
        byte[] message = text.getBytes();
        byte[] len = conversion(message.length);
        try{
            embed(img, len, 0);
            embed(img, message, 32);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "File cannot hold message!");
        }
        return pic;
    }
    
    private byte[] conversion(int i){
        byte byte4 = (byte)((i & 0xFF000000) >>> 24);
        byte byte3 = (byte)((i & 0x00FF0000) >>> 16);
        byte byte2 = (byte)((i & 0x0000FF00) >>> 8);
        byte byte1 = (byte)(i & 0x000000FF);
        return(new byte[]{byte4, byte3, byte2, byte1});
    }
    
    private byte[] embed(byte[] pic, byte[] message, int offset){
        if(message.length > pic.length){
            throw new IllegalArgumentException("Message wont' fit in the picture!");
        }
        for(int i = 0; i < message.length; i++){
            int add = message[i];
            for(int bit = 7; bit >= 0; bit--, offset++){
                int b = (add >>> bit) & 1;
                pic[offset] = (byte)((pic[offset] & 0xFE) | b);
            }
        }
        return pic;
    }
    
    private byte[] image_data(BufferedImage pic){
        WritableRaster img = pic.getRaster();
        DataBufferByte buf = (DataBufferByte)img.getDataBuffer();
        return buf.getData();
    }
    
    private BufferedImage user_space(BufferedImage pic){
        BufferedImage new_pic = new BufferedImage(pic.getWidth(), pic.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = new_pic.createGraphics();
        g.drawRenderedImage(pic, null);
        g.dispose();
        return new_pic;
    }
    
    private BufferedImage getPic(File f){
        BufferedImage pic = null;
        //File file = new File(f);
        try{
            pic = ImageIO.read(f);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Picture could not be read!");
        }
        return pic;
    }
    
    private boolean setImage(BufferedImage pic, String ext){
        File fileToSave;
        while(true){
            fileToSave = null;
            JFileChooser choose = new JFileChooser();
            choose.setDialogTitle("Save file as");
            int userSelection = choose.showSaveDialog(choose);
            if(userSelection == JFileChooser.APPROVE_OPTION){
                fileToSave = choose.getSelectedFile();
                String check = fileToSave.getPath();
                int extIndex = check.lastIndexOf(".");
                String extCheck = check.substring(extIndex+1, check.length());
                if(extCheck.equalsIgnoreCase(ext)){
                    break;
                }
                else{
                    JOptionPane.showMessageDialog(null, "Saved file must be of the same " +
                        "type as the original file: " + ext);
                }
            }
        }
        try{
            ImageIO.write(pic,ext,fileToSave.getAbsoluteFile());
            return true;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "File could not be saved!");
            return false;
        }
    }
    
    public String decode(File f){
        byte[] decode;
        try{
            BufferedImage img = user_space(getPic(f));
            decode = decode_text(image_data(img));
            if(Arrays.equals(decode, "".getBytes())){
                return "";
            }
            return(new String(decode));
        }catch(Exception e){
            return "";
        }
    }
    
    private byte[] decode_text(byte[] image){
        byte[] result;
        int length = 0;
        int offset = 32;
        for(int i = 0; i < 32; i++){
            length = (length << 1) | (image[i] & 1);
        }
        try{
            result = new byte[length];
        }catch(OutOfMemoryError e){
            return "".getBytes();
        }
        
        for(int b = 0; b < result.length; b++){
            for(int j = 0; j < 8; j++, offset++){
                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }
}


