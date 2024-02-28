package com.photoeditor;

import java.awt.image.BufferedImage;
import java.nio.Buffer;


/**
 * The CardObject class allows a new view to be initilized in a JPanel
 */
public class CardObject{
    public String title = " ";
    public BufferedImage AssociatedImag;

    CardObject(BufferedImage input,String Name){
        this.title=Name;
        this.AssociatedImag=input;

    }

    
    /** 
     * @return String
     */
    public String getTitle() {
        return title;
    }
    
    /** 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /** 
     * @return BufferedImage
     */
    public BufferedImage getAssociatedImag() {
        return AssociatedImag;
    }

    
    
    /** 
     * @param associatedImag
     */
    public void setAssociatedImag(BufferedImage associatedImag) {
        AssociatedImag = associatedImag;
    }

}
