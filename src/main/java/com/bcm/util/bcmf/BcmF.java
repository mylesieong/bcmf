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
			// Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath));
            // Sheet sheet = workbook.getSheetAt(0);
			// for (Row row : sheet) {
				// BcmFEntry entry = new BcmFEntry();
				// try{
					// Iterator<Cell> itr = row.cellIterator();
                    // itr.next();
					// entry.setUaa(itr.next().toString() + "-" + itr.next().toString());
					// entry.setUser(itr.next().toString());
					// entry.setDate(itr.next().toString());
					// entry.setAction(itr.next().toString());
					// entry.setDepartment(itr.next().toString());
					// entry.setToDate(itr.next().toString());
				// }catch(Exception ex){
					// ex.printStackTrace();
				// }
				// this.mData.add(entry);
			// }
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
		for (BcmFEntry entry : mData){
			System.out.println(entry);
		}

	}
    
	public void showUser(int n){
        for (int i = 0; i < n ; i++){
			System.out.println(mData.get(i));
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
