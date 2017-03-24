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
            prop.load(new FileInputStream("etc/bcmf.conf"));
            excelAddress = prop.getProperty("target_excel");
        }catch (IOException e){
            e.printStackTrace();
        }

		// bcmf.load("sample.xlsx");
		bcmf.load(excelAddress);
        
		if (args.length >0){
            if ( args[0].compareTo("-sum") == 0 ){
                if( args.length > 1){
                    bcmf.summary(args[1]);              //bcmf -sum 22/03/2016
                }else{
                    bcmf.summary();                     //bcmf -sum
                }
            }else if( args[0].compareTo("-help") == 0 ){
                bcmf.help();                            //bcmf -help
            }else{
                bcmf.showUser(args[0]);                 //bcmf B999
            }
		}else{
            bcmf.showUser();                            //bcmf
        }
        
    }
}
