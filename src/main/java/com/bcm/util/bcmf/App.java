package com.bcm.util.bcmf;

import java.util.Properties;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

public class App{

    public static void main( String[] args ){

        Properties prop = new Properties();
        InputStream is = null;

        try {

            String base = System.getenv("BCMF");
            String conf = base + File.separator + "bcmf.conf";

            prop.load(new FileInputStream(conf)); 
            String target = prop.getProperty("target_excel");
            String backup = prop.getProperty("backup");

            BcmF bcmf = new BcmF();
            bcmf.load(target);
            bcmf.setBackupPath(backup);
                        
            if (args.length == 0){
                
                bcmf.show();                            //bcmf
                
            }else if (args.length == 1){
                
                if (args[0].compareTo("-a") == 0){
                    bcmf.add();                         //bcmf -a
                }
                
                if (args[0].compareTo("-s") == 0){
                    bcmf.summary();                         //bcmf -s
                }
                
                if (args[0].compareTo("-h") == 0){
                    bcmf.help();                            //bcmf -h
                }
                
                if (args[0].compareTo("-b") == 0){
                    bcmf.backup();                            //bcmf -b
                }
                
                if (args[0].substring(0,1).compareTo("-") != 0){
                    bcmf.show(args[0]);                 //bcmf B999
                }
                
            }else{
                System.out.println("Args format wrong.");
            }
        
        }catch (Exception e){

            e.printStackTrace();
            System.out.println("Cannot load properties or excel file.");

        }finally {

            if (is != null){
                try{ is.close(); }
                catch(Exception ee){ ee.printStackTrace(); }
            }

        }

    }

}
