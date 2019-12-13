### Functionalities
__1a. edu.lab.wsalab.common.Preprocessor.removeSGML(String)__      
It uses regular expression `</?\\w*/?>` to match the tags and replaces all matching tab with character `" "`.  
  
__1b. edu.lab.wsalab.common.Preprocessor.tokenize(String)__  
It first replaces all characters except `A-Z`, `a-z`, and `" "` with `" "`. Then replaces more than one `" "` with single `" "`. It trims the text and splits text on `" "` to obtain tokens.  
  
__2a. edu.lab.wsalab.Counter.countUniqueWords(String[])__  
It stores all tokens in `HashSet` which only stores distinct tokens. It then calculates size of `HashSet`. (Vocabulary size: 8184)
  
__2b. edu.lab.wsalab.Counter.calWordToFreq(String[], int)__  
It creates `Map<String, Integer>` which stores word as key and frequency as integer. It runs loop over array of words and increments count. The map is then sorted based on the entry value. It then returns 10 top words.

__2c.__  
The 10 top words are:   
the: 20204  
of: 14032  
a: 8263  
and: 7116  
in: 5044  
to: 4725  
is: 4118  
for: 3714  
with: 2444  
are: 2431  
All are meaningless.  
  
__2d. edu.lab.wsalab.Counter.minHalfWordCount(Map<String, Integer>)__  
It calculates the minimum number of unique words accounting for half of the total number of words in the collection by adding every entry value to half sum and checking if it equals half count. If not removes the previously added word frequency and continues. (Count: 80)    
  
__3.__     
__edu.lab.wsalab.common.Preprocessor.applyStemmer(String[])__  
It applies Porter Stemmer to each word. The Porter Stemming implementation is obtained from `org.apache.opennlp`.  
  
__edu.lab.wsalab.common.Preprocessor.removeStopWords(String[])__  
It removes stop words listed in stopwords.txt file in resources folder.  
  
Vocabulary size: 5402  
  
Top 10 words:  
flow: 2458  
ar: 2456   
w: 1643  
b: 1540  
pressur: 1522  
t: 1521  
number: 1449  
boundari: 1403  
layer: 1350  
effect: 1147  
    
Minimum number of unique words accounting for half of the total number of words: 150   
 
__4, 5, 6__  
The documentation of methods in `edu.lab.wsalab.Application` explains the working mechanisms.
  
### Instructions
__To build the project__
1. Install gradle in the system from [this](https://gradle.org/install/).
2. In command line, go to project folder and run command `gradle clean build`

__To run the project__
(Use IDE to run this project)
1. Run `edu.lab.wsalab.Main` file to obtain output of questions from 1 to 3.
2. Run `edu.lab.wsalab.Application` file and select option to perform tasks: Index, Query, Measure Performance

__Note__  
cranfield data is downloaded from this site:
http://web.eecs.umich.edu/~mihalcea/courses/498IR/Resources/cranfield.zip
  
        
            
*Prepared by Joshana Shakya*



