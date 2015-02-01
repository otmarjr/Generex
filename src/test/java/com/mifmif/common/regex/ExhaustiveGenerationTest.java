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

import dk.brics.automaton.Transition;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
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
                this.actualExamples = g.getAllMatchedStringsViaStatePermutations();
                
                final Set<String> actualSet = actualExamples;
                actuallyAbsent = expectedExamples.stream().filter(expec -> !actualSet.contains(expec)).collect(Collectors.toSet());
            }
            
                
            public Set<String> actualExamples;
            
            public Set<String> actuallyAbsent;
           
        }
        
        @Test
        public void testJavaMopCasesForJavaUtil(){
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
            
            test = new ConvenienceRegexTesting("ga+", Arrays.asList("ga","gaa"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("g(m|c)*iu*(m|c)+u", Arrays.asList("gimu","gicu","gimmu","giccu","gimcu","gicmu","giumu","giucu","giummu","giuccu","giumcu","giucmu","gcimu","gcicu","gcimmu","gciccu","gcimcu","gcicmu","gciumu","gciucu","gciummu","gciuccu","gciumcu","gciucmu","gmimu","gmicu","gmimmu","gmiccu","gmimcu","gmicmu","gmiumu","gmiucu","gmiummu","gmiuccu","gmiumcu","gmiucmu"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("(elm*)*", Arrays.asList("el","elm"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("c(x|y)*(g|h)(x|y|z)*iu*(x|y|z)+u", Arrays.asList("cgixu","chixu","cgiyu","chiyu","cgizu","chizu","cxgixu","cxhixu","cxgiyu","cxhiyu","cxgizu","cxhizu","cygixu","cyhixu","cygiyu","cyhiyu","cygizu","cyhizu","cgixu","chixu","cgiyu","chiyu","cgizu","chizu","cxgixu","cxhixu","cxgiyu","cxhiyu","cxgizu","cxhizu","cygixu","cyhixu","cygiyu","cyhiyu","cygizu","cyhizu","cgxixu","chxixu","cgxiyu","chxiyu","cgxizu","chxizu","cxgxixu","cxhxixu","cxgxiyu","cxhxiyu","cxgxizu","cxhxizu","cygxixu","cyhxixu","cygxiyu","cyhxiyu","cygxizu","cyhxizu","cgyixu","chyixu","cgyiyu","chyiyu","cgyizu","chyizu","cxgyixu","cxhyixu","cxgyiyu","cxhyiyu","cxgyizu","cxhyizu","cygyixu","cyhyixu","cygyiyu","cyhyiyu","cygyizu","cyhyizu","cgzixu","chzixu","cgziyu","chziyu","cgzizu","chzizu","cxgzixu","cxhzixu","cxgziyu","cxhziyu","cxgzizu","cxhzizu","cygzixu","cyhzixu","cygziyu","cyhziyu","cygzizu","cyhzizu"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            
            test = new ConvenienceRegexTesting("c(m|n)*(g|h)u*(m|n)+u", Arrays.asList("cmhmnu","cmmgunmu","cmgummu","cnguumnu","cmnhuumu","cmnhmu","cngumu","cnmhmmu","cmmhuummu","cmmhmu","cnhmnu","cngunnu","cnhunmu","cnnhuunu","cmmguunnu","cnhmu","cmguummu","cgummu","cnmguumu","cnmhunnu","cnhunnu","cnhuumu","cgmu","cmgunu","cnmhuunu","chumu","cnnhuummu","chnu","cmhuumnu","cmhnu","cgunnu","cnguumu","cmnguunmu","cnhumu","cnhuummu","cnhnu","cnhunu","cnmhunu","chunu","chmu","cnmgmmu","chunnu","chummu","cmnhumu","cmmgnu","cmmhnnu","chmnu","cmnhumnu","chuunu","cguumu","cmhunu","cgnmu","cngnu","cnmgunu","cmmgumu","cgumu","cguunu","cnmgmu","cnmgnnu","cmnhnu","cmgumu","cnnhmu","cmhnmu","cgnu","cgunu","cmnhummu","cnguunnu","chnnu","cnmguunu","cngunu","cmgnnu","chuunnu","cnnguunu","cmgunnu","cmhnnu","cgunmu","cnnhmnu","cnmhunmu","cnmhummu","cmmhunnu","cmngmmu","cguumnu","cguunnu","cmnhuunu","cgmmu","cngumnu","cmhummu","cmhuumu","cmmhumu","chunmu","cmmhummu","cnmguunnu","cnngumu","cmhuunu","cnngunmu","cmgmmu","cnhummu","cguunmu","cmmhuumnu","cnnhnmu","cgmnu","cnmhnu","cmnhnnu","chumnu","cgumnu","chuumu","cnmgmnu","cnmhuunnu","cmgumnu","chmmu","cguummu","cmmguumu","cnhumnu","cnmhuumu","cmmhumnu","cnmhuummu","cnhmmu","cnngmu","chnmu","cmmhnu","cngmu","cmhmmu","cngnmu","cmguumu","cgnnu","cmmgmu","cmhuummu","cmnguummu","cmnhuummu","cmhunnu","cmguumnu","cmmgummu","cmnhuumnu","cnnhunmu","cmhmu","cmngmnu","cmmguunmu","cmhuunnu","chuunmu","cnmhmnu","cmhuunmu","cnguummu","cnguunu","chuummu","cnnhuunnu","cngmmu","cnnguummu","cnnhuumu","cmguunu","cmngunnu","cnhuunu","cnmhumnu","cmmhuunu","cnmhmu","cmgmu","cmmhmnu","cmguunnu","cmmhunmu","cmgunmu","cngunmu","cnnhnnu","cngnnu","cnmgnu","cnmgunnu","cmgnu","cmnhunu","cnmgunmu","cnnhumu","cnhnmu","chuumnu","cmgmnu","cnnhummu","cmhunmu","cmmhuumu","cnmgnmu","cnnhuumnu","cmngmu","cnnguumnu","cmngumu","cmmguummu","cnngummu","cnmgumnu","cnngnu","cmguunmu","cnhuunnu","cmhumu","cngmnu","cmnhunnu","cmnhunmu","cnguunmu","cmngummu","cmmhunu","cnhuumnu","cnnguumu","cmmgnnu","cmnguunu","cmmhuunnu","cnhuunmu","cnmgummu","cnmhuunmu","cnmhumu","cmnguumnu","cnngnnu","cmngunu","cmngnmu","cmhumnu","cnngunu","cmnhuunnu","cmmgunu","cnngmnu","cmnguumu","cnmguunmu","cmnhmnu","cmngnu","cnmgumu","cmnhuunmu","cnmhuumnu","cmnhnmu","cmgnmu","cnnhmmu","cnnhuunmu","cmmguumnu","cmngumnu","cnnguunmu","cmmguunu","cmnguunnu","cmmgmnu","cmngunmu","cnngmmu","cnngnmu","cnnguunnu","cmmhnmu","cngummu"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
            
            test = new ConvenienceRegexTesting("c(m|n)*(g|h)u*(m|n)+u", Arrays.asList("cnhuunmu","cnngunnu","cnhumu","cmhmmu","chunnu","cguummu","cguumu","cmgnnu","chnmu","cguunnu","cmnhunu","chumu","cnguumu","cngnmu","cmhummu","cgnmu","cmgmmu","cnnguumu","cmhuunu","cgmu","cgumnu","cmngumnu","cmhumu","cnguunu","cnhunnu","cmgnu","cngumnu","cnngunmu","cmmgunu","cmhuumu","cnnhunu","chmnu","cnnhnnu","cnnguunnu","chuumu","cnngummu","cnhnu","cnhunu","chmu","cnhumnu","cmnguumu","cmnhummu","chunu","cmgunu","cguunu","cngnnu","chuummu","cnmhuunmu","chuunu","cnmgumu","cgummu","chumnu","cmngunmu","cmgumnu","cgnnu","cnmhmu","cmnhunnu","cnhuunu","cnnhuunnu","cngunnu","cgmmu","cnnhummu","cmnguunu","chuunnu","cgnu","cmnhumnu","chnnu","cmgmu","cnhuumu","cmmguunu","cmhnmu","cnmhnnu","chuumnu","cmgumu","cngmmu","cgmnu","chunmu","cmgnmu","cmmhuumu","cngunu","cnngmu","cguunmu","cnhmmu","cguumnu","cmmhmu","cmnhumu","cmmhunnu","cmnguunmu","cnmgmmu","chnu","cnnguunu","cnngnmu","cmhmu","cmgunnu","cnngunu","cnmhnu","cmngmu","cmnhuunu","cmhuunmu","cnmhunu","cmhumnu","chummu","cmngmmu","cnnhumu","cnmgnu","cnmgumnu","cmhnnu","cmmhuumnu","cnmhumnu","cnhuunnu","cmnhuumu","cnmgunu","cnnhmnu","cnmhuumu","cmmhumnu","cmmhuummu","cngmu","cmmhmnu","cnmguumu","cngunmu","cmnhuummu","chuunmu","cmhnu","cmmguummu","cnguummu","cnmhmmu","cmmhunmu","cmngnmu","cngummu","cnmgmnu","cnmhunmu","cmguunu","cnnguumnu","cmmhumu","cnmhuunnu","cnguunnu","cmguumu","cmmgmu","cnnhumnu","cmngmnu","cgunmu","cmngunu","cmhmnu","cnmgmu","cmmhunu","cmhunu","cmngummu","cmnhuunnu","cnnhuunu","cnnhuunmu","cnhmu","cmmgnu","cnngnu","cmgunmu","cmnguumnu","cmguumnu","cnguunmu","cmgummu","cmmgunnu","cgunnu","cmnhuumnu","cgumu","cmmhmmu","cnnguummu","cgunu","cnmhuunu","cnguumnu","cnhnmu","cmnhnu","cnnhunnu","cmnhnnu","cmngnu","cnhnnu","cmguunnu","cngnu","cnngmmu","cngmnu","cmmhnu","cnmgunnu","cmmguunnu","cnmgummu","cnhmnu","cnmguunmu","cnmhnmu","cmmgumnu","cnnhnu","cnngnnu","cnmhummu","cmnhunmu","cmmhnmu","cnngumnu","cmmguumu","cnngumu","cnhunmu","cmmguunmu","cnmhuumnu","cnhummu","cnnhmu","cmhuummu","cnmguunnu","cnnhuumu","cmhunnu","cmngumu","cngumu","cnmhunnu","cmmhnnu","cmnhnmu","cmhuumnu","cmguummu","cmmgnmu","chmmu","cmnhmmu","cmngnnu","cmmhuunu","cnmhmnu","cmmhummu","cnmguumnu","cnmhumu","cnmguummu","cmhuunnu","cmmgunmu","cmmgumu","cmmgmnu","cnnhmmu","cmguunmu","cmnhmu","cnmguunu","cnmgnnu","cnngmnu","cmmgummu","cnnhuumnu","cnmhuummu","cmnhmnu","cnhuummu","cmmhuunnu","cmnhuunmu","cnnhunmu","cnnhnmu"));
            Assert.assertTrue(test.actuallyAbsent.isEmpty());
            Assert.assertTrue(test.actualExamples.containsAll(test.expectedExamples));
        }
}
