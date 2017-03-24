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
                bcmf.summary();
            }else if( args[0].compareTo("-help") == 0 ){
                bcmf.help();
            }else{
                bcmf.showUser(args[0]);
            }
		}else{
            bcmf.showUser();
        }
    }
}
