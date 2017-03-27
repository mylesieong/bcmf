package com.bcm.util.bcmf;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class App{
    public static void main( String[] args ){
        BcmF bcmf = new BcmF();
        Properties prop = new Properties();
        String excelAddress = "";
        try {
            // prop.load(new FileInputStream("C:/Users/BI77/Documents/bin/etc/bcmf.conf"));
            prop.load(new FileInputStream("etc/bcmf.conf"));
            excelAddress = prop.getProperty("target_excel");
            bcmf.load(excelAddress);
                        
            if (args.length == 0){
                
                bcmf.showUser();                            //bcmf
                
            }else if (args.length == 1){
                
                if (args[0].compareTo("-sum") == 0){
                    bcmf.summary();                         //bcmf -sum
                }
                
                if (args[0].compareTo("-help") == 0){
                    bcmf.help();                            //bcmf -help
                }
                
                if (args[0].substring(0,1).compareTo("-") != 0){
                    bcmf.showUser(args[0]);                 //bcmf B999
                }
                
            }else if (args.length == 2){
                
                if (args[0].compareTo("-sum") == 0){
                    bcmf.summary(args[1]);                      //bcmf -sum 22/03/2016
                }
                
                if (args[0].compareTo("-l") == 0){
                    bcmf.showUser(args[1]);                     //bcmf -l B999
                }

                if (args[0].compareTo("-la") == 0){
                    bcmf.showUser(args[1], BcmF.ASCENDING);     //bcmf -la B999
                }
                
                if (args[0].compareTo("-ld") == 0){
                    bcmf.showUser(args[1], BcmF.DESCENDING);    //bcmf -ld B999
                }
                
            }else{
                System.out.println("Args format wrong.");
            }
        
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Cannot load properties or excel file.");
        }
    }
}
