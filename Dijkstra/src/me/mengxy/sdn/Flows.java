package me.mengxy.sdn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class Flows {

    public static void getFlowentery(int[][] portlist, int[] path,String src_ip,String dst_ip) throws Exception {

        int in_port=0;
        int out_port=0;
        int switchid=0;
        int src = path[0];
        int dst = path[path.length-1];

        for (int i = 0; i < portlist.length; i++) {
            in_port = portlist[i][0];
            switchid = portlist[i][1];
            out_port = portlist[i][2];

            String ipflowname = "flow-"+switchid+"-"+src+"-"+dst+"-out";
            String reipflowname = "flow-"+switchid+"-"+src+"-"+dst+"-in";
            String arpflowname = "arp-"+switchid+"-"+src+"-"+dst+"-out";
            String rearpflowname = "arp-"+switchid+"-"+src+"-"+dst+"-in";
            issuedFlowsentry(switchid,ipflowname,src_ip,dst_ip,in_port,out_port);
            issuedFlowsentry(switchid,reipflowname,dst_ip,src_ip,out_port,in_port);
            issuedArpflowsentry(switchid,arpflowname,src_ip,dst_ip,in_port,out_port);
            issuedArpflowsentry(switchid,rearpflowname,dst_ip,src_ip,out_port,in_port);
        }
    }

    public static void issuedFlowsentry(int switch_id,String name,String src_ip,String dst_ip,int in_port,int out_port) throws Exception {

        String str_output = "output=";
        String outport=out_port+"";
        String hex_switch_id = Integer.toHexString(switch_id);
        String temp_inport=in_port+"";
        String temp_outport= str_output.concat(outport);
        String temp_switchid =hex_switch_id;


        String strURL = "http://sdn:8080/wm/staticflowpusher/json";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("switch",temp_switchid);
        paramMap.put("name", name);
        paramMap.put("cookie", "0");
        paramMap.put("priority", "1000");
        paramMap.put("eth_type", "0x0800");
        paramMap.put("ipv4_src", src_ip);
        paramMap.put("ipv4_dst", dst_ip);
        paramMap.put("in_port", temp_inport);
        paramMap.put("active", "true");
        paramMap.put("actions", temp_outport);

        OutputStreamWriter out = null;
        InputStream is = null;
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8); // utf-8编码
            out.append(JsonUtil.object2json(paramMap));
            out.flush();
            out.close();

            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, StandardCharsets.UTF_8); // utf-8编码
                System.out.println("Controller Message:" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void issuedArpflowsentry(int switch_id,String name,String src_ip,String dst_ip,int in_port,int out_port) throws Exception {

        String str_output = "output=";
        String outport=out_port+"";
        String hex_switch_id = Integer.toHexString(switch_id);
        String temp_inport=in_port+"";
        String temp_outport= str_output.concat(outport);
        String temp_switchid =hex_switch_id;


        String strURL = "http://sdn:8080/wm/staticflowpusher/json";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("switch",temp_switchid);
        paramMap.put("name", name);
        paramMap.put("cookie", "0");
        paramMap.put("priority", "1000");
        paramMap.put("eth_type", "0x0806");
        paramMap.put("arp_spa", src_ip);
        paramMap.put("arp_tpa", dst_ip);
        paramMap.put("in_port", temp_inport);
        paramMap.put("active", "true");
        paramMap.put("actions", temp_outport);

        OutputStreamWriter out = null;
        InputStream is = null;
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8); // utf-8编码
            out.append(JsonUtil.object2json(paramMap));
            out.flush();
            out.close();

            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, StandardCharsets.UTF_8); // utf-8编码
                System.out.println("Controller Message:" + result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}