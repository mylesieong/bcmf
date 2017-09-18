package com.bcm.util.bcmf;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import java.util.*;
import java.io.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.bcm.app.core.FileBackuper;

public class BcmF {
    
    public final static int ASCENDING = 1;
    public final static int DESCENDING = -1;
    
    private ArrayList<BcmFEntry> mData;
    private String mExcelAddress;
    private Workbook mWorkbook;
    private FileBackuper mBackuper;
    
    public BcmF(){
        System.out.println( "<BCM Form utililty>" );
        this.mData = new ArrayList<BcmFEntry>();
        this.mBackuper = new FileBackuper();
    }
    
    public void setExcel(String filePath){

        this.mExcelAddress = filePath;
        this.mBackuper.setFile(new File(filePath));

        InputStream is = null;

        try{

            is = new FileInputStream(filePath);
            this.mWorkbook = WorkbookFactory.create(is);
            Sheet sheet = mWorkbook.getSheetAt(0);
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
                        entry.setToDepartment(row.getCell(9) == null?"":row.getCell(9).toString());
                        this.mData.add(entry);
                    }
                }
            }

        }catch (Exception e){

            e.printStackTrace();

        }finally{

            if (is != null){
                try {is.close();}
                catch (Exception ee){ee.printStackTrace();}
            }

        }

    }

    public void setBackupPath(String path){
        this.mBackuper.setPath(path);
    }
    
    public void add(){

        String user = "";
        String action = "";
        String department ="";
        String date = "";
        String toDate = "";
        String toDepartment ="";

        Scanner s = null;
        OutputStream os = null;

        try{

            s = new Scanner(System.in);
            os = new FileOutputStream(this.mExcelAddress);

            // Read user id
            System.out.print("Please input user id: ");
            user = s.next();

            // Read action category
            System.out.print("Action category(C/M/U/D/MB): ");
            action = s.next();

            // Read detail base on category selection
            if (action.compareToIgnoreCase("MB") == 0){

                System.out.println("Select from department from below options: ");
                department = selectDepartment(s, user);
                System.out.println("Select to department from below options: ");
                toDepartment = selectDepartment(s, user);
                System.out.print("From date (dd/mm/yyyy): ");
                date = s.next();
                System.out.print("To date (dd/mm/yyyy): ");
                toDate = s.next();

            }else{

                System.out.println("Select user department from below options: ");
                department = selectDepartment(s, user);
                System.out.print("Date (dd/mm/yyyy): ");
                date = s.next();

            }

            // Write to excel 
            Sheet sheet = mWorkbook.getSheetAt(0);
            for (Row row : sheet) {
                if ( row.getCell(3).toString().isEmpty()){

                    Cell cell = row.getCell(3);
                    if (cell == null)  row.createCell(3);
                    row.getCell(3).setCellValue(user);

                    cell = row.getCell(4);
                    if (cell == null)  row.createCell(4);
                    row.getCell(4).setCellValue(date);

                    cell = row.getCell(5);
                    if (cell == null)  row.createCell(5);
                    row.getCell(5).setCellValue(action);

                    cell = row.getCell(6);
                    if (cell == null)  row.createCell(6);
                    row.getCell(6).setCellValue(department);

                    cell = row.getCell(7);
                    if (cell == null)  row.createCell(7);
                    row.getCell(7).setCellValue(toDate);

                    cell = row.getCell(9);
                    if (cell == null)  row.createCell(9);
                    row.getCell(9).setCellValue(toDepartment);

                    break;

                }
            }
            XSSFFormulaEvaluator.evaluateAllFormulaCells(this.mWorkbook);
            this.mWorkbook.write(os); 
        
        }catch(Exception e){

            e.printStackTrace();

        }finally{

            if (os != null){
                try {os.close();}
                catch (Exception ee){ee.printStackTrace();}
            }

        }

    }

    private String selectDepartment(Scanner s, String user){
        // Collect possibilities from mData
        Set<String> p = new HashSet<String>();
        for (BcmFEntry e: mData){
            if (e.getUser().compareToIgnoreCase(user) == 0){
                p.add(e.getDepartment());
                p.add(e.getToDepartment());
            }
        }

        // List possibilities
        List<String> lp = new ArrayList<String>();
        for (String str: p){
            if (str.compareTo("") != 0){
                lp.add(str);
            }
        }

        System.out.println("0: <User Input>");
        for (int i = 0; i < lp.size(); i++){
            String id = Integer.toString(i + 1);
            System.out.println(id + ": " + lp.get(i));
        }

        // Read user's input
        while (true) {
            int i = s.nextInt();
            if ( i > lp.size() || i < 0 ){
                System.out.println("Not valid options. Input again: ");
            }else if(i == 0){
                return s.next();
            }else{
                return lp.get(i-1);
           }
        }
        
    }

    public void backup(){
        this.mBackuper.manipulate();
        if (this.mBackuper.isSuccess()){
            System.out.println("Backup performed.");
        }else{
            System.out.println("Backup failed.");
        }
    }

    public void show(){
        show(null, BcmF.ASCENDING);
    }
    
    public void show(String userName){
        show(userName, BcmF.ASCENDING); 
    }
    
    public void show(String userName, int order){

        this.printHeader();
        LinkedList<BcmFEntry> result = new LinkedList<BcmFEntry>();
        
        /* Selection record from mData and insert to result list */ 
        if (userName == null){

            for (BcmFEntry entry : mData){
                result.add(entry);
            }

        }else{

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
       String helptext = "bcmf [ -a | -s | -b | -h | user_id | date_dmy ]";
       System.out.println(helptext);
    }
    
    public void summary(){ 
        DateTime today = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        String todayString = fmt.print(today);
        summary(todayString);
    }
    
    public void summary(String dateString){ 
        // Check date format

        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime date = fmt.parseDateTime(dateString);
        DateTime dateAfter = date.plusDays(1);
        String dateAfterString = fmt.print(dateAfter);

        // Iterate mData and find output inserted into 3 arrays 
        ArrayList<BcmFEntry> dateCMUD = new ArrayList<BcmFEntry>();
        ArrayList<BcmFEntry> dateAfterMB = new ArrayList<BcmFEntry>();
        ArrayList<BcmFEntry> dateMB = new ArrayList<BcmFEntry>();
        for (BcmFEntry entry : mData){
            if ( entry.getDate().compareTo(dateString) == 0 
                && (entry.getAction().compareTo("C") == 0 
                || entry.getAction().compareTo("M") == 0
                || entry.getAction().compareTo("U") == 0
                || entry.getAction().compareTo("D") == 0 )){
                dateCMUD.add(entry);
            }
            if ( entry.getDate().compareTo(dateAfterString) == 0
                && entry.getAction().compareTo("MB") == 0 ){
                dateAfterMB.add(entry);
            }
            if ( entry.getToDate().compareTo(dateString) == 0
                && entry.getAction().compareTo("MB") == 0 ){
                dateMB.add(entry);
            }

        }

        // Output 3 result array 
        System.out.println("-----Date C/M/U/D Forms-----");
        for (BcmFEntry entry : dateCMUD){
            System.out.println(entry);
        }
        System.out.println("-----AfterDate Start MB Forms-----");
        for (BcmFEntry entry : dateAfterMB){ 
            System.out.println(entry);
        }
        System.out.println("-----Date End MB Forms-----");
        for (BcmFEntry entry : dateMB){
            System.out.println(entry);
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
        private String mToDepartment;
        
        public String getUaa() {return this.mUaa;}
        public String getUser() {return this.mUser;}
        public String getDate() {return this.mDate;}
        public String getAction() {return this.mAction;}
        public String getDepartment() {return this.mDepartment;}
        public String getToDate() {return this.mToDate;}
        public String getToDepartment() {return this.mToDepartment;}
        
        public void setUaa(String uaa){this.mUaa = uaa;}
        public void setUser(String user){this.mUser = user;}
        public void setDate(String date){this.mDate = date;}
        public void setAction(String action){this.mAction = action;}
        public void setDepartment(String department){this.mDepartment = department;}
        public void setToDate(String todate){this.mToDate = todate;}
        public void setToDepartment(String toDepartment){this.mToDepartment = toDepartment;}
        
        public String toString(){
            String uaa = this.mUaa;
            String user = this.mUser.length()>4?this.mUser.substring(0,4)+"...":this.mUser + "   ";
            String date = this.mDate;
            String toDate = this.mToDate.isEmpty()?"          ":this.mToDate;
            String action = this.mAction;
            String department = this.mDepartment.length()>15?this.mDepartment.substring(0,15)+"...":this.mDepartment;
            String toDepartment = this.mToDepartment.length()>15?this.mToDepartment.substring(0,15)+"...":this.mToDepartment;
            return uaa + " " + user + " " + date + " " + toDate + " " + action 
                + "\t" + department + " " + toDepartment;
        }
    }

}
