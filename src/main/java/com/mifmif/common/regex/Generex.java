/**
 * Copyright 2014 y.mifrah
 *
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mifmif.common.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mifmif.common.regex.util.Iterable;
import com.mifmif.common.regex.util.Iterator;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Java utility class that help generating string values that match a given
 * regular expression.It generate all values that are matched by the Regex, a
 * random value, or you can generate only a specific string based on it's
 * lexicographical order .
 *
 * @author y.mifrah
 *
 */
public class Generex implements Iterable {

    private Map<String, String> predefinedCharacterClasses = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("\\\\d", "[0-9]");
            put("\\\\D", "[^0-9]");
            put("\\\\s", "[ \t\n\f\r]");
            put("\\\\S", "[^ \t\n\f\r]");
            put("\\\\w", "[a-zA-Z_0-9]");
            put("\\\\W", "[^a-zA-Z_0-9]");
        }
    };

    public Generex(String regex) {
        for (String key : predefinedCharacterClasses.keySet()) {
            regex = regex.replaceAll(key, predefinedCharacterClasses.get(key));
        }
        regExp = new RegExp(regex);
        automaton = regExp.toAutomaton();
    }

    public Generex(Automaton automaton) {
        this.automaton = automaton;
    }

    private RegExp regExp;
    private Automaton automaton;
    private List<String> matchedStrings = new ArrayList<String>();
    private Node rootNode;
    private boolean isTransactionNodeBuilt;

    /**
     * @param indexOrder ( 1<= indexOrder <=n) @ return The matched string by
     * the given pattern in the given it's order in the sorted list of matched
     * String.<br> <code>indexOrder</code> between 1 and <code>n</code> where
     * <code>n</code> is the number of matched String.<br>
     * If indexOrder >= n , return an empty string. if there is an infinite
     * number of String that matches the given Regex, the method throws
     * {@code StackOverflowError}
     */
    public String getMatchedString(int indexOrder) {
        buildRootNode();
        if (indexOrder == 0) {
            indexOrder = 1;
        }
        String result = buildStringFromNode(rootNode, indexOrder);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    private String buildStringFromNode(Node node, int indexOrder) {
        String result = "";
        long passedStringNbr = 0;
        long step = node.getNbrMatchedString() / node.getNbrChar();
        for (char usedChar = node.getMinChar(); usedChar <= node.getMaxChar(); ++usedChar) {
            passedStringNbr += step;
            if (passedStringNbr >= indexOrder) {
                passedStringNbr -= step;
                indexOrder -= passedStringNbr;
                result = result.concat("" + usedChar);
                break;
            }
        }
        long passedStringNbrInChildNode = 0;
        if (result.length() == 0) {
            passedStringNbrInChildNode = passedStringNbr;
        }
        for (Node childN : node.getNextNodes()) {
            passedStringNbrInChildNode += childN.getNbrMatchedString();
            if (passedStringNbrInChildNode >= indexOrder) {
                passedStringNbrInChildNode -= childN.getNbrMatchedString();
                indexOrder -= passedStringNbrInChildNode;
                result = result.concat(buildStringFromNode(childN, indexOrder));
                break;
            }
        }
        return result;
    }

    /**
     * @return first string in lexicographical order that is matched by the
     * given pattern.
     */
    public String getFirstMatch() {
        buildRootNode();
        Node node = rootNode;
        String result = "";
        while (node.getNextNodes().size() > 0) {
            result = result.concat("" + node.getMinChar());
            node = node.getNextNodes().get(0);
        }
        result = result.substring(1);
        return result;
    }

    /**
     * @return the number of strings that are matched by the given pattern.
     */
    public long matchedStringsSize() {
        return rootNode.getNbrMatchedString();
    }

    /**
     * Prepare the rootNode and it's child nodes so that we can get
     * matchedString by index
     */
    private void buildRootNode() {
        if (isTransactionNodeBuilt) {
            return;
        }
        isTransactionNodeBuilt = true;
        rootNode = new Node();
        rootNode.setNbrChar(1);
        List<Node> nextNodes = prepareTransactionNodes(automaton.getInitialState());
        rootNode.setNextNodes(nextNodes);
        rootNode.updateNbrMatchedString();
    }

    private int matchedStringCounter = 0;

    private void generate(String strMatch, State state, int limit, Set<Transition> visitedTransitions) {
        if (matchedStringCounter == limit) {
            return;
        }
        ++matchedStringCounter;
        List<Transition> transitions = state.getSortedTransitions(true);
        if (transitions.size() == 0) {
            matchedStrings.add(strMatch);
            return;
        }
        if (state.isAccept()) {
            matchedStrings.add(strMatch);
        }
        for (Transition transition : transitions) {
            for (char c = transition.getMin(); c <= transition.getMax(); ++c) {
                if (!visitedTransitions.contains(transition)) {
                    visitedTransitions.add(transition);
                    generate(strMatch + c, transition.getDest(), limit, visitedTransitions);
                }
            }
        }
    }

    /**
     * Build list of nodes that present possible transactions from the
     * <code>state</code>.
     *
     * @param state
     * @return
     */
    private List<Node> prepareTransactionNodes(State state) {

        List<Node> transactionNodes = new ArrayList<Node>();
        if (preparedTransactionNode == Integer.MAX_VALUE / 2) {
            return transactionNodes;
        }
        ++preparedTransactionNode;

        if (state.isAccept()) {
            Node acceptedNode = new Node();
            acceptedNode.setNbrChar(1);
            transactionNodes.add(acceptedNode);
        }
        List<Transition> transitions = state.getSortedTransitions(true);
        for (Transition transition : transitions) {
            Node trsNode = new Node();
            int nbrChar = transition.getMax() - transition.getMin() + 1;
            trsNode.setNbrChar(nbrChar);
            trsNode.setMaxChar(transition.getMax());
            trsNode.setMinChar(transition.getMin());
            List<Node> nextNodes = prepareTransactionNodes(transition.getDest());
            trsNode.setNextNodes(nextNodes);
            transactionNodes.add(trsNode);
        }
        return transactionNodes;
    }

    private int preparedTransactionNode;

    /**
     * Generate all Strings that matches the given Regex.
     *
     * @return
     */
    public List<String> getAllMatchedStrings() {
        matchedStrings = new ArrayList<String>();
        generate("", automaton.getInitialState(), Integer.MAX_VALUE, new HashSet<Transition>());
        return matchedStrings;

    }

    public Set<String> getAllMatchedStringsViaAllPaths() {
        GraphFindAllPaths<State> automatonGraph = new GraphFindAllPaths<State>();

        for (State st : automaton.getStates()) {
            automatonGraph.addNode(st);
        }

        Set<State> statesWithSelfLoops = new HashSet<State>();

        for (State st : automaton.getStates()) {
            for (Transition t : st.getSortedTransitions(true)) {
                automatonGraph.addEdge(st, t.getDest(), 1);

                if (t.getDest().equals(st)) {
                    statesWithSelfLoops.add(st);
                }
            }
        }

        FindAllPaths<State> findAllPaths = new FindAllPaths<State>(automatonGraph);

        Set<String> acceptedStrings = new HashSet<String>();
        List<List<State>> allPaths = new LinkedList<List<State>>();
        List<List<State>> newPathsWithSelfLoops = new LinkedList<List<State>>();
        List<List<State>> pathsWithoutCycles = null;

        for (State accst : automaton.getAcceptStates()) {

            if (accst.equals(automaton.getInitialState())) {
                // Choose another accepting state:
                for (State ac2 : automaton.getAcceptStates()) {
                    if (!ac2.equals(accst)) {
                        if (pathsWithoutCycles == null) {
                            pathsWithoutCycles = new LinkedList<List<State>>();
                        }
                        pathsWithoutCycles.addAll(findAllPaths.getAllPaths(automaton.getInitialState(), ac2));
                    }
                }

            } else {
                pathsWithoutCycles = findAllPaths.getAllPaths(automaton.getInitialState(), accst); // must handle initial state as accepting!
            }

            if (pathsWithoutCycles == null) { // Initial state is the only accepting state
                // Exhaust all possibilities of paths!
                pathsWithoutCycles = new LinkedList<List<State>>();
                for (State si : automaton.getStates()) {
                    for (State sj : automaton.getStates()) {
                        if (!si.equals(sj)) {
                            List<List<State>> prefixes = new LinkedList<List<State>>();

                            if (!si.equals(automaton.getInitialState())) {
                                prefixes = findAllPaths.getAllPaths(automaton.getInitialState(), si);
                            }

                            if (sj.isAccept()) {
                                List<List<State>> suffixes = findAllPaths.getAllPaths(si, sj);

                                for (List<State> prefix : prefixes) {
                                    for (List<State> sufix : suffixes) {
                                        List<State> word = new LinkedList<State>();
                                        word.addAll(prefix.subList(0, prefix.size() - 1));
                                        word.addAll(sufix);
                                        pathsWithoutCycles.add(word);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            boolean allStatesInPathWithoutCycle = pathsWithoutCycles.stream().anyMatch(path -> path.containsAll(automaton.getStates()));
            
            while (!allStatesInPathWithoutCycle){
                final List<List<State>> simplePathsEndingInCurrentAccSt;
                simplePathsEndingInCurrentAccSt = new LinkedList<>(pathsWithoutCycles);
                // Acceptance is done before getting to some of the nodes!
                Set<State> statesMissingInPath = automaton.getStates().stream()
                        .filter(s -> simplePathsEndingInCurrentAccSt.stream().allMatch(path -> !path.contains(s)))
                        .collect(Collectors.toSet());
                
                List<List<State>> newCycles = new LinkedList<>();
                
                for (State missingST : statesMissingInPath){
                    List<List<State>> fromAccStToState = findAllPaths.getAllPaths(accst, missingST);
                    List<List<State>> fromMissingToAccst = findAllPaths.getAllPaths(missingST,accst);
                    
                    for (List<State> beforeMissingSt : fromAccStToState){
                        for (List<State> backToAccSt : fromMissingToAccst){
                            List<State> cycleAfterAccstCoveringMissingSt;
                            cycleAfterAccstCoveringMissingSt = new LinkedList<>();
                            cycleAfterAccstCoveringMissingSt.addAll(beforeMissingSt.subList(0, beforeMissingSt.size()-1));
                            cycleAfterAccstCoveringMissingSt.addAll(backToAccSt);
                            newCycles.add(cycleAfterAccstCoveringMissingSt);
                        }
                    }
                }
                
                List<List<State>> newSimplePaths = new LinkedList<>();
                
                for (List<State> path : pathsWithoutCycles){
                    for (List<State> cycle : newCycles){
                        List<State> newCyclicPath = new LinkedList<>();
                        newCyclicPath.addAll(path.subList(0, path.size()-1));
                        newCyclicPath.addAll(cycle);
                        newSimplePaths.add(newCyclicPath);
                    }
                }
                pathsWithoutCycles.addAll(newSimplePaths);
                
                allStatesInPathWithoutCycle = pathsWithoutCycles.stream().anyMatch(path -> path.containsAll(automaton.getStates()));
            }

            /*
             Expand allPaths with one loop iteration occuring.
             */
            for (List<State> path : pathsWithoutCycles) {
                List<State> cyclicStatesToBeAddedInThisPath = new LinkedList<State>();

                for (State swl : statesWithSelfLoops) {
                    if (path.contains(swl)) {
                        cyclicStatesToBeAddedInThisPath.add(swl);
                    }
                }

                if (!cyclicStatesToBeAddedInThisPath.isEmpty()) {
                    // We already have path without loops. Now, we enumerate
                    // to incorporate all possible combinations!
                    int C = (int) Math.pow(2, cyclicStatesToBeAddedInThisPath.size());

                    List<State> statesToBeAdded = new LinkedList<State>();

                    for (int i = 0; i < C; i++) {
                        for (int j = 0; j < cyclicStatesToBeAddedInThisPath.size(); j++) {
                            if (BigInteger.valueOf(i).testBit(j)) { // j-th position of permutation C indicates if this state should participate!
                                statesToBeAdded.add(cyclicStatesToBeAddedInThisPath.get(j));
                            }
                        }

                        if (!statesToBeAdded.isEmpty()) {
                            List<State> newPathWithSelfLoops = new LinkedList<State>(path);

                            while (!statesToBeAdded.isEmpty()) {
                                State sl = statesToBeAdded.get(0);
                                int matches =0;
                                List<Integer> toInsertIndexes = new LinkedList<>();
                                
                                for (int k=0;k<newPathWithSelfLoops.size();k++){
                                    if (newPathWithSelfLoops.get(k).equals(sl)){
                                        toInsertIndexes.add(k+matches);
                                        matches++;
                                    }
                                }
                                
                                toInsertIndexes.stream().forEach((k) -> {
                                    newPathWithSelfLoops.add(k, sl);
                                });
                                
                                statesToBeAdded.remove(sl);
                            }

                            newPathsWithSelfLoops.add(newPathWithSelfLoops);
                        }
                    }
                }

            }

            allPaths.addAll(pathsWithoutCycles);
            allPaths.addAll(newPathsWithSelfLoops);
        }

        for (List<State> path : allPaths) {

            State previousState = null;
            List<String> previousStateExpressions = null;

            List<String> expressions = new LinkedList<String>();

            for (State s : path) {
                if (previousState != null) {
                    expressions = new LinkedList<String>(previousStateExpressions);

                    List<String> newlyAddedExpressions = new LinkedList<String>(); // tracks down which expresions were created between previous state and the current one

                    for (Transition t : previousState.getTransitions()) {
                        if (t.getDest().equals(s)) {

                            if (expressions.isEmpty()) {
                                String expr = Character.toString(t.getMin()).trim();
                                newlyAddedExpressions.add(expr);
                                expressions.add(expr);
                            } else {
                                expressions.clear();
                                if (!newlyAddedExpressions.isEmpty()) {
                                    expressions.addAll(newlyAddedExpressions);
                                }

                                if (previousStateExpressions != null && previousStateExpressions.isEmpty()) {
                                    String expr = Character.toString(t.getMin()).trim();
                                    //newlyAddedExpressions.add(expr);
                                    expressions.add(expr);
                                } else {
                                    for (String prevExpr : previousStateExpressions) {
                                        String newExp = prevExpr;
                                        newExp += Character.toString(t.getMin()).trim();
                                        newlyAddedExpressions.add(newExp);
                                        expressions.add(newExp);
                                    }
                                }

                            }
                        }
                    }

                }

                previousState = s;
                previousStateExpressions = expressions;

            }
            acceptedStrings.addAll(expressions);
        }
        return acceptedStrings;
    }

    /**
     * Generate subList with a size of <code>limit</code> of Strings that
     * matches the given Regex. the Strings are ordered in lexicographical
     * order.
     *
     * @param limit
     * @return
     */
    public List<String> getMatchedStrings(int limit) {
        matchedStrings = new ArrayList<String>();
        generate("", automaton.getInitialState(), limit, new HashSet<Transition>());
        return matchedStrings;

    }

    /**
     * Generate and return a random String that match the pattern used in this
     * Generex.
     *
     * @return
     */
    public String random() {
        return prepareRandom("", automaton.getInitialState(), 1, Integer.MAX_VALUE);
    }

    /**
     * Generate and return a random String that match the pattern used in this
     * Generex, and the string has a length >= <code>minLength</code>
     *
     * @param minLength
     * @return
     */
    public String random(int minLength) {
        return prepareRandom("", automaton.getInitialState(), minLength, Integer.MAX_VALUE);
    }

    /**
     * Generate and return a random String that match the pattern used in this
     * Generex, and the string has a length >= <code>minLength</code> and <=
     * <code>maxLength</code>
     *
     *
     * @param minLength
     * @param maxLength
     * @return
     */
    public String random(int minLength, int maxLength) {
        return prepareRandom("", automaton.getInitialState(), minLength, maxLength);
    }

    private String prepareRandom(String strMatch, State state, int minLength, int maxLength) {
        List<Transition> transitions = state.getSortedTransitions(false);

        if (state.isAccept()) {
            if (strMatch.length() == maxLength) {
                return strMatch;
            }
            if (Math.random() > 0.7 && strMatch.length() >= minLength) {
                return strMatch;
            }
        }
        if (transitions.size() == 0) {
            return strMatch;
        }
        Random random = new Random();
        Transition randomTransition = transitions.get(random.nextInt(transitions.size()));
        int diff = randomTransition.getMax() - randomTransition.getMin();
        int randomOffset = diff;
        if (diff > 0) {
            randomOffset = (int) (random.nextInt(diff));
        }
        char randomChar = (char) (randomOffset + randomTransition.getMin());
        return prepareRandom(strMatch + randomChar, randomTransition.getDest(), minLength, maxLength);

    }

    public Iterator iterator() {
        return new GenerexIterator(automaton.getInitialState());

    }

}
