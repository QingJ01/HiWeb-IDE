package com.hiweb.ide.server.easyweb_server;

import com.venter.jssrunner.*;
import com.yanzhenjie.andserver.RequestHandler;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import java.io.*;

import java.io.File;

public class StartHandler implements RequestHandler
{
    public int IPort;
    public File FLink;
    public int IType;
    public String SPath;
    public JssRunner Jr;
    public boolean BIsFirst=false;
    public StartHandler(int IPort,File FLink,int IType,String SPath,JssRunner Jr,boolean BIsFirst)
    {
        this.IPort=IPort;
        this.FLink=FLink;
        this.IType=IType;
        this.SPath=SPath;
        this.Jr=Jr;
        this.BIsFirst=BIsFirst;
    }
    @Override
    public void handle(HttpRequest p1, HttpResponse p2, HttpContext p3) throws HttpException,IOException {
        if(BIsFirst)
        {
            new StartHandler(IPort,FLink,IType,SPath,Jr,false).handle(p1,p2,p3);
        }
        else
        {
            new HandlerClass(IPort,FLink,IType,SPath,Jr).handle(p1,p2,p3);
            BIsFirst=true;
        }
    }
}
