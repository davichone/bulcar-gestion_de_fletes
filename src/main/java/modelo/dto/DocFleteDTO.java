/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author drola
 */
public class DocFleteDTO {

    private int id;
    private String docNum;

    public DocFleteDTO(int id, String docNum) {
        this.id = id;
        this.docNum = docNum;
    }

    public DocFleteDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getLastNumbers() {
        int i = this.docNum.length();
        String lastChart = "";
        if (i == 10) {
            char x = this.getDocNum().charAt(9);
            lastChart = String.valueOf(x);
            return lastChart;
        } else if (i == 11) {
            char x1 = this.getDocNum().charAt(9);
            String s1 = String.valueOf(x1);

            char x2 = this.getDocNum().charAt(10);
            String s2 = String.valueOf(x2);

            lastChart = s1 + s2;
            return lastChart;
        }
        return lastChart;
    }
    
    public boolean equalDate(){
        
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MMddyy");
        
        String d2 = hoy.format(formato);
        String d1 =this.docNum.substring(2, 8);
        
        return d1.equalsIgnoreCase(d2);
    }

}
