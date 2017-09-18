package com.bcm.util.bcmf;

import java.util.Properties;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

public class App{

    private final static String EXCEL_PROPERTY = "target_excel";
    private final static String BACKUP_PROPERTY = "backup";
    private final static String CONFIG_FILE_NAME = "bcmf.conf";
    private final static String PROJECT_ENV = "BCMF";

    public static void main( String[] args ){

        Properties prop = new Properties();
        InputStream is = null;

        try {

            String base = System.getenv(PROJECT_ENV);
            String conf = base + File.separator + CONFIG_FILE_NAME;

            prop.load(new FileInputStream(conf)); 
            String target = prop.getProperty(EXCEL_PROPERTY);
            String backup = prop.getProperty(BACKUP_PROPERTY);

            BcmF bcmf = new BcmF();
            bcmf.setExcel(target);
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
                
            }else if (args.length == 2){

                if (args[0].compareTo("-s") == 0){
                    bcmf.summary(args[1]);                         //bcmf -s 22/10/2019
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
