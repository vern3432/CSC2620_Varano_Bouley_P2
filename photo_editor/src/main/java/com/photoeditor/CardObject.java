package com.photoeditor;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class CardObject{
    public String title="";
    public BufferedImage AssociatedImag;


    CardObject(BufferedImage AssociatedImage,String Name){
        this.title=title;
        this.AssociatedImag=AssociatedImage;



    }






    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public BufferedImage getAssociatedImag() {
        return AssociatedImag;
    }
    public void setAssociatedImag(BufferedImage associatedImag) {
        AssociatedImag = associatedImag;
    }


    

    
}
