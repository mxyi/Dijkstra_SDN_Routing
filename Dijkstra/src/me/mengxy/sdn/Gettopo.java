package me.mengxy.sdn;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.Math.*;

public class Gettopo {

    public static int MaxValue = 1000000000;

    int nodesnum = 0;

    int linksnum = 0;

    int hostsnum = 0;

//    public static void main(String[] args) {
////        JsonParser parser = new JsonParser();
////        try {
////            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
////            String nodesnum = object.get("nodesnum").getAsString();
////            String linksnum = object.get("linksnum").getAsString();
////            JsonArray array = object.get("link").getAsJsonArray();
////            for (int i = 0; i < array.size(); i++) {
////                JsonObject subObject = array.get(i).getAsJsonObject();
////                System.out.println("lid:" + subObject.get("lid").getAsInt());
////                System.out.println("src:" + subObject.get("src").getAsInt());
////                System.out.println("sp:" + subObject.get("sp").getAsInt());
////                System.out.println("dst:" + subObject.get("dst").getAsInt());
////                System.out.println("dp:" + subObject.get("dp").getAsInt());
////                System.out.println("bw:" + subObject.get("bw").getAsInt());
////                System.out.println("dl:" + subObject.get("dl").getAsInt());
////            }
////        } catch (JsonIOException e) {
////            e.printStackTrace();
////        } catch (JsonSyntaxException e) {
////            e.printStackTrace();
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//    }


    public String getMethod(String FilePath) {
        String method = null;
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            method = object.get("method").getAsString();
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return method;
    }

    public int getNodesnum(String FilePath) {
        JsonParser parser = new JsonParser();

        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            JsonArray hlist = object.get("hlist").getAsJsonArray();
            this.hostsnum = hlist.size();
            this.nodesnum = object.get("nodesnum").getAsInt();
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.nodesnum;
    }

    public int getLinksnum(String FilePath) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            this.linksnum = object.get("linksnum").getAsInt();
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.linksnum;
    }

    public int[][] getTopoinfo_bw(String FilePath) {
        JsonParser parser = new JsonParser();
        int[][] matrix = new int[nodesnum][nodesnum];//初始化矩阵
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            int nodesnum = object.get("nodesnum").getAsInt();
            for (int i = 0; i < nodesnum; i++) {
                for (int j = 0; j < nodesnum; j++) {
                    matrix[i][j] = MaxValue;
                }
            } //初始化矩阵
            JsonArray linkinfo = object.get("linkinfo").getAsJsonArray();
            for (int i = 0; i < linkinfo.size(); i++) {
                JsonObject subObject = linkinfo.get(i).getAsJsonObject();
                int src = subObject.get("src").getAsInt();
                int dst = subObject.get("dst").getAsInt();
                int bw = subObject.get("bw").getAsInt();
                matrix[src][dst] =( 10000 / bw);
                matrix[dst][src] =( 10000 / bw);
            }
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public int[][] getTopoinfo_dl(String FilePath) {

        JsonParser parser = new JsonParser();
        int[][] matrix = new int[nodesnum][nodesnum];//初始化矩阵
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            int nodesnum = object.get("nodesnum").getAsInt();
            for (int i = 0; i < nodesnum; i++) {
                for (int j = 0; j < nodesnum; j++) {
                    matrix[i][j] = MaxValue;
                }
            } //初始化矩阵
            JsonArray jsonArray = object.get("linkinfo").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject subObject = jsonArray.get(i).getAsJsonObject();
                int src = subObject.get("src").getAsInt();
                int dst = subObject.get("dst").getAsInt();
                int dl = subObject.get("dl").getAsInt();
                matrix[src][dst] = dl;
                matrix[dst][src] = dl;
            }
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public int[] getHostlist(String FilePath) {
        int[] hlist = new int[hostsnum];
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            JsonArray array = object.get("hlist").getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject subObject = array.get(i).getAsJsonObject();
                int hid = subObject.get("hid").getAsInt();
                hlist[i] = hid;
            }
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return hlist;
    }

    public int[] getPathinfo(String FilePath, int line) {

        int[] topoinfo = new int[5];
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            JsonArray linkinfo = object.get("linkinfo").getAsJsonArray();
            JsonObject subObject = linkinfo.get(line).getAsJsonObject();
            topoinfo[0] = subObject.get("src").getAsInt();
            topoinfo[1] = subObject.get("sp").getAsInt();
            topoinfo[2] = subObject.get("dst").getAsInt();
            topoinfo[3] = subObject.get("dp").getAsInt();
            topoinfo[4] = subObject.get("lid").getAsInt();
            return topoinfo;
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[][] getAllpath(String FilePath) {
        int[][] allpath = new int[linksnum][5];
        for (int i = 0; i < linksnum; i++) {
            int[] a = getPathinfo(FilePath, i);
            allpath[i][0] = a[0];
            allpath[i][1] = a[1];
            allpath[i][2] = a[2];
            allpath[i][3] = a[3];
            allpath[i][4] = a[4];
        }
        return allpath;
    }

    public int[][] getTopolinks(String FilePath, int[] logicpath) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            JsonArray linkinfo = object.get("linkinfo").getAsJsonArray();
            int[][] topolink = new int[logicpath.length][4];
            for (int i = 0; i < logicpath.length; i++) {
                int lid = logicpath[i];
                for (int j = 0; j < linkinfo.size(); j++) {
                    JsonObject subObject = linkinfo.get(j).getAsJsonObject();
                    int temp_lid = subObject.get("lid").getAsInt();
                    int src = subObject.get("src").getAsInt();
                    int sp = subObject.get("sp").getAsInt();
                    int dst = subObject.get("dst").getAsInt();
                    int dp = subObject.get("dp").getAsInt();
                    if (lid == temp_lid) {
                        topolink[i][0] = src;
                        topolink[i][1] = sp;
                        topolink[i][2] = dst;
                        topolink[i][3] = dp;
                    }
                }
            }
            return topolink;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHostip(String FilePath,int hid){
        JsonParser parser = new JsonParser();
        String ipadd = null;
        try {
            JsonObject object = (JsonObject) parser.parse(new FileReader(FilePath));
            JsonArray hlist = object.get("hlist").getAsJsonArray();
            for (int i = 0; i < hlist.size(); i++) {
                JsonObject subObject = hlist.get(i).getAsJsonObject();
                int temp_hid = subObject.get("hid").getAsInt();
                ipadd = subObject.get("ip").getAsString();
                if(temp_hid==hid){
                    return ipadd;
                }
            }
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}