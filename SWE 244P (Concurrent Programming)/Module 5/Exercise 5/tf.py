#REF: https://www.guru99.com/python-multithreading-gil-example.html
#     https://realpython.com/intro-to-python-threading/
#     https://www.tutorialspoint.com/concurrency_in_python/concurrency_in_python_pool_of_threads.htm

import re, sys, collections
import glob
import os
from concurrent.futures import ThreadPoolExecutor
import time
import threading

#Read multiple text files .txt and loop through each one concurrently 
#Count each instance of the frequent word
#Push results to the print 

counts = collections.Counter()
threadlock = threading.Lock() #create a threadlock similar to a semaphore or Lock() in java

class frequencies:

    def fileReader():
        path = os.getcwd() #Get current directory (make sure .txt files are in cwd)
        textFiles = glob.glob("{0}/*.txt".format(path)) #Get text files in the path with anything that ends in .txt
        # for i in textFiles:
        #     print(i)
        return textFiles



    def toString(counts):
        print(("\n---------- Word counts (top 40) -----------"))
        for (w, c) in counts.most_common(40):
            print('{0}: {1}'.format(w, c))



    def fileProcessor(file):
        # threadlock.acquire() #acquire a lock for synchronization (dont need - this will cause sequential processing)
        print("Started: {0}".format(str(file)))
        print(threading.current_thread().getName()) #see current thread running

        stopwords = set(open('stop_words').read().split(','))
        words = re.findall('\w{3,}', open(file).read().lower())
        for w in words:
            if w not in stopwords:
                if w in counts:
                    counts[w] = counts[w] + 1
                else:
                    counts[w] = 1

        print("Ended: {0}".format(str(file)))
        # threadlock.release() #release lock for other threads (dont need - this will cause sequential processing)


def main():

    time1 = time.time() #start timer

    frequent = frequencies #instantiate class 
    textFiles = frequent.fileReader() #get the list of textfiles to iterate through

    with ThreadPoolExecutor(max_workers=4) as executor: #create 4 threads 
        executor.map(frequent.fileProcessor, iter(textFiles)) #call fileProcessor as a function object for each iterable of text file concurrently
    ##### ThreadPoolExectuor is executing this code below #####

    # t1 = threading.Thread(target = frequent.fileProcessor(textFiles[0]))
    # t2 = threading.Thread(target = frequent.fileProcessor(textFiles[1]))
    # t3 = threading.Thread(target = frequent.fileProcessor(textFiles[2]))
    # t4 = threading.Thread(target = frequent.fileProcessor(textFiles[3]))

    # t1.start()
    # t2.start()
    # t3.start()
    # t4.start()

    # t1.join()
    # t2.join()
    # t3.join()
    # t4.join()

    ##### ThreadPoolExectuor is executing this code above #####
    
    frequent.toString(counts) #print results 

    time2 = time.time() #end timer
    timeDiff = round((time2 - time1)*1000) #convert timer units to ms

    print("\nRuntime:", timeDiff, "ms")


if __name__ == '__main__':
    main()