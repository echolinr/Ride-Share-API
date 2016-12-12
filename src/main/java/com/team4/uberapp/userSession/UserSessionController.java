/**
 * UserSession route for user authentication
 * POST /sessions?email, passord for real authentication
 * GET /sessions for debug purpose
 *
 * @author  Lin Zhai
 * @version 0.1
 */
package com.team4.uberapp.userSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.driver.Driver;
import com.team4.uberapp.passenger.Passenger;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Order;
import org.mongolink.domain.criteria.Restrictions;
import spark.Route;

import java.util.*;

/**
 * The type User session controller.
 */
public class UserSessionController extends UberAppUtil {
    /**
     * GET /sessions  Get all sessions
     * @return List<Session> a list of sessions
     */
    public static Route getAll = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        List<UserSession> userSessions;

        if (req.queryParams().isEmpty()) {
            userSessions = Repositories.userSessions().all();
        } else {
            Criteria criteria = session.createCriteria(UserSession.class); // create criteria object
            final List<String> queryFields = Arrays.asList("count", "offsetId", "sort", "sortOrder");
            Set<String> queryParams = req.queryParams();
            //StringBuilder str = new StringBuilder();
            String querySort = null;
            String querySortOrder = null;
            //str.append("Request Parameters are <br/>");
            for(String param : queryParams){
                //str.append(param).append(" ").append(req.queryParams(param)).append("<br />"); // build debug message
                if (!queryFields.contains(param)) {
                    session.stop();
                    res.status(200);
                    res.type("applicaiton/json");
                    return dataToJson("Wrong query params :" + param);
                }
                if (param.compareTo("count") == 0)  {
                    criteria.limit(Integer.parseInt(req.queryParams(param)));
                } else if (param.compareTo("offsetId") == 0) {
                    criteria.skip(Integer.parseInt(req.queryParams(param)));
                } else if (param.equalsIgnoreCase("sort") == true){
                    querySort = new String(req.queryParams(param));
                } else if (param.equalsIgnoreCase("sortOrder") == true) {
                    querySortOrder = new String(req.queryParams(param));
                }
            }
            // setup sort and sortOrder
            if (querySort != null && querySortOrder != null) {
                if (querySortOrder.equalsIgnoreCase("asc") == true) {
                    criteria.sort(querySort, Order.ASCENDING);
                } else {
                    criteria.sort(querySort, Order.DESCENDING);
                }
            } else if (((querySort != null) && (querySortOrder == null)) ||
                    ((querySort == null) && (querySortOrder != null)) ){
                session.stop();
                res.status(200);
                res.type("applicaiton/json");
                return dataToJson("sort & sortOrder params must be in pair.");
            }
            userSessions = criteria.list();
        }

        // close database connection
        session.stop();

