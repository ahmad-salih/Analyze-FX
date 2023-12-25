package com.fx.analyzeFx;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;

public class WriteToLog {
    private static final Semaphore sem1 = new Semaphore(1);
    private static final Semaphore sem2 = new Semaphore(1);
    private final DateTimeFormatter monthFolder = DateTimeFormatter.ofPattern("MM");
    private final DateTimeFormatter yearFolder = DateTimeFormatter.ofPattern("yyyy");
    private final DateTimeFormatter fileName = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void writeLog(String finalLog) {
        BufferedWriter bw = null;
        try {
            sem1.acquire();
            File file =
                    new File(
                            "log/"
                                    + yearFolder.format(LocalDateTime.now())
                                    + "/"
                                    + monthFolder.format(LocalDateTime.now()));
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            file =
                    new File(
                            "log/"
                                    + yearFolder.format(LocalDateTime.now())
                                    + "/"
                                    + monthFolder.format(LocalDateTime.now())
                                    + "/"
                                    + fileName.format(LocalDateTime.now())
                                    + ".txt");


            bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file,true), "UTF8"));
            bw.write(finalLog);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                bw.close();
            } catch (Exception ignored){

            }
        }

        sem1.release();
    }

    public void writeToExceptionLog(String finalLog) {
        BufferedWriter bw = null;
        try {
            sem2.acquire();
            File file =
                    new File(
                            "log/Exception/"
                                    + yearFolder.format(LocalDateTime.now())
                                    + "/"
                                    + monthFolder.format(LocalDateTime.now()));
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            file =
                    new File(
                            "log/Exception/"
                                    + yearFolder.format(LocalDateTime.now())
                                    + "/"
                                    + monthFolder.format(LocalDateTime.now())
                                    + "/"
                                    + fileName.format(LocalDateTime.now())
                                    + ".txt");

            bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file,true), "UTF8"));
            bw.write(String.valueOf(finalLog));
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try{
                bw.close();
            } catch (Exception ignored){

            }
        }

        sem2.release();
    }

}

