package org.dojo.livingdoc.demo;


import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateGraph;
import org.dojo.livingdoc.application.Person;
import org.dojo.livingdoc.application.service.Dao;
import org.dojo.livingdoc.application.service.Notifier;
import org.dojo.livingdoc.application.service.Service;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Display contributors calls.
 *
 * We execute a method and trace every calls to injected services.
 */
@ClassDemo(group = "Execute to get information", label = "Show methods called")
public class CallFlowDoc {

    public static void main(String[] args) {
        System.out.println(new CallFlowDoc().generateCallFlow());
    }

    static class TraceAnswer implements Answer<Object> {
        public List<String> linksPlantUml = new ArrayList<>();
        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();


            final StackTraceElement x = Arrays.stream(stackTrace)
                    .dropWhile(s -> s.getClassName() != invocationOnMock.getMock().getClass().getName())
                    .skip(1)
                    .dropWhile(s -> !s.getClassName().startsWith("org.dojo") )
                    .findFirst().get();

            linksPlantUml.add(plantUmlCall(x, invocationOnMock.getMethod()));

            return invocationOnMock.callRealMethod();
        }

        private String plantUmlCall(StackTraceElement caller, Method methodCalled) {
            return "\"" + getClassName(caller) + "\" -> \""
                    + methodCalled.getDeclaringClass().getSimpleName() + "\": "
                    + methodCalled.getName();
        }


        private String getClassName(StackTraceElement s) {
            try {
                return Class.forName(s.getClassName()).getSimpleName();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private String getSimpleName(StackTraceElement s) {
            return getClassName(s) + "." + s.getMethodName().replaceFirst("^lambda\\$(.*)\\$.*", "$1");
        }

        private String getSimpleName(Method method) {
            return method.getDeclaringClass().getSimpleName() + "." + method.getName();
        }

    }

    /**
     * Generate sequence diagram.
     *
     * @return
     * @throws Error
     */
    @GenerateGraph(name = "Call flow generated")
    // tag::example[]
    public String generateCallFlow() throws Error {

        final TraceAnswer recordCalls = new TraceAnswer();
        final Notifier notifier = spyWithTracer(new NotifierImpl(), recordCalls);
        final Dao dao = spyWithTracer(new DaoImpl(notifier), recordCalls);

        // Call method to trace
        final Service service = new Service(dao, notifier);
        service.findHomonyms(5);

        return String.join("\n",
                        "",
                        ".Calls from Service.findHomonyms method",
                        "[plantuml]",
                        "----",
                        recordCalls.linksPlantUml.stream()
                                .collect(Collectors.joining("\n")),
                        "----");
    }

    /**
     * Create a spy over a object to trace every methods called.
     * @param instance
     * @param recordCalls
     * @param <T>
     * @return
     */
    private <T> T spyWithTracer(T instance, TraceAnswer recordCalls) {
        return Mockito.mock((Class<T>) instance.getClass(),
                Mockito.withSettings()
                        .spiedInstance(instance)
                        .defaultAnswer(recordCalls));
    }
    // end::example[]


}

class DaoImpl implements Dao {
    private final Notifier notifier;

    DaoImpl(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public Optional<Person> findById(long id) {
        notifier.send("Search with id: " + id);

        return Optional.of(new Person("John", "Doe"));
    }

    @Override
    public List<Person> searchWithName(String lastName) {
        notifier.send("Search with name: " + lastName);
        return List.of(
                new Person("Martin", "Doe"),
                new Person("Kevin", "Doe")
        );
    }
}

class NotifierImpl implements Notifier {


    @Override
    public void send(String message) {
//            System.out.println(message);
    }
}
