package com.team4.uberapp.userSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.uberapp.MongoConfiguration;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import com.team4.uberapp.util.UberAppUtil;
import org.mongolink.MongoSession;
import org.mongolink.domain.criteria.Criteria;
import org.mongolink.domain.criteria.Order;
import spark.Route;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UserSessionController extends UberAppUtil {
    // GET /cars  Get all cars
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

    // POST /usersessions  Create usersessions
    public static Route create = (req, res) -> {
        /* initialize db connection */
        MongoSession session = MongoConfiguration.createSession();
        session.start();
        Repositories.initialise(new MongoRepositories(session));

        try {
            ObjectMapper mapper = new ObjectMapper();
            UserSession userSession = mapper.readValue(req.body(), UserSession.class);
            //userSession.setId(UUID.randomUUID());

            try {
                userSession.isValid();
            } catch (Exception e){
                res.status(400);
                return dataToJson(e.getMessage());
            }

            Repositories.userSessions().add(userSession);

            // close database connection
            session.stop();

            //prepare return result
            res.type("application/json");
            res.status(200);
            return dataToJson(userSession);
        }  catch (Exception e){
            session.stop();
            res.type("application/json");
            res.status(400);
            return dataToJson(e.getMessage());
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
}
