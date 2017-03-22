package com.bcm.util.bcmf;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.util.CellReference;

import java.util.*;
import java.lang.Exception;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.lang.Integer;
public class BcmF {
	private ArrayList<BcmFEntry> mData;
    public BcmF(){
        System.out.println( "Construct BCMF!" );
		mData = new ArrayList<BcmFEntry>();
    }
	public void load(String filePath){
		try{
			Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				BcmFEntry entry = new BcmFEntry();
				Iterator<Cell> itr = row.cellIterator();
				// System.out.println(itr.next());
				entry.setUna(itr.next().toString());
				entry.setUser(itr.next().toString());
				entry.setAction(itr.next().toString());
				entry.setDate(itr.next().toString());
				this.mData.add(entry);
			}

		}catch (Exception ex){
			ex.printStackTrace();
		}

		
	}
	public void showUser(){
		for (BcmFEntry entry : mData){
			System.out.println(entry);
		}
	}
	public void showUser(String userName){
		for (BcmFEntry entry : mData){
			if (entry.getUser().compareTo(userName)==0){
				System.out.println(entry);
			}
		}
	}
	
	public class BcmFEntry {
		private String una;
		private String user;
		private String action;
		private String date;
		public BcmFEntry (){
			super();
		}
		public BcmFEntry(String una, String user, String action, String date){
			this.una = una;
			this.user = user;
			this.action = action;
			this.date = date;
		}
		public String getUna(){return this.una;}
		public String getUser(){return this.user;}
		public String getAction(){return this.action;}
		public String getDate(){return this.date;}
		public void setUna(String una){this.una = una;}
		public void setUser(String user){this.user = user;}
		public void setAction(String action){this.action = action;}
		public void setDate(String date){this.date = date;}
		public String toString(){
			return this.una+"\t"+this.user+"\t"+this.action+"\t"+this.date;
		}
	}
}
