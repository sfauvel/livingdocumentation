package org.dojo.livingdoc.demo;


import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateGraph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dojo.livingdoc.demo.Workflow.State.*;

/**
 * Show algorithm workflow.
 *
 * Wokflow configuration is defined in code.
 * We extract information to display a graph.
 *
 * We use graphviz to draw the graph.
 * ****
 * [graphviz]
 * ----
 * digraph g {
 *    A -> B
 * }
 * ----
 * ****
 */
@ClassDemo
public class WorkflowDoc {

    public static void main(String[] args) {
        System.out.println(new WorkflowDoc().generateWorkflowGraph());
    }

    /**
     * Workflow graph.
     *
     * @return
     * @throws Error
     */
    @GenerateGraph(name="Workflow graph generated")
    // tag::example[]
    public String generateWorkflowGraph() throws Error {

        final Workflow workflow = new Workflow();

        return String.join("\n",
                "",
                "[graphviz]",
                "----",
                "digraph g {",
                Arrays.stream(Workflow.State.values())
                        .flatMap(state -> formatStateLinks(workflow, state))
                        .collect(Collectors.joining("\n")),
                "",
                "}",
                "----");
    }

    private Stream<String> formatStateLinks(Workflow workflow, Workflow.State currentState) {
        return workflow.availableTransition(currentState).stream()
                .map(availableState -> currentState + " -> " + availableState);
    }
    // end::example[]


}


// tag::example[]

/**
 * Class in application that defined workflow.
 */
class Workflow {

    public enum State {
        OPEN, RESOLVED, IN_PROGRESS, CLOSED, REOPENED;
    }

    public List<State> availableTransition(State state) {

        switch (state) {
            case OPEN:
                return Arrays.asList(RESOLVED, IN_PROGRESS, CLOSED);
            case RESOLVED:
                return Arrays.asList(REOPENED, CLOSED);
            case CLOSED:
                return Arrays.asList();
            case IN_PROGRESS:
                return Arrays.asList(OPEN, RESOLVED);
            case REOPENED:
                return Arrays.asList(CLOSED, IN_PROGRESS);
            default:
                return Arrays.asList();

        }
    }
}
// end::example[]