import sys, string
import numpy as np
from numpy import core as npc

# Example input: "Hello  World!" 
characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='<U1')

# Normalize
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.upper(characters)
# Result: array([' ', 'h', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'w', 'o', 'r', 'l', 'd', ' ', ' '], dtype='<U1')

### Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)
# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Result: array([[ 0,  6],
#                [ 6,  7],
#                [ 7, 13],
#                [13, 14]], dtype=int64)
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)

# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'), 
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Result: array(['hello', 'world'], dtype='<U5')

# Next, let's remove stop words
stop_words = np.array(list(set(open('stop_words.txt').read().split(','))))
stop_words = np.char.upper(stop_words) #convert to uppper for stop words to avoid discrepancy

ns_words = swords[~np.isin(swords, stop_words)]


# Leet Implementation

# Alphabet reference: https://simple.wikipedia.org/wiki/Leet
# https://www.gamehouse.com/blog/leet-speak-cheat-sheet/
leetAlphabet = "ДBCD€FGH!JKLMNΘPQRSTµVWXYZ" 

# NOTE: Make a translation table so that the characters will be used in the mapping of word frequencies
# Translate: https://www.tutorialspoint.com/python/string_maketrans.htm
characterMap = str.maketrans(string.ascii_uppercase , leetAlphabet) # uppercase characters and this will map each character in ns_words
translatedLeet = npc.defchararray.translate(ns_words, characterMap) # translate each characterMap to remove stop words


# 2-grams implementation
window = np.repeat(translatedLeet, 2) # repeat like a "sliding window"


# Reference: https://stackoverflow.com/questions/18691084/what-does-1-mean-in-numpy-reshape
words = np.reshape(window[1:-1], (-1, 2)) # reshape dimensions (-1 means unknown dimension --> result is 1-D)


### Finally, count the word occurrences
uniq, counts = np.unique(words, axis = 0, return_counts = True)
wf_sorted = sorted(zip(uniq, counts), key = lambda t: t[1], reverse = True)

# change from top 25 to 5 (: means start from the beginning and go to the end (5))
print("\n--------------")
print("Frequency List")
print("--------------")

for twoGram, frequency in wf_sorted[:5]: 
    print(twoGram[0], twoGram[1], '-', frequency) # get 2-grams and its respective frequency

print("\n")