        res.status(200);
        res.type("application/json");
        if (userSessions.size() == 0) {
            return dataToJson("No user session");
        } else {
            return dataToJson(userSessions);
        }

    };

    /**
     * POST /sessions  Create sessions
     * {
     * "email": "hectorguo@gmail.com",
     * "password": "123456"
     * }
     * @return Seesion  one session with id
     */
    public static Route create = (req, res) -> {
        Driver driver;
        Passenger passenger;
        String userId;

        /* initialize db connection */
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));
        UserSession userSession;
        try {
            Map<String, String> token = new HashMap<String, String>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                userSession = mapper.readValue(req.body(), UserSession.class);
            } catch (Exception e) {
                res.status(400);
                res.type("application/json");
                return dataToJson(e.getMessage());
            }
            try {
                userSession.isValid();
            } catch (Exception e){
                res.status(400);
                res.type("application/json");
                return dataToJson(e.getMessage());
            }

            // find driver by email address
            {
                Criteria criteria = session.createCriteria(Driver.class); // create criteria object
                criteria.add(Restrictions.equals("emailAddress", userSession.getEmail()));
                // emailAddress for driver must be unique
                if (criteria.list() != null && !criteria.list().isEmpty()) {
                    driver = (Driver)(criteria.list().get(0));
                    //check password
                    if (checkPassword(userSession.getPassword(),driver.getPassword())) {
                        // cleanup session
                        session.clear();
                        userSession.setId(UUID.randomUUID());
                        // hash password
                        userSession.setPassword(hashPassword(userSession.getPassword()));
                        // generate session token
                        userSession.setToken(createToken(driver.getId().toString(),"Driver"));

                        // store session， just for testing purpose, we don't need really put it into db
                        // will remove this part later
                        if(validTokenUser(userSession.getToken()) != null) {
                            Repositories.userSessions().add(userSession);
                        }

                        // generate return
                        session.stop();
                        res.status(201);
                        res.type("application/json");
                        token.put("token",userSession.getToken());
                        return dataToJson(token);
                    } else {
                        session.stop();
                        res.status(401);
                        res.type("application/json");
                        return dataToJson("Log in failed. Password failed");
                    }
                }
            }
            // try passenger db
            {
                session.clear();
                Criteria criteria = session.createCriteria(Passenger.class); // create criteria object
                criteria.add(Restrictions.equals("emailAddress", userSession.getEmail()));
                // emailAddress for driver must be unique
                if (criteria.list() != null && !criteria.list().isEmpty()) {
                    passenger = (Passenger)(criteria.list().get(0));
                    //check password
                    if (checkPassword(userSession.getPassword(),passenger.getPassword())) {
                        // cleanup session
                        session.clear();
                        userSession.setId(UUID.randomUUID());
                        // hash password
                        userSession.setPassword(hashPassword(userSession.getPassword()));
                        // generate session token
                        userSession.setToken(createToken(passenger.getId().toString(), "Passenger"));

                        // store session， just for testing purpose, we don't need really put it into db
                        // will remove this part later
                        if(validTokenUser(userSession.getToken()) != null) {
                        // store session
                             Repositories.userSessions().add(userSession);
                        }

                        // generate return
                        session.stop();
                        res.status(201);
                        res.type("application/json");
                        token.put("token",userSession.getToken());
                        return dataToJson(token);
                    } else {
                        session.stop();
                        res.status(401);
                        res.type("application/json");
                        return dataToJson("Log in failed. Password failed");
                    }
                }
            }
            session.stop();
            res.status(401);
            res.type("application/json");
            return dataToJson("Log in failed. No user found");
        }  catch (Exception e){
            session.stop();
            res.type("application/json");
            res.status(400);
            return dataToJson(e.getMessage());
        }
    };

/*
    // GET /usersessions/:id  Get usersessions by id
    public static Route getById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        //UUID uid = UUID.fromString(req.params(":id"));
        UserSession userSession = Repositories.userSessions().get(req.params(":id"));

        // close database connection
        session.stop();

        res.type("application/json");
        if (userSession == null) {
            res.status(404); // 404 Not found
            return dataToJson("UserSession: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return dataToJson(userSession);
        }
    };

    // DELETE /cars/:id  Delete car by id
    public static Route delById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        //UUID uid = UUID.fromString(req.params(":id"));
        UserSession userSession = Repositories.userSessions().get(req.params(":id"));

        res.type("application/json");
        if (userSession == null) {
            res.status(404); // 404 Not found
            // close database connection
            session.stop();
            return dataToJson("UserSession: " + req.params(":id") +" not found");
        } else {
            Repositories.userSessions().delete(userSession);
            // close database connection
            session.stop();
            res.status(200);
            return dataToJson("UserSession: " + req.params(":id") +" deleted");
        }
    };
/*
    // GET /usersessions/:id  Get usersessions by id
    public static Route getById = (req, res) -> {
        //initialize db connection
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        // get car by id, generate UUID from string id first
        //UUID uid = UUID.fromString(req.params(":id"));
        UserSession userSession = Repositories.userSessions().get(req.params(":id"));

        // close database connection
        session.stop();

        res.type("application/json");
        if (userSession == null) {
            res.status(404); // 404 Not found
            return dataToJson("UserSession: " + req.params(":id") +" not found");
        } else {
            res.status(200);
            return dataToJson(userSession);
        }
    };
*/
}
