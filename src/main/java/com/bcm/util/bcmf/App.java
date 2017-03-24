package com.bcm.util.bcmf;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){
        BcmF bcmf = new BcmF();
		bcmf.load("sample.xlsx");
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
