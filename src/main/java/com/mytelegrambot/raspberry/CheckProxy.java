package com.mytelegrambot.raspberry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.net.InetAddress;

public class CheckProxy {

    boolean checkProxie(String host, int port){
        try{
            InetAddress addr=InetAddress.getByName(host);
            if(addr.isReachable(5000)){
//                System.out.println("reached");
                return ensocketize(host, port);
            }
        } catch(Exception ignored) {}
        return false;
    }

    private boolean ensocketize(String host, int port){
        try{
            HttpClient client=new DefaultHttpClient();
            HttpGet get=new HttpGet("https://www.google.it");
            HttpHost proxy=new HttpHost(host,port);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
            HttpResponse response=client.execute(get);
            if(response!=null){
                return true;
            }
        } catch(Exception ignored){}
        return false;
    }

    public static void main(String[] args){
        String host = "142.93.87.88";
        int port = 3128;

        boolean status = new CheckProxy().checkProxie(host, port);
        if(status) {
            System.out.println(host+":"+port+" @@ working!");
        } else {
            System.out.println("Proxy failed");
        }
    }
}