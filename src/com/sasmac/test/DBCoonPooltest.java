package com.sasmac.test;

import java.sql.Connection;

import com.sasmac.dbconnpool.ConnPoolUtil;

public class DBCoonPooltest {

	public static void main(String[] args) {
        /**
         * 需要资源6个 ，
         * 超过常用的5个的时候，
         * 会出现一个复用的现象，正确的操作
         */
        for(int i=0;i<6;i++){
            Connection conn = ConnPoolUtil.getConnection();
            System.out.println(conn);
            ConnPoolUtil.close(conn, null, null);
        }
        /**
         * 需要资源6个，
         * 超过常用的设置的5个，
         * 且没有进行资源的释放操作，
         * 没有超过最大的设置，
         * 会进一步的连接的创建
         */
        for(int i=0;i<6;i++){
            Connection conn = ConnPoolUtil.getConnection();
            System.out.println(conn);
        }
        /**
         * 资源超过最大的20个的时候，
         * 由于我们没有进行资源的释放的操作的时候，
         * 出现异常，如下测试
         */
        for(int i=0;i<21;i++){
            Connection conn = ConnPoolUtil.getConnection();
            System.out.println(conn);
        }
         
    }
}
