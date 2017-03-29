package com.bcm.util.bcmf;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.util.CellReference;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.util.*;
import java.lang.Integer;
import java.lang.Exception;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.bcm.app.core.FileManipulator;

public class BcmF {
    
    public final static int ASCENDING = 1;
    public final static int DESCENDING = -1;
    
    private ArrayList<BcmFEntry> mData;
    private String mExcelAddress;
    private FileBackuper mBackuper;
    
    public BcmF(){
        System.out.println( "<BCM Form utililty>" );
        this.mData = new ArrayList<BcmFEntry>();
        this.mBackuper = new FileBackuper();
    }
    
    public void load(String filePath){
        this.mExcelAddress = filePath;
        try{
            Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath));
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ( row.getCell(3).toString().isEmpty()){
                    break;
                }else{
                       if ( row.getCell(3).toString().compareTo("User ID") != 0 ){
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
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setBackupPath(String path){
        this.mBackuper.setBackupPath(path);
    }
    
    public void backup(){
        this.mBackuper.setFile(new File(this.mExcelAddress));
        this.mBackuper.manipulate();
        if (this.mBackuper.isSuccess()){
            System.out.println("Backup performed.");
        }else{
            System.out.println("Backup failed.");
        }
    }
    
    public void showUser(){
        this.printHeader();
        if (this.mData.isEmpty()){
            System.out.println("--nothin--");
        }else{
            for (BcmFEntry entry : mData){
                System.out.println(entry);
            }
        }
    }
    
    public void showUser(String userName){
        this.printHeader();
        boolean findEntry = false;
        for (BcmFEntry entry : mData){
            if (entry.getUser().compareTo(userName)==0){
                findEntry = true;
                System.out.println(entry);
            }
        }
        if (!findEntry){
            System.out.println("---nothing---");
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
                    if(compareDate(entry.getDate(), result.get(i).getDate()) == order){
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
    
    private int compareDate(String dateA, String dateB){
        int result = 0;
        
        int dayA = Integer.parseInt(dateA.substring(0,2));
        int monthA = Integer.parseInt(dateA.substring(3,5));
        int yearA = Integer.parseInt(dateA.substring(6));
        int dateANum = yearA * 10000 + monthA * 100 + dayA;
        
        int dayB = Integer.parseInt(dateB.substring(0,2));
        int monthB = Integer.parseInt(dateB.substring(3,5));
        int yearB = Integer.parseInt(dateB.substring(6));
        int dateBNum = yearB * 10000 + monthB * 100 + dayB;

        if (dateANum > dateBNum) result = 1;
        if (dateANum == dateBNum) result = 0;
        if (dateANum < dateBNum) result = -1;
        
        return result;
    }
    
    
    public void help(){
        System.out.println("SYNOPSIS: bcmf [-sum|[DD/MM/YYYY]] [-help] [user_id]");
    }
    
    public void summary(){ 
        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        String todayString = fmt.print(today);
        String tomorrowString = fmt.print(tomorrow);
        /* Iterate mData and find output inserted into 3 arrays */
        ArrayList<BcmFEntry> todayCMUD = new ArrayList<BcmFEntry>();
        ArrayList<BcmFEntry> tomorrowMB = new ArrayList<BcmFEntry>();
        ArrayList<BcmFEntry> todayMB = new ArrayList<BcmFEntry>();
        for (BcmFEntry entry : mData){
            if ( entry.getDate().compareTo(todayString) == 0 
                && (entry.getAction().compareTo("C") == 0 
                || entry.getAction().compareTo("M") == 0
                || entry.getAction().compareTo("U") == 0
                || entry.getAction().compareTo("D") == 0 )){
                todayCMUD.add(entry);
            }
            if ( entry.getDate().compareTo(tomorrowString) == 0
                && entry.getAction().compareTo("MB") == 0 ){
                tomorrowMB.add(entry);
            }
            if ( entry.getToDate().compareTo(todayString) == 0
                && entry.getAction().compareTo("MB") == 0 ){
                todayMB.add(entry);
            }

        }

        /* Output 3 result array */
        System.out.println("-----Today C/M/U/D Forms-----");
        for (BcmFEntry entry : todayCMUD){
            System.out.println(entry);
        }
        System.out.println("-----Tomorrow Start MB Forms-----");
        for (BcmFEntry entry : tomorrowMB){ 
            System.out.println(entry);
        }
        System.out.println("-----Today End MB Forms-----");
        for (BcmFEntry entry : todayMB){
            System.out.println(entry);
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
        System.out.println("UAA            User    From       To         ActDepartment");
    }
    
    /**
     * Inner Class : Data Structure of Item Entry
     */
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
            String uaa = this.mUaa;
            String user = this.mUser.length()>4?this.mUser.substring(0,4)+"...":this.mUser + "   ";
            String date = this.mDate;
            String toDate = this.mToDate.isEmpty()?"          ":this.mToDate;
            String action = this.mAction;
            String department = this.mDepartment.length()>15?this.mDepartment.substring(0,15)+"...":this.mDepartment;
            return uaa + " " + user + " " + date + " " + toDate + " " + action + "\t" + department;
            //return  this.mUaa+" "
                    //+this.mUser+" "
                    //+this.mDate+" "
                    //+this.mToDate+" "
                    //+this.mAction+" "
                    //+this.mDepartment;
        }
    }

    /**
     * Inner Class: backup utility enforcement class
     */
    public class FileBackuper implements FileManipulator{
     
        private String mBackupPath;
	private File mFile;
	private boolean mIsSuccess;

	public void setBackupPath(String path){
	    this.mBackupPath = path;
	}

	@Override
	public void setFile(File f){
            this.mFile = f;
	    this.mIsSuccess = false;
	}

	@Override 
	public File getFile(){
	    return this.mFile;
	}

        @Override
	public void manipulate(){
        try{
            if (this.mFile != null && this.mFile.exists()){
                /* get current date and time*/
                DateTime datetime = new DateTime();
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMddHHmmss");
                
                String fileName = FilenameUtils.getBaseName(this.getFile().toString());
                String fileExtension = FilenameUtils.getExtension(this.getFile().toString());
                
                File backupFile = new File(this.mBackupPath + "\\" + fileName + datetime.toString(fmt) + "." + fileExtension);
                backupFile.createNewFile();
                
                FileUtils.copyFile(this.getFile(), backupFile);
                this.mIsSuccess = true;

            }else{
                this.mIsSuccess = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}

        @Override
	public boolean isSuccess(){
	    return this.mIsSuccess;
	}

    }
}
