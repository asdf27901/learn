package org.example.variable;
import java.io.*;

public class Variable_Definit {
    private static FileInputStream inputStream = null;
    private static BufferedReader bufferedReader = null;
    private static FileWriter writer = null;
    private static BufferedWriter bw = null;
    public static String str = null;

    public Variable_Definit() {
    }

    public Variable_Definit(FileInputStream inputStream, BufferedReader bufferedReader, FileWriter writer, BufferedWriter bw) {
        this.inputStream = inputStream;
        this.bufferedReader = bufferedReader;
        this.writer = writer;
        this.bw = bw;
    }

    public FileInputStream getInputStream() {
        return inputStream;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public FileWriter getWriter() {
        return writer;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setInputStream(FileInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void setWriter(FileWriter writer) {
        this.writer = writer;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public void closeStream() throws IOException {
        inputStream.close();
        bufferedReader.close();
        bw.close();
        writer.close();
    }
}
