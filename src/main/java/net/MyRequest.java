package net;

import java.io.Serializable;

//класс для отправки обьектов с инструкциями на сервер
public class MyRequest implements Serializable {

    //перечисление возможных типов передаваемых данных
    public static enum RequestType {CREATE, READ, UPDATE, DELETE, ANSWER};
    public static enum RequestTypeB {USER, CAR, LIST_SIGN_IN, LIST_NEW_PRODUCT, LIST_MY_PRODUCT, LIST_FIND_SELLER,
        LIST_MY_ORDERS, CONFIRM_ORDER, INT, ORDER};

    private RequestType mType;
    private RequestTypeB mTypeB;
    private Object mData;
    private Object mDataB;

    public MyRequest(RequestType  type, RequestTypeB  typeB, Object data){
        mType = type;
        mTypeB = typeB;
        mData = data;
    }

    public MyRequest(RequestType type, RequestTypeB typeB, Object data, Object dataB) {
        mType = type;
        mTypeB = typeB;
        mData = data;
        mDataB = dataB;
    }

    public RequestType  getRequestType(){
        return mType;
    }

    public RequestTypeB  getRequestTypeB(){
        return mTypeB;
    }

    public Object getData(){
        return mData;
    }

    public Object getDataB() {
        return mDataB;
    }
}