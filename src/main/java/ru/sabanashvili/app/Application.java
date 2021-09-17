package ru.sabanashvili.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sabanashvili.model.Car;
import ru.sabanashvili.model.User;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        //блок с подключением к БД
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/fix_user_db");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "postgres");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        //два способа работы - либо создаем файл .hbm.xml для каждого класса, где указываем правила
        // маппинга - из реляционного соотношения в отношение ООП
        // либо вместо файлов, используем аннотации, как показано в примере с классом Car
        // после чего добавляем аннотируемый класс в конфигурацию хибера
        configuration.addAnnotatedClass(Car.class);
        // настройка для показа sql запросов в БД от хибернэйта
        configuration.setProperty("hibernate.show_sql", "true");
        // благодаря следующей настройки, можно указать новое поле в классе, что представляет
        // таблицу-сущность из БД, и хибернэйт сам проапдейтит нужную таблицу, создав там одноименную колонку
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");



        configuration.addResource("User.hbm.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        // это не язык sql, а язык хибернэйта для оперирования и сущностями ООП, и сущностями БД
        User user = session.createQuery("from User user where user.id = 1", User.class).getSingleResult();
        session.beginTransaction();
        session.save(new User("Mini", "Max", 37));
        session.getTransaction().commit();
        System.out.println(user);

        List<Car> car = session.createQuery("from Car car", Car.class).getResultList();
    }
}
