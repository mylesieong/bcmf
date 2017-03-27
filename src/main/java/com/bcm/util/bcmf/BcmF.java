package com.bcm.util.bcmf;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.util.CellReference;

import java.util.*;
import java.lang.Exception;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.lang.Integer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BcmF {
    
    public final static int ASCENDING = 0;
    public final static int DESCENDING = 1;
    
	private ArrayList<BcmFEntry> mData;
    
    public BcmF(){
        System.out.println( "<BCM Form utililty>" );
		mData = new ArrayList<BcmFEntry>();
    }
    
	public void load(String filePath){
		try{
            Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ( row.getCell(3).toString().isEmpty() ){
                    break;
                }else{
                    BcmFEntry entry = new BcmFEntry();
                    entry.setUaa(row.getCell(1).toString() + "-" + row.getCell(2).toString());
                    entry.setUser(row.getCell(3).toString());
                    entry.setDate(row.getCell(4).toString());
                    entry.setAction(row.getCell(5).toString());
                    entry.setDepartment(row.getCell(6).toString());
                    entry.setToDate(row.getCell(7) == null?"":row.getCell(7).toString());
                    this.mData.add(entry);
                }
            }

		}catch (Exception ex){
			ex.printStackTrace();
		}

	}
    
	public void showUser(){
        this.printHeader();
		for (BcmFEntry entry : mData){
			System.out.println(entry);
		}
	}
    
	public void showUser(String userName){
        this.printHeader();
		for (BcmFEntry entry : mData){
			if (entry.getUser().compareTo(userName)==0){
				System.out.println(entry);
			}
		}
	}
    
    public void showUser(String userName, int order){
        System.out.println("Ordering by:" + order);
        this.printHeader();
        
        LinkedList<BcmFEntry> result = new LinkedList<BcmFEntry>();
        
        /* Selection record from mData and insert to result list */ 
		for (BcmFEntry entry : mData){
			if (entry.getUser().compareTo(userName)==0){
				int i = 0;
                while( i < result.size() ){
                    if(entry.getDate().compareTo(result.get(i).getDate()) == order){
                        i++;
                    }else{
                        break;
                    }
                }
                result.add(i, entry);
			}
		}
        
        /* Print result list */
        for (BcmFEntry entry : result){
            System.out.println(entry);
		}
        
	}
    
    
    public void help(){
        System.out.println("SYNOPSIS: bcmf [-sum|[DD/MM/YYYY]] [-help] [user_id]");
    }
    
    public void summary(){ 
        DateTime today = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        String todayString = fmt.print(today);
		for (BcmFEntry entry : mData){
			if ( entry.getDate().compareTo(todayString) == 0 ){
				System.out.println(entry);
			}
		}
    }
    
    public void summary(String date){ 
		for (BcmFEntry entry : mData){
			if ( entry.getDate().compareTo(date) == 0 ){
				System.out.println(entry);
			}
		}
    }
    
    private void printHeader(){
        System.out.println(this.mData.get(0));
    }
	
	public class BcmFEntry {
        
		private String mUaa;
		private String mUser;
		private String mDate;
		private String mAction;
		private String mDepartment;
		private String mToDate;
        
		public String getUaa() {return this.mUaa;}
		public String getUser() {return this.mUser;}
		public String getDate() {return this.mDate;}
		public String getAction() {return this.mAction;}
		public String getDepartment() {return this.mDepartment;}
		public String getToDate() {return this.mToDate;}
        
		public void setUaa(String uaa){this.mUaa = uaa;}
		public void setUser(String user){this.mUser = user;}
		public void setDate(String date){this.mDate = date;}
		public void setAction(String action){this.mAction = action;}
		public void setDepartment(String department){this.mDepartment = department;}
		public void setToDate(String todate){this.mToDate = todate;}
        
		public String toString(){
			return  this.mUaa+"\t"
                    +this.mUser+"\t"
                    +this.mDate+"\t"
                    +this.mAction+"\t"
                    +this.mDepartment+"\t"
                    +this.mToDate;
		}
	}
}
