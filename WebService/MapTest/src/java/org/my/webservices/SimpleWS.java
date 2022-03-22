/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package org.my.webservices;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.lang.Math;

/**
 *
 * @author Wiki
 */
@WebService(serviceName = "SimpleWS")
public class SimpleWS {
    private static final String MAP_PATH = "C:\\mapa_torun.jpg";
    private BufferedImage img = null;
   
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getInitialImage")
    public String getInitialImage() {
        
        if (img == null)
        {
            try 
            {
                img = ImageIO.read(new File(MAP_PATH));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream byte_array_image = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "jpg", byte_array_image);
        } 
        catch (IOException ex) {
            Logger.getLogger(SimpleWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte [] bytes = byte_array_image.toByteArray();
        String image_base64 = Base64.getEncoder().encodeToString(bytes);
        
        return image_base64;
    }
    
     /*
     * Web service operation
     */
    @WebMethod(operationName = "getCroppedImage")
    public String getCroppedImage(@WebParam(name="x1") int x1, @WebParam(name="y1")int y1, 
                                  @WebParam(name="x2")int x2, @WebParam(name="y2")int y2) 
    {
        int x_start, y_start, x_end, y_end;
        
        if (x1 < x2)
        {
            x_start = x1;
            x_end = x2;
        }
        else
        {
            x_start = x2;
            x_end = x1;
        }
        
        if (y1 < y2)
        {
            y_start = y1;
            y_end = y2;
        }
        else
        {
            y_start = y2;
            y_end = y1;
        }
        
        
        if (img == null)
        {
            try 
            {
                img = ImageIO.read(new File(MAP_PATH));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
                
        int width = x_end - x_start;
        int height = y_end - y_start; 

        BufferedImage subimg = img.getSubimage(x_start, y_start, width, height);
        
        ByteArrayOutputStream byte_array_image = new ByteArrayOutputStream();
        try {
            ImageIO.write(subimg, "jpg", byte_array_image);
        } 
        catch (IOException ex) {
            Logger.getLogger(SimpleWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte [] bytes = byte_array_image.toByteArray();
        String image_base64 = Base64.getEncoder().encodeToString(bytes);
        
        System.out.println(img.getWidth());
        return image_base64;
    }
    
    /*
     * Web service operation
     */
    @WebMethod(operationName = "getCoordsImage")
    public String getCoordsImage(@WebParam(name="x1_s") String x1_s, @WebParam(name="y1_s") String y1_s, 
                                 @WebParam(name="x2_s") String x2_s, @WebParam(name="y2_s") String y2_s) 
    {
        double x1 = Double.parseDouble(x1_s),
               x2 = Double.parseDouble(x2_s),
               y1 = Double.parseDouble(y1_s),
               y2 = Double.parseDouble(y2_s);
        
        final double max_w_px = 1000.0,
                     max_h_px = 1000.0,
                     top_left_x = 53.116141, 
                     top_left_y = 18.471981, 
                     bot_right_x = 52.910132, 
                     bot_right_y = 18.813919,
                     max_w_cords = top_left_x - bot_right_x,
                     max_h_cords = bot_right_y - top_left_y,
                     w_coeff = max_w_px/max_w_cords,
                     h_coeff = max_h_px/max_h_cords;
        

        double abs_x1 = Math.abs(top_left_x - x1),
               abs_x2 = Math.abs(top_left_x - x2),
               abs_y1 = Math.abs(top_left_y - y1),
               abs_y2 = Math.abs(top_left_y - y2);
        
        System.out.println("X1: " + abs_x1 + " Y1: " + abs_y1 +  " X2: " + abs_x2 + " Y2: " + abs_y2);
        
        
        double x_start , y_start, x_end, y_end;
        
        if (abs_x1 < abs_x2)
        {
            x_start = abs_x1;
            x_end = abs_x2;
        }
        else
        {
            x_start = abs_x2;
            x_end = abs_x1;
        }
        
        if (abs_y1 < abs_y2)
        {
            y_start = abs_y1;
            y_end = abs_y2;
        }
        else
        {
            y_start = abs_y2;
            y_end = abs_y1;
        }
        
        
        if (img == null)
        {
            try 
            {
                img = ImageIO.read(new File(MAP_PATH));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
                
        double width = x_end - x_start,
               height = y_end - y_start; 
        
        System.out.println("Width: " + width + " Height: " + height);
        System.out.println("Coef_W: " + w_coeff + " Coeff_H: " + h_coeff);
        
        int width_px = (int) Math.round(w_coeff * width),
            height_px = (int) Math.round(h_coeff * height),
            x_start_px = (int) Math.round(w_coeff * x_start),
            y_start_px = (int) Math.round(h_coeff * y_start);
        
        System.out.println("Width: " + width_px + " Height: " + height_px +  " X: " + x_start_px + " Y: " + y_start_px);
        BufferedImage subimg = img.getSubimage(x_start_px, y_start_px, width_px, height_px);
        
        ByteArrayOutputStream byte_array_image = new ByteArrayOutputStream();
        try {
            ImageIO.write(subimg, "jpg", byte_array_image);
        } 
        catch (IOException ex) {
            Logger.getLogger(SimpleWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte [] bytes = byte_array_image.toByteArray();
        String image_base64 = Base64.getEncoder().encodeToString(bytes);
        
        System.out.println(img.getWidth());
        return image_base64;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getCoordsImageMarshal")
    public String getCoordsImageMarshal(@WebParam(name = "x1") double x1, @WebParam(name = "y1") double y1, @WebParam(name = "x2") double x2, @WebParam(name = "y2") double y2) {
 
        final double max_w_px = 1000.0,
                     max_h_px = 1000.0,
                     top_left_x = 53.116141, 
                     top_left_y = 18.471981, 
                     bot_right_x = 52.910132, 
                     bot_right_y = 18.813919,
                     max_w_cords = top_left_x - bot_right_x,
                     max_h_cords = bot_right_y - top_left_y,
                     w_coeff = max_w_px/max_w_cords,
                     h_coeff = max_h_px/max_h_cords;
        

        double abs_x1 = Math.abs(top_left_x - x1),
               abs_x2 = Math.abs(top_left_x - x2),
               abs_y1 = Math.abs(top_left_y - y1),
               abs_y2 = Math.abs(top_left_y - y2);
        
        System.out.println("X1: " + abs_x1 + " Y1: " + abs_y1 +  " X2: " + abs_x2 + " Y2: " + abs_y2);
        
        
        double x_start , y_start, x_end, y_end;
        
        if (abs_x1 < abs_x2)
        {
            x_start = abs_x1;
            x_end = abs_x2;
        }
        else
        {
            x_start = abs_x2;
            x_end = abs_x1;
        }
        
        if (abs_y1 < abs_y2)
        {
            y_start = abs_y1;
            y_end = abs_y2;
        }
        else
        {
            y_start = abs_y2;
            y_end = abs_y1;
        }
        
        
        if (img == null)
        {
            try 
            {
                img = ImageIO.read(new File(MAP_PATH));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
                
        double width = x_end - x_start,
               height = y_end - y_start; 
        
        System.out.println("Width: " + width + " Height: " + height);
        System.out.println("Coef_W: " + w_coeff + " Coeff_H: " + h_coeff);
        
        int width_px = (int) Math.round(w_coeff * width),
            height_px = (int) Math.round(h_coeff * height),
            x_start_px = (int) Math.round(w_coeff * x_start),
            y_start_px = (int) Math.round(h_coeff * y_start);
        
        System.out.println("Width: " + width_px + " Height: " + height_px +  " X: " + x_start_px + " Y: " + y_start_px);
        BufferedImage subimg = img.getSubimage(x_start_px, y_start_px, width_px, height_px);
        
        ByteArrayOutputStream byte_array_image = new ByteArrayOutputStream();
        try {
            ImageIO.write(subimg, "jpg", byte_array_image);
        } 
        catch (IOException ex) {
            Logger.getLogger(SimpleWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte [] bytes = byte_array_image.toByteArray();
        String image_base64 = Base64.getEncoder().encodeToString(bytes);
        
        System.out.println(img.getWidth());
        return image_base64;
    }
}
