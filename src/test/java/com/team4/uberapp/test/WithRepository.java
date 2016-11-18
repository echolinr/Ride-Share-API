/*
 *
 */

package com.team4.uberapp.test;

import org.junit.rules.ExternalResource;
import com.team4.uberapp.domain.Repositories;
import com.team4.uberapp.persistence.MongoRepositories;
import org.mongolink.test.MongolinkRule;

public class WithRepository extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        mongolinkRule = MongolinkRule.withPackage("com.team4.uberapp.persistence.mapping");
        mongolinkRule.before();
        Repositories.initialise(new MongoRepositories(mongolinkRule.getCurrentSession()));
    }

    @Override
    protected void after() {
        mongolinkRule.after();
    }

    public void cleanSession() {
        mongolinkRule.cleanSession();
    }

    private MongolinkRule mongolinkRule;

}
