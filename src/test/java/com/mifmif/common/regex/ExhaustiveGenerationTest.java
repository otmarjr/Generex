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
            }
            
            
            public Set<String> actualExamples;
        }
        
        @Test
        public void testJavaMopCasesForJavaUtil(){
            
            Set<String> actuallyAbsent;
            ConvenienceRegexTesting test =  new ConvenienceRegexTesting("cu*m+u", Arrays.asList("cmu", "cumu", "cmmu", "cummu"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test =  new ConvenienceRegexTesting("b|(ca)", Arrays.asList("b", "ca"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test =  new ConvenienceRegexTesting("(ab)|(acd)", Arrays.asList("ab", "acd"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test =  new ConvenienceRegexTesting("ab(c|(de))", Arrays.asList("abc", "abde"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test =  new ConvenienceRegexTesting("(elm*)*", Arrays.asList("el", "elm"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test = new ConvenienceRegexTesting("ca+", Arrays.asList("ca", "caa"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test = new ConvenienceRegexTesting("u", Arrays.asList("u"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test = new ConvenienceRegexTesting("(n+(r| ))*", Arrays.asList("nr", "nnr", "nn", "n"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test = new ConvenienceRegexTesting("((n|p)+(r| ))*", Arrays.asList("n", "nn","nr", "nnr", "p","pp", "pr","ppr","np","npr", "pn","pnr"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            test = new ConvenienceRegexTesting("ca*", Arrays.asList("c", "ca"));
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
            
            
            test = new ConvenienceRegexTesting("ca*((n|p)+s*(r|a+| ))*", Arrays.asList("cnnra","cnnr","cnnaa","cnna","cnn","cnpra","cnpr","cnpaa","cnpa","cnp","cnsra","cnsr","cnsaa","cnsa","cns","cnra","cnr","cnaa","cna","cn","cpnra","cpnr","cpnaa","cpna","cpn","cppra","cppr","cppaa","cppa","cpp","cpsra","cpsr","cpsaa","cpsa","cps","cpra","cpr","cpaa","cpa","cp","cannra","cannr","cannaa","canna","cann","canpra","canpr","canpaa","canpa","canp","cansra","cansr","cansaa","cansa","cans","canra","canr","canaa","cana","can","capnra","capnr","capnaa","capna","capn","cappra","cappr","cappaa","cappa","capp","capsra","capsr","capsaa","capsa","caps","capra","capr","capaa","capa","cap"));
            final Set<String> actualSet = test.actualExamples;
            
            actuallyAbsent = test.expectedExamples.stream().filter(expec -> !actualSet.contains(expec)).collect(Collectors.toSet());
            Assert.assertTrue(actuallyAbsent.isEmpty());
            Assert.assertEquals(test.expectedExamples.size(),test.actualExamples.size());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            Assert.assertTrue(test.expectedExamples.containsAll(test.actualExamples));
        }
}
