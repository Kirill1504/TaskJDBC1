package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static UserDao daoHibernateInstance = null;
    private static SessionFactory sessionFactory;

    private UserDaoHibernateImpl() {

    }

    private UserDaoHibernateImpl(SessionFactory sessionFactory) {
        UserDaoHibernateImpl.sessionFactory = sessionFactory;
    }

    public static UserDao getDaoHibernateInstance() {
        if (daoHibernateInstance == null) {
            daoHibernateInstance = new UserDaoHibernateImpl(Util.getSessionFactory());
        }
        return daoHibernateInstance;
    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();

        try {
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users(id BIGINT NOT NULL AUTO_INCREMENT" +
                    ", name VARCHAR(60)" +
                    ", lastName VARCHAR(60)" +
                    ", age TINYINT" +
                    ", PRIMARY KEY (id))");
        } finally {
            session.close();
        }

    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();

        try {
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();

                try {
                    session.save(new User(name, lastName, age));
                    session.getTransaction().commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }

    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();

                try {
                    User user = (User) session.load(User.class, id);
                    session.delete(user);
                    session.getTransaction().commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }

        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        List<User> list = new ArrayList<>();
        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();

                try {
                    list = session.createQuery("FROM User").list();
                    session.getTransaction().commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();

                try {
                    session.createQuery("DELETE FROM User").executeUpdate();
                    session.getTransaction().commit();
                } catch (Exception e) {
                    transaction.rollback();
                }
            }
        } finally {
            session.close();
        }
    }
}
