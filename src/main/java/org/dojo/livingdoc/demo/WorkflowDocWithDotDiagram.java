package org.dojo.livingdoc.demo;


import io.github.livingdocumentation.dotdiagram.DotGraph;
import org.dojo.livingdoc.annotation.ClassDemo;
import org.dojo.livingdoc.annotation.GenerateGraph;

import java.util.Arrays;

/**
 * Show algorithm workflow.
 *
 * Wokflow configuration is defined in code.
 * We extract information to display a graph.
 *
 * We use graphviz to draw the graph and https://github.com/LivingDocumentation/dot-diagram[dot-diagram] to generate dot text.
 * ****
 * [graphviz]
 * ----
 * digraph g {
 *    A -> B
 * }
 * ----
 * ****
 */
@ClassDemo(group = "Execute to get information", label = "Show workflow using dot-diagram")
public class WorkflowDocWithDotDiagram {

    public static void main(String[] args) {
        System.out.println(new WorkflowDocWithDotDiagram().generateWorkflowGraph());
    }

    /**
     * Workflow graph.
     *
     * @return
     * @throws Error
     */
    @GenerateGraph(name="Workflow graph generated using dot-diagram")
    // tag::example[]
    public String generateWorkflowGraph() throws Error {

        final Workflow workflow = new Workflow();

        final DotGraph graph = new DotGraph("");
        final DotGraph.Digraph digraph = graph.getDigraph();
        Arrays.asList(Workflow.State.values()).forEach(state -> addStateTransitions(digraph, workflow, state));

        return String.join("\n",
                "",
                "[graphviz]",
                "----",
                graph.render(),
                "----");
    }

    private void addStateTransitions(DotGraph.Digraph digraph, Workflow workflow, Workflow.State currentState) {
        digraph.addNode(currentState.name()).setLabel(currentState.name());
        workflow.availableTransition(currentState)
                .forEach(availableState -> digraph.addAssociation(currentState.name(), availableState.name()));
    }
    // end::example[]


}


// tag::example[]

// end::example[]