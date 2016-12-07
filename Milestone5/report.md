#Milestone 5 Status Report
by Hector and Lin

## Code Sharing
1. Jmeter load test plan  is the <code>load_test.jms</code> file under folder Milestone5 (https://github.com/hectorguo/uber-rest-api/tree/master/Milestone5).
2. Aggregate Graph for 100 instance at 60 seconds interval: https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Aggregate_Graph_100_60.png 
3. Response Time Graph for 100 instance at 60 seconds interval: https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Response_Time_Graph_100_60.png
4. Response Time Graph for 100 instance at 30 seconds interval: https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Response%20Time%20Graph-100-30.png
5. Response Time Graph for 100 instance at 30 seconds interval:https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Aggregate%20Graph-100-30.png
6. Response Time Graph for 100 instance at 15 seconds interval:https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Response%20Time%20Graph-100-15.png
7. Response Time Graph for 100 instance at 15 seconds interval:https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/Aggregate%20Graph-100-15.png
8. Full test results (in CVS format) are <B> uploaded </B> respectively as <code>lt-100-15.tar.gz</code>, <code>lt-100-30.tar.gz</code> and <code>lt-100-60.tar.gz</code>.

## Hours Spent: Hector 5 hours, Lin 10 hours

## Challenges 
1. Our program does have performance issues during load test. Not all cases can pass when the load adds up.
2. Sometimes, 'Post' would fail; sometimes, jmeter throws http exception. We suspect it may be caused by Spark server.
3. At first, we thought it's not hard, but we did not forsee the risks of not being able to pass heavy load tests.
4. If code needs improvement, we need more time to do that.

## Grading Criteria

| Component | Weight | Report |
|-----|------:|-------|
| Create jMeter Plans for each existing callback | 65 | All required use cases are implemented (see test plan <code>load_test.jms</code>) |
| Serve the existing API and DB in a Google Compute Engine Instance | 20 | We use 1 GCE instance to execute jar file and host MongoDB, another instance to run JMeter. GCE console screenshots are also uploaded [here](https://github.com/hectorguo/uber-rest-api/blob/master/Milestone5/GCE-VM.png). |
| Individual Contribution | 10 | 50/50
| Status report | 5 | current file


