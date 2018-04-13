package net;

import java.io.Serializable;

//класс для отправки обьектов с инструкциями на сервер
public class MyRequest implements Serializable {

    //перечисление возможных типов передаваемых данных
    public static enum RequestType {USER, CAR, LIST, INT};

    private RequestType mType;
    private  Object mData;

    public MyRequest(RequestType  type, Object data){
        mType = type;
        mData = data;
    }

    public RequestType  getRequestType(){
        return mType;
    }

    public Object getData(){
        return mData;
    }

}

