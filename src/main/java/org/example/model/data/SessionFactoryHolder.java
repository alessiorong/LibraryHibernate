package org.example.model.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryHolder {

    private final SessionFactory factory;
    //private static SessionFactoryHolder holder = new SessionFactoryHolder();
    private static SessionFactoryHolder holder;

    private SessionFactoryHolder() {
        Configuration conf = new Configuration().configure("hibernate.cfg.xml");
        factory = conf.buildSessionFactory();
    }

//    public static SessionFactoryHolder getHolder(){
//        return holder;
//    }

    public static SessionFactoryHolder getHolder(){
        if(holder == null){
            holder = new SessionFactoryHolder();
        }
        return holder;
    }

    public void close(){
        factory.close();
    }

    public Session createSession(){
        return factory.openSession();
    }

    public static void veryUsefulMethod(){

    }
}
