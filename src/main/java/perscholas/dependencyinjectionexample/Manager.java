package perscholas.dependencyinjectionexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component //Starts looking for any classes that have certain annotations
public class Manager {

    // first spring creates its context where it wil put all objects that it is going to create
    // the context is like a bucket where it puts objects that it creates, and we can ask for them
    // later with @Autowired. spring makes 3 passes when it starts up

    // first pass is what's called a component scan. This finds all spring classes with ana annotation
    /// such as @Controller, @Component, @Repository and creates an instance of this class. Essentially
    // it creates a new object of the annotated class

    // second pass is when it does all @Autowired. This is done in a second pass because all objects
    // need to be created before they can be autowired

    // WARNING : In spring you can not use constructors to initialize the class if it is using an
    // autowired variable.  This is because on pass 1 when it is creating the instances of all
    // classes, it has not yet autowired the variables.

    // the third pass that spring wil make runs all @PostConstruct methods.

    @Autowired
    private Worker1 worker1;

    @Autowired
    private Worker2 worker2;

    @Autowired
    private Worker3 worker3;

    // this is the same way of doing it as the @Autowired Worker3 above
    // this is more prevalent in test driven design applications
    //    @Autowired
    //    public void setFooFormatter(Worker3 w3) {
    //        this.worker3 = w3;
    //    }

    public Manager () {
        System.out.println("I am the manager constructor");
        // this executes before autowired
        // these lines of code iwl fail due to spring has not yet autowired the variables
        // it is not a good practice to use a constructor with sprig component for this reason
//        worker1.doWork();
//        worker2.doWork();
//        worker3.doWork();
    }

    @PostConstruct
    public void init() {
        worker1.doWork();
        worker2.doWork();
        worker3.doWork();
    }
}
