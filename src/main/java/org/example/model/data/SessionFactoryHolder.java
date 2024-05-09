package org.example.model.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryHolder {
    private final SessionFactory factory;
    private static final SessionFactoryHolder holder = new SessionFactoryHolder(); //stiamo istanziando un oggetto della stessa classe in una variabile statica
    private SessionFactoryHolder(){
        Configuration conf = new Configuration().configure("hibernate.cfg.xml");
        factory = conf.buildSessionFactory();
    }

    public static SessionFactoryHolder getHolder(){
        return holder;
    }

    public void close(){
        factory.close();
    }

    public Session createSession(){
        return factory.openSession();
    }


}
