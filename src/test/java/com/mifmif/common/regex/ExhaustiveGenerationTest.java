/*
 * Copyright 2015 Otmar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mifmif.common.regex;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Otmar
 */
public class ExhaustiveGenerationTest {
    class ConvenienceRegexTesting
        {
            public String regex;
            public Set<String> expectedExamples;
            
            ConvenienceRegexTesting(String regex, List<String> expected){
                this.regex = regex;
                this.expectedExamples = new HashSet<String>(expected);
                this.run();
            }
            
            void run(){
                Generex g = new Generex(this.regex);
                this.actualExamples = g.getAllMatchedStringsViaAllPaths();
                final Set<String> actualSet = actualExamples;
                actuallyAbsent = expectedExamples.stream().filter(expec -> !actualSet.contains(expec)).collect(Collectors.toSet());
            }
            
            
            public Set<String> actualExamples;
            
            public Set<String> actuallyAbsent;
           
        }
        
        @Test
        public void testJavaMopCasesForJavaUtil(){
            
            Set<String> actuallyAbsent;
            ConvenienceRegexTesting test =  new ConvenienceRegexTesting("cu*m+u", Arrays.asList("cmu", "cumu", "cmmu", "cummu"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test =  new ConvenienceRegexTesting("b|(ca)", Arrays.asList("b", "ca"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test =  new ConvenienceRegexTesting("(ab)|(acd)", Arrays.asList("ab", "acd"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test =  new ConvenienceRegexTesting("ab(c|(de))", Arrays.asList("abc", "abde"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test =  new ConvenienceRegexTesting("(elm*)*", Arrays.asList("el", "elm"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("ca+", Arrays.asList("ca", "caa"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("u", Arrays.asList("u"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("(n+(r| ))*", Arrays.asList("nr", "nnr", "nn", "n"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("((n|p)+(r| ))*", Arrays.asList("n", "nn","nr", "nnr", "p","pp", "pr","ppr","np","npr", "pn","pnr"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("ca*", Arrays.asList("c", "ca"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            
            test = new ConvenienceRegexTesting("ca*((n|p)+s*(r|a+| ))*", Arrays.asList("cnnr","cnnaa","cnna","cnn","cnpr","cnpaa","cnpa","cnp","cnsr","cnsaa","cnsa","cns","cnr","cnaa","cna","cn","cpnr","cpnaa","cpna","cpn","cppr","cppaa","cppa","cpp","cpsr","cpsaa","cpsa","cps","cpr","cpaa","cpa","cp","cannr","cannaa","canna","cann","canpr","canpaa","canpa","canp","cansr","cansaa","cansa","cans","canr","canaa","cana","can","capnr","capnaa","capna","capn","cappr","cappaa","cappa","capp","capsr","capsaa","capsa","caps","capr","capaa","capa","cap"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
        }
}
