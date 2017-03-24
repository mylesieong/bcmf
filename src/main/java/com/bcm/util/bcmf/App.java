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
			bcmf.showUser(args[0]);
		}else {
			bcmf.showUser(10);
			
		}
    }
}